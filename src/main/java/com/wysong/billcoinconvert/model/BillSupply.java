package com.wysong.billcoinconvert.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * @since July 24, 2019.
 */
public class BillSupply {
    @ApiModelProperty(notes = "The value of the bill, like 1, 2, 5, 10...")
    private int billValue;
    @ApiModelProperty(notes = "The number of bills that will be provided.")
    private int numProvided;

    public BillSupply() {
    }

    public BillSupply(int billValue, int numProvided) {
        this.billValue = billValue;
        this.numProvided = numProvided;
    }

    public int getBillValue() {
        return billValue;
    }

    public BillSupply setBillValue(int billValue) {
        this.billValue = billValue;
        return this;
    }

    public int getNumProvided() {
        return numProvided;
    }

    public BillSupply setNumProvided(int numProvided) {
        this.numProvided = numProvided;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillSupply that = (BillSupply) o;
        return getBillValue() == that.getBillValue() &&
                getNumProvided() == that.getNumProvided();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBillValue(), getNumProvided());
    }
}
