/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 George Ladutsko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.ladutsko.jhfs.fs.internal;

import com.github.ladutsko.jhfs.fs.FsDescriptor;
import com.github.ladutsko.jhfs.fs.FsEntry;
import com.github.ladutsko.jhfs.fs.FsService;
import com.github.ladutsko.jhfs.fs.FsUtil;
import com.github.ladutsko.jhfs.fs.PathInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.github.ladutsko.jhfs.fs.FsUtil.pathInfo;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.isReadable;
import static java.nio.file.Files.newDirectoryStream;
import static java.nio.file.Files.readAllBytes;
import static java.util.Collections.unmodifiableMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * Created by George Ladutsko on 16.02.2018.
 */
@Service
public class FsServiceImpl implements FsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FsServiceImpl.class);

    private static final SubordinateFilter SUBORDINATE_FILTER = new SubordinateFilter();

    private final String charset;
    private final Map<String, FsDescriptor> descriptors;

    public FsServiceImpl(FsServiceProperties config) {
        charset = config.getCharset();
        descriptors = unmodifiableMap(config.getDescriptors().stream()
                                                             .collect(toMap(FsDescriptor::getId,
                                                                            identity(),
                                                                            (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); },
                                                                            LinkedHashMap::new)));
    }

    @Override
    public Map<String, FsDescriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public FsEntry getEntry(String path) {
        PathInfo pi = pathInfo(path);
        FsDescriptor descriptor = getDescriptor(pi.getFsId());
        Path p = getPath(descriptor.getUri(), pi.getPath());
        if (!SUBORDINATE_FILTER.accept(p)) {
            throw new UncheckedIOException(new FileNotFoundException(path));
        }

        try {
            return getEntry(descriptor, p);
        } catch (IOException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new UncheckedIOException(new IOException("Something wrong with " + path));
        }
    }

    @Override
    public List<FsEntry> getChildren(String path){
        PathInfo pi = pathInfo(path);
        FsDescriptor descriptor = getDescriptor(pi.getFsId());
        Path p = getPath(descriptor.getUri(), pi.getPath());
        if (!isDirectory(p)) {
            throw new UnsupportedOperationException();
        }

        try (DirectoryStream<Path> ds = newDirectoryStream(p, SUBORDINATE_FILTER)) {
            List<FsEntry> entries = new ArrayList<>();
            for (Path dsPath : ds) {
                try {
                    entries.add(getEntry(descriptor, dsPath));
                } catch (Exception e) {
                    LOGGER.warn("Unexpected exception", e);
                }
            }
            return entries;
        } catch (IOException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new UncheckedIOException(new IOException("Something wrong with " + path));
        }
    }

    @Override
    public String probeContentType(String path) {
        PathInfo pi = pathInfo(path);
        FsDescriptor descriptor = getDescriptor(pi.getFsId());
        Path p = getPath(descriptor.getUri(), pi.getPath());
        if (isDirectory(p)) {
            return null;
        }

        try {
            return Files.probeContentType(p);
        } catch (IOException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new UncheckedIOException(new IOException("Something wrong with " + path));
        }
    }

    @Override
    public InputStream newInputStream(String path) {
        PathInfo pi = pathInfo(path);
        FsDescriptor descriptor = getDescriptor(pi.getFsId());
        Path p = getPath(descriptor.getUri(), pi.getPath());
        if (isDirectory(p)) {
            throw new UnsupportedOperationException();
        }

        try {
            return Files.newInputStream(p);
        } catch (IOException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new UncheckedIOException(new IOException("Something wrong with " + path));
        }
    }

    protected FsDescriptor getDescriptor(String id) {
        FsDescriptor descriptor = descriptors.get(id);
        if (null == descriptor) {
            throw new UncheckedIOException(new FileNotFoundException(id));
        }

        return descriptor;
    }

    protected Path getPath(URI uri, String path) {
        return Paths.get(uri).resolve(FsUtil.normalize(Paths.get(path).normalize().toString()));
    }

    protected SimpleFsEntry getEntry(FsDescriptor descriptor, Path p) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(p, BasicFileAttributes.class);

        Path commentPath = Paths.get(p.toString() + ".comment");
        String description = (exists(commentPath) ? new String(readAllBytes(commentPath), charset) : "");

        return new SimpleFsEntry().setPath(descriptor.getId() + "/" + FsUtil.normalize(Paths.get(descriptor.getUri()).relativize(p).toString()))
                                  .setName(p.getFileName().toString())
                                  .setContainer(attrs.isDirectory())
                                  .setCreationTime(attrs.creationTime().toInstant())
                                  .setLastModifiedTime(attrs.lastModifiedTime().toInstant())
                                  .setLastAccessTime(attrs.lastAccessTime().toInstant())
                                  .setSize(attrs.size())
                                  .setDescription(description);
    }

    public static class SubordinateFilter implements DirectoryStream.Filter<Path> {

        private static final Pattern EXT_PATTERN = Pattern.compile("(?<=\\.)(?:comment|nfo)$", Pattern.CASE_INSENSITIVE); // TODO configurable

        @Override
        public boolean accept(Path entry) {
            return isReadable(entry)
                   && (isDirectory(entry)
                       || (!entry.getFileName().toString().startsWith(".")
                           && !EXT_PATTERN.matcher(entry.toString()).find()));
        }
    }
}
