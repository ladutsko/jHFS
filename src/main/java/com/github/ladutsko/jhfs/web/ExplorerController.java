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

import com.github.ladutsko.jhfs.fs.FsDescriptor;
import com.github.ladutsko.jhfs.fs.FsEntry;
import com.github.ladutsko.jhfs.fs.FsService;
import com.github.ladutsko.jhfs.web.model.UiBreadcrumbModel;
import com.github.ladutsko.jhfs.web.model.UiBreadcrumbModelParser;
import com.github.ladutsko.jhfs.web.model.UiEntryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;

import static com.github.ladutsko.jhfs.web.WebUtil.extractPath;
import static com.github.ladutsko.jhfs.web.model.ModelComparators.uiEntryComparator;
import static java.lang.Math.max;
import static java.util.stream.Collectors.toList;

/**
 * Created by George Ladutsko on 16.02.2018.
 */
@RequestMapping("/explorer")
@Controller
public class ExplorerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExplorerController.class);

    private final FsService fsService;

    private long startTime;
    private Map<String, FsDescriptor> descriptors;
    private UiBreadcrumbModelParser uiBreadcrumbModelParser;
    private OrderParser orderParser;

    public ExplorerController(FsService fsService) {
        this.fsService = fsService;
    }

    @PostConstruct
    public void init() {
        startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        descriptors = fsService.getDescriptors();
        LOGGER.debug("Descriptors: {}", descriptors);
        uiBreadcrumbModelParser = new UiBreadcrumbModelParser(descriptors);
        orderParser = new OrderParser();
    }

    @GetMapping("/**")
    public ModelAndView handleGet(ServletWebRequest request, @RequestParam(name = "o", required = false) String orderRaw) {
        String path = extractPath(request.getRequest());
        LOGGER.debug("handleGet(path={}, order={})", path, orderRaw);

        FsEntry entry = fsService.getEntry(path);
        if (request.checkNotModified(max(startTime, entry.getLastModifiedTime().toEpochMilli()))) {
            LOGGER.debug("handleGet: NOT_MODIFIED");
            return null;
        }

        ModelAndView model = new ModelAndView("explorer");
        model.addObject("path", path);
        model.addObject("parent", entry);

        // Breadcrumb

        List<UiBreadcrumbModel> breadcrumb = uiBreadcrumbModelParser.parse(path);
        LOGGER.debug("Breadcrumb: {}", breadcrumb);
        model.addObject("breadcrumb", breadcrumb);

        // Order

        Order order = orderParser.parse(orderRaw);
        LOGGER.debug("Order: {}", order);
        model.addObject("order", order);

        // Entries

        List<UiEntryModel> entries = fsService.getChildren(path)
                                              .stream()
                                              .map(UiEntryModel::of)
                                              .sorted(uiEntryComparator(order))
                                              .collect(toList());
        LOGGER.debug("Entries: {}", entries);
        model.addObject("entries", entries);

        return model;
    }
}
