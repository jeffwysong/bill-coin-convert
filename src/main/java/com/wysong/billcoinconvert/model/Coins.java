package com.wysong.billcoinconvert.model;

/**
 * @since July 24, 2019.
 */
public enum Coins implements Coin {
    QUARTER(25),
    DIME(10),
    NICKEL(5),
    PENNY(1);

    private final int value;

    Coins(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
