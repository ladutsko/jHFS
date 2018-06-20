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

import com.github.ladutsko.jhfs.fs.FsDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Created by George Ladutsko on 14.06.2018.
 */
public class UiBreadcrumbModelParser {

    private final Map<String, FsDescriptor> descriptors;

    public UiBreadcrumbModelParser(Map<String, FsDescriptor> descriptors) {
        this.descriptors = requireNonNull(descriptors);
    }

    public List<UiBreadcrumbModel> parse(String raw) {
        List<UiBreadcrumbModel> breadcrumb = new ArrayList<>();
        StringBuilder breadcrumbBuilder = new StringBuilder();

        for (String name : raw.split("/")) {
            if (0 < breadcrumbBuilder.length()) {
                breadcrumbBuilder.append("/");
            }
            breadcrumbBuilder.append(name);

            String displayName;

            if (breadcrumb.isEmpty() && descriptors.containsKey(name)) {
                displayName = descriptors.get(name).getDisplayName();
            } else {
                displayName = name.trim();
            }

            breadcrumb.add(new UiBreadcrumbModel().setDisplayName(displayName)
                                                  .setPath(breadcrumbBuilder.toString()));
        }

        return breadcrumb;
    }
}
