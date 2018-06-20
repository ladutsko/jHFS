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

package com.github.ladutsko.jhfs.fs;

/**
 * Created by George Ladutsko on 18.03.2018.
 */
public class PathInfo {

    private String fsId;
    private String path;
    private String name;
    private String ext;

    public String getFsId() {
        return fsId;
    }

    public PathInfo setFsId(String fsId) {
        this.fsId = fsId;
        return this;
    }

    public String getPath() {
        return path;
    }

    public PathInfo setPath(String path) {
        this.path = path;
        return this;
    }

    public String getName() {
        return name;
    }

    public PathInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getExt() {
        return ext;
    }

    public PathInfo setExt(String ext) {
        this.ext = ext;
        return this;
    }
}
