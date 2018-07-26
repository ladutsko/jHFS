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
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;

import static java.nio.channels.Channels.newChannel;

/**
 * Created by George Ladutsko on 26.07.2018.
 */
public abstract class AbstractFsEntryResource implements Resource {

    private final FsEntry entry;

    public AbstractFsEntryResource(FsEntry entry) {
        this.entry = entry;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException("getURL");
    }

    @Override
    public URI getURI() {
        throw new UnsupportedOperationException("getURI");
    }

    @Override
    public File getFile() {
        throw new UnsupportedOperationException("getFile");
    }

    @Override
    public ReadableByteChannel readableChannel() throws IOException {
        return newChannel(getInputStream());
    }

    @Override
    public long contentLength() {
        return entry.getSize();
    }

    @Override
    public long lastModified() {
        return entry.getLastModifiedTime().toEpochMilli();
    }

    @Override
    public Resource createRelative(String relativePath) {
        throw new UnsupportedOperationException("createRelative");
    }

    @Override
    public String getFilename() {
        return entry.getName();
    }

    @Override
    public String getDescription() {
        return toString();
    }

    @Override
    public int hashCode() {
        return entry.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this
                || (obj instanceof Resource && ((Resource) obj).getDescription().equals(getDescription())));
    }

    @Override
    public String toString() {
        return "Resource{name=" + entry.getName() + "}";
    }

    protected FsEntry getEntry() {
        return entry;
    }
}
