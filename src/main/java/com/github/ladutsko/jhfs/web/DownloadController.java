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

package com.github.ladutsko.jhfs.web;

import com.github.ladutsko.jhfs.fs.FsEntry;
import com.github.ladutsko.jhfs.fs.FsService;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.github.ladutsko.jhfs.web.WebUtil.extractPath;
import static java.util.stream.Collectors.toList;

/**
 * Created by George Ladutsko on 16.03.2018.
 */
@RequestMapping("/download")
@RestController
public class DownloadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);

    private static final String UTF8 = StandardCharsets.UTF_8.name();

    private static final String BINARY_MIME_TYPE = "application/octen-stream";
    private static final String TAR_MIME_TYPE    = "application/x-tar";
    private static final String TAR_EXT          = ".tar";

    private final FsService fsService;

    private int bufferSize;

    public DownloadController(FsService fsService) {
        this.fsService = fsService;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    @Value("${jhfs.fs.download.bufferSize:8192}")
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @GetMapping("/**")
    public void handleGet(ServletWebRequest request, HttpServletResponse response) throws IOException {
        String path = extractPath(request.getRequest());
        LOGGER.debug("handleGet(path={})", path);

        FsEntry entry = fsService.getEntry(path);
        if (entry.isContainer()) {
            downloadTar(entry, fsService.getChildren(path), response);
        } else {
            if (request.checkNotModified(entry.getLastModifiedTime().toEpochMilli())) {
                LOGGER.debug("handleGet: NOT_MODIFIED");
                return;
            }

            download(entry, response);
        }
    }

    @PostMapping("/**")
    public void handlePost(HttpServletRequest request, HttpServletResponse response,
                           @RequestBody(required = false) MultiValueMap<String, String> values) throws IOException {
        String path = extractPath(request);
        LOGGER.debug("handlePost(path={}, values={})", path, values);

        FsEntry entry = fsService.getEntry(path);

        List<FsEntry> entries = Optional.ofNullable(values)
                                        .map(map -> map.get("name"))
                                        .filter(names -> !names.isEmpty())
                                        .map(names -> names.stream()
                                                           .map(name -> fsService.getEntry(path + "/" + name))
                                                           .collect(toList()))
                                        .orElseGet(() -> fsService.getChildren(path));

        downloadTar(entry, entries, response);
    }

    protected void download(FsEntry entry, HttpServletResponse response) throws IOException {
        LOGGER.debug("download(entry={})", entry);

        response.setContentType(Optional.ofNullable(fsService.probeContentType(entry.getPath())).orElse(BINARY_MIME_TYPE));
        response.setContentLengthLong(entry.getSize());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''"
                                                            + URLEncoder.encode(entry.getName(), UTF8)
                                                                        .replace("+", "%20"));

        try (InputStream in = fsService.newInputStream(entry.getPath());
             OutputStream out = response.getOutputStream()) {
            copy(in, out, new byte[bufferSize]);
        }
    }

    protected void downloadTar(FsEntry parent, Collection<? extends FsEntry> fsEntries, HttpServletResponse response) throws IOException {
        LOGGER.debug("downloadTar(parent={}, entry={})", parent, fsEntries);

        List<FsEntry> entries = fsEntries.stream()
                                         .filter(entry -> !entry.isContainer())
                                         .collect(toList());
        if (entries.isEmpty()) {
            return;
        }

        response.setContentType(TAR_MIME_TYPE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''"
                                                            + URLEncoder.encode(parent.getName() + TAR_EXT, UTF8)
                                                                        .replace("+", "%20"));

        try (TarArchiveOutputStream tar = new TarArchiveOutputStream(response.getOutputStream(), UTF8)) {
            tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            tar.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);

            byte[] buf = new byte[bufferSize];
            for (FsEntry entry : entries) {
                try (InputStream in = fsService.newInputStream(entry.getPath())) {
                    TarArchiveEntry tarEntry = new TarArchiveEntry(entry.getName());
                    tarEntry.setSize(entry.getSize());
                    tarEntry.setModTime(entry.getLastModifiedTime().toEpochMilli());
                    tar.putArchiveEntry(tarEntry);

                    copy(in, tar, buf);

                    tar.closeArchiveEntry();
                }
            }
            tar.finish();
        }
    }

    private static void copy(InputStream in, OutputStream out, byte[] buf) throws IOException {
        for (;;) {
            int len = in.read(buf);
            if (0 < len) {
                out.write(buf, 0, len);
            } else if (0 > len) {
                break;
            }
        }
    }
}
