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
public class FsUtil {

    private FsUtil() {
        // There are no use cases for this class where you need to build an object. You can only use static items. I am preventing you from even trying to build an object of this class.
    }

    public static PathInfo pathInfo(String p) {
        p = normalize(p);
        int pos = p.indexOf('/');
        if (0 < pos) {
            String fsId = p.substring(0, pos);
            String path = p.substring(pos+1);
            if (1 == path.length()) {
                return new PathInfo().setFsId(fsId).setPath("/");
            }

            if ('/' == path.charAt(path.length()-1)) {
                path = path.substring(0, path.length()-1);
            }

            pos = path.lastIndexOf('/');
            String name = path.substring(pos+1);

            pos = name.lastIndexOf('.');
            String ext = (0 < pos ? name.substring(pos+1) : null);

            return new PathInfo().setFsId(fsId).setPath(path).setName(name).setExt(ext);
        } else {
            return new PathInfo().setFsId(p).setPath("/");
        }
    }

    public static String normalize(String p) {
        p = p.trim().replace('\\', '/');
        if (p.isEmpty()) {
            return "";
        }

        int start = 0;
        if ('/' == p.charAt(0)) {
            start++;
        }

        int finish = p.length();
        if ('/' == p.charAt(p.length()-1)) {
            finish--;
        }

        return (start >= finish ? "" : p.substring(start, finish));
    }
}
