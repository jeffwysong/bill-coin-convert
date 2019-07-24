package com.wysong.billcoinconvert.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @since July 24, 2019.
 */
@Entity
public class CoinBank {
    public static final int DEFAULT_INITIALIZED_NUMBER = 100;

    @Id
    private int coinValue;

    private int numAvailable;

    public CoinBank() {
    }

    public int getCoinValue() {
        return coinValue;
    }

    public CoinBank setCoinValue(int coinValue) {
        this.coinValue = coinValue;
        return this;
    }

    public int getNumAvailable() {
        return numAvailable;
    }

    public CoinBank setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoinBank coinBank = (CoinBank) o;
        return getCoinValue() == coinBank.getCoinValue() &&
                getNumAvailable() == coinBank.getNumAvailable();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCoinValue(), getNumAvailable());
    }
}
