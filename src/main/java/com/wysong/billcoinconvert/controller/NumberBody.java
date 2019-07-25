package com.wysong.billcoinconvert.controller;

import java.util.Objects;

/**
 * @since July 24, 2019.
 */
public class NumberBody {
    private Integer number;

    public NumberBody() {
    }

    public NumberBody(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public NumberBody setNumber(Integer number) {
        this.number = number;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberBody that = (NumberBody) o;
        return Objects.equals(getNumber(), that.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber());
    }
}
