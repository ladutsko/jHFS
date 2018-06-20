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

import java.net.URI;

/**
 * Created by George Ladutsko on 16.02.2018.
 */
public class SimpleFsDescriptor implements FsDescriptor {

    private static final long serialVersionUID = 4621975494248056217L;

    private String id;
    private String displayName;
    private String description;
    private URI uri;

    @Override
    public String getId() {
        return id;
    }

    public SimpleFsDescriptor setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public SimpleFsDescriptor setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public SimpleFsDescriptor setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    public SimpleFsDescriptor setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder(100).append("SimpleFsDescriptor{")
                                     .append("id='").append(id).append('\'')
                                     .append(", displayName='").append(displayName).append('\'')
                                     .append(", description='").append(description).append('\'')
                                     .append(", uri=").append(uri)
                                     .append('}')
                                     .toString();
    }
}
