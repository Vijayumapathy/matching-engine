package com.vijay.matching.model;

public enum OrderType {
    MKT(1),
    LMT(2),
    FOK(3),
    IOC(4);

    private final int value;

    OrderType(final int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static OrderType get(final int value) {
        switch (value) {
            case 1:
                return MKT;
            case 2:
                return LMT;
            case 3:
                return FOK;
            case 4:
                return IOC;
            default:
                throw new IllegalArgumentException("Unknown value: " + value);
        }
    }
}