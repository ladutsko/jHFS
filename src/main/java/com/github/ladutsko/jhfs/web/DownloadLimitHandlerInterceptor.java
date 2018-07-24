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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.net.InetAddress.getLoopbackAddress;

/**
 * Created by George Ladutsko on 12.06.2018.
 */
public class DownloadLimitHandlerInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadLimitHandlerInterceptor.class);

    private final ConcurrentMap<InetAddress, AtomicInteger> registry = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        InetAddress addr = getRemoteAddr(request);
        AtomicInteger counter = registry.computeIfAbsent(addr, key -> new AtomicInteger());
        int count = counter.incrementAndGet();
        if (6 < count) { // TODO configurable
            counter.decrementAndGet();
            LOGGER.debug("Parallel download limit for {} is exceeded. Return 429" , addr);
            response.sendError(429, "Too Many Requests");
            return false;
        }

        LOGGER.debug("Parallel download count for {} is {}" , addr, count);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        InetAddress addr = getRemoteAddr(request);
        AtomicInteger counter = registry.computeIfAbsent(addr, key -> new AtomicInteger());
        int count = counter.decrementAndGet();
        LOGGER.debug("Parallel download count for {} is {}" , addr, count);
    }

    private static InetAddress getRemoteAddr(HttpServletRequest request) {
        try {
            InetAddress addr = InetAddress.getByName(request.getRemoteAddr());
            if (addr.isSiteLocalAddress() || addr.isLoopbackAddress()) {
                return getLoopbackAddress();
            }

            return addr;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
