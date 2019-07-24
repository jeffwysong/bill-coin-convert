package com.wysong.billcoinconvert.controller;

import java.util.Objects;

/**
 * @since July 24, 2019.
 */
public class InitializeBody {
    private Integer number;

    public InitializeBody() {
    }

    public InitializeBody(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public InitializeBody setNumber(Integer number) {
        this.number = number;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InitializeBody that = (InitializeBody) o;
        return Objects.equals(getNumber(), that.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber());
    }
}
