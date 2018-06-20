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

import com.github.ladutsko.jhfs.web.ArrangementEnum;
import com.github.ladutsko.jhfs.web.Order;

import java.time.Instant;
import java.util.Comparator;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;

/**
 * Created by George Ladutsko on 14.06.2018.
 */
public class ModelComparators {

    public static final Comparator<UiEntryModel> DEFAULT_UI_ENTRY_COMPARATOR = comparing(UiEntryModel::isContainer, reverseOrder());

    private ModelComparators() {
        // There are no use cases for this class where you need to build an object. You can only use static items. I am preventing you from even trying to build an object of this class.
    }

    public static Comparator<UiEntryModel> uiEntryComparator(Order order) {
        switch (order.getOrder()) {
            case NAME:
                Comparator<String> nameComparator = String.CASE_INSENSITIVE_ORDER;
                if (ArrangementEnum.DESC == order.getArrangement()) {
                    nameComparator = nameComparator.reversed();
                }
                return DEFAULT_UI_ENTRY_COMPARATOR.thenComparing(UiEntryModel::getName, nameComparator);

            case MODIFIED:
                Comparator<Instant> modifiedComparator = ArrangementEnum.DESC == order.getArrangement() ? reverseOrder()
                                                                                                        : naturalOrder();
                return DEFAULT_UI_ENTRY_COMPARATOR.thenComparing(UiEntryModel::getLastModifiedTime, modifiedComparator);

            default:
                return DEFAULT_UI_ENTRY_COMPARATOR;
        }
    }
}
