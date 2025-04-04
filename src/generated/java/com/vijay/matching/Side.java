/* Generated SBE (Simple Binary Encoding) message codec. */
package com.vijay.matching;

@SuppressWarnings("all")
public enum Side
{
    Buy((byte)66),

    Sell((byte)83),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL((byte)0);

    private final byte value;

    Side(final byte value)
    {
        this.value = value;
    }

    /**
     * The raw encoded value in the Java type representation.
     *
     * @return the raw value encoded.
     */
    public byte value()
    {
        return value;
    }

    /**
     * Lookup the enum value representing the value.
     *
     * @param value encoded to be looked up.
     * @return the enum value representing the value.
     */
    public static Side get(final byte value)
    {
        switch (value)
        {
            case 66: return Buy;
            case 83: return Sell;
            case 0: return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
