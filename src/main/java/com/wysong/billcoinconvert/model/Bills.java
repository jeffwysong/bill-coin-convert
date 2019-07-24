package com.wysong.billcoinconvert.model;

/**
 * @since July 24, 2019.
 */
public enum Bills implements Bill {
    HUNDRED(100),
    FIFTY(50),
    TWENTY(20),
    TEN(10),
    FIVE(5),
    TWO(2),
    ONE(1);

    private final int value;

    Bills(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
