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

import com.github.ladutsko.jhfs.fs.FsEntry;

import java.time.Instant;

/**
 * Created by George Ladutsko on 16.02.2018.
 */
public class SimpleFsEntry implements FsEntry {

    private static final long serialVersionUID = -58082671456349859L;

    private String path;
    private String name;
    private boolean container;
    private String description;
    private Instant lastModifiedTime;
    private Instant lastAccessTime;
    private Instant creationTime;
    private long size;

    @Override
    public String getPath() {
        return path;
    }

    public SimpleFsEntry setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public SimpleFsEntry setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public SimpleFsEntry setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    @Override
    public Instant getLastAccessTime() {
        return lastAccessTime;
    }

    public SimpleFsEntry setLastAccessTime(Instant lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
        return this;
    }

    @Override
    public Instant getCreationTime() {
        return creationTime;
    }

    public SimpleFsEntry setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    @Override
    public boolean isContainer() {
        return container;
    }

    public SimpleFsEntry setContainer(boolean container) {
        this.container = container;
        return this;
    }

    @Override
    public long getSize() {
        return size;
    }

    public SimpleFsEntry setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public SimpleFsEntry setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder(256).append("SimpleFsEntry{")
                                     .append("path='").append(path).append('\'')
                                     .append(", NAME=").append(name)
                                     .append(", container=").append(container)
                                     .append(", lastModifiedTime=").append(lastModifiedTime)
                                     .append(", lastAccessTime=").append(lastAccessTime)
                                     .append(", creationTime=").append(creationTime)
                                     .append(", size=").append(size)
                                     .append(", description='").append(description).append('\'')
                                     .append('}')
                                     .toString();
    }
}
