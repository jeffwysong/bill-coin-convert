package com.wysong.billcoinconvert.model;

import java.util.Objects;

/**
 * @since July 24, 2019.
 */
public class CoinReturn {

    private int coinValue;

    private int numReturned;

    public CoinReturn() {
    }

    public int getCoinValue() {
        return coinValue;
    }

    public CoinReturn setCoinValue(int coinValue) {
        this.coinValue = coinValue;
        return this;
    }

    public int getNumReturned() {
        return numReturned;
    }

    public CoinReturn setNumReturned(int numReturned) {
        this.numReturned = numReturned;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoinReturn that = (CoinReturn) o;
        return getCoinValue() == that.getCoinValue() &&
                getNumReturned() == that.getNumReturned();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCoinValue(), getNumReturned());
    }
}
