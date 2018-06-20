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

package com.github.ladutsko.jhfs.web.model;

import com.github.ladutsko.jhfs.fs.FsEntry;
import com.github.ladutsko.jhfs.fs.FsUtil;
import com.github.ladutsko.jhfs.fs.PathInfo;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by George Ladutsko on 18.03.2018.
 */
public class UiEntryModel {

    private String path;
    private String name;
    private String ext;
    private boolean container;
    private String description;
    private Instant lastModifiedTime;
    private long size;
    private int hits;
    private boolean novelty;

    public static UiEntryModel of(FsEntry fsEntry) {
        PathInfo pi = FsUtil.pathInfo(fsEntry.getPath());
        return new UiEntryModel().setPath(fsEntry.getPath())
                                 .setName(fsEntry.getName())
                                 .setExt(pi.getExt())
                                 .setContainer(fsEntry.isContainer())
                                 .setDescription(fsEntry.getDescription())
                                 .setLastModifiedTime(fsEntry.getLastModifiedTime())
                                 .setSize(fsEntry.getSize())
                                 .setNovelty(1L >= Duration.between(fsEntry.getLastModifiedTime(), Instant.now()).toDays());
    }

    public String getPath() {
        return path;
    }

    public UiEntryModel setPath(String path) {
        this.path = path;
        return this;
    }

    public String getName() {
        return name;
    }

    public UiEntryModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getExt() {
        return ext;
    }

    public UiEntryModel setExt(String ext) {
        this.ext = ext;
        return this;
    }

    public boolean isContainer() {
        return container;
    }

    public UiEntryModel setContainer(boolean container) {
        this.container = container;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UiEntryModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public UiEntryModel setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public long getSize() {
        return size;
    }

    public UiEntryModel setSize(long size) {
        this.size = size;
        return this;
    }

    public int getHits() {
        return hits;
    }

    public UiEntryModel setHits(int hits) {
        this.hits = hits;
        return this;
    }

    public boolean isNovelty() {
        return novelty;
    }

    public UiEntryModel setNovelty(boolean novelty) {
        this.novelty = novelty;
        return this;
    }

    @Override
    public String toString() {
        return "UiEntryModel{"
               + "path='" + path + '\''
               + ", name='" + name + '\''
               + ", ext='" + ext + '\''
               + ", container=" + container
               + ", description='" + description + '\''
               + ", lastModifiedTime=" + lastModifiedTime
               + ", size=" + size
               + ", hits=" + hits
               + ", novelty=" + novelty
               + '}';
    }
}
