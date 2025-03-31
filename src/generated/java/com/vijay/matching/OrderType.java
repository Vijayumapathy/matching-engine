/* Generated SBE (Simple Binary Encoding) message codec. */
package com.vijay.matching;

@SuppressWarnings("all")
public enum OrderType
{
    MKT(1),

    LMT(2),

    FOK(3),

    IOC(4),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL(-2147483648);

    private final int value;

    OrderType(final int value)
    {
        this.value = value;
    }

    /**
     * The raw encoded value in the Java type representation.
     *
     * @return the raw value encoded.
     */
    public int value()
    {
        return value;
    }

    /**
     * Lookup the enum value representing the value.
     *
     * @param value encoded to be looked up.
     * @return the enum value representing the value.
     */
    public static OrderType get(final int value)
    {
        switch (value)
        {
            case 1: return MKT;
            case 2: return LMT;
            case 3: return FOK;
            case 4: return IOC;
            case -2147483648: return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
