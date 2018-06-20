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

import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

/**
 * Created by George Ladutsko on 14.06.2018.
 */
public class Order {

    public static final Order DEFAULT_ORDER = new Order(OrderEnum.NONE, ArrangementEnum.ASC);

    private final OrderEnum order;
    private final ArrangementEnum arrangement;

    public Order(OrderEnum order, ArrangementEnum arrangement) {
        this.order = requireNonNull(order);
        this.arrangement = requireNonNull(arrangement);
    }

    public OrderEnum getOrder() {
        return order;
    }

    public ArrangementEnum getArrangement() {
        return arrangement;
    }

    public Order reversed() {
        return new Order(order, ArrangementEnum.ASC == arrangement ? ArrangementEnum.DESC
                                                                   : ArrangementEnum.ASC);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order other = (Order) o;
        return order == other.order && arrangement == other.arrangement;
    }

    @Override
    public int hashCode() {
        return hash(order, arrangement);
    }

    @Override
    public String toString() {
        return new StringBuilder(39).append("Order{")
                                    .append("order=").append(order)
                                    .append(", arrangement=").append(arrangement)
                                    .append('}')
                                    .toString();
    }
}
