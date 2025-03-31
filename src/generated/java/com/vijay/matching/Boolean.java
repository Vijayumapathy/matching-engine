/* Generated SBE (Simple Binary Encoding) message codec. */
package com.vijay.matching;

@SuppressWarnings("all")
public enum Boolean
{
    F((short)0),

    T((short)1),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL((short)255);

    private final short value;

    Boolean(final short value)
    {
        this.value = value;
    }

    /**
     * The raw encoded value in the Java type representation.
     *
     * @return the raw value encoded.
     */
    public short value()
    {
        return value;
    }

    /**
     * Lookup the enum value representing the value.
     *
     * @param value encoded to be looked up.
     * @return the enum value representing the value.
     */
    public static Boolean get(final short value)
    {
        switch (value)
        {
            case 0: return F;
            case 1: return T;
            case 255: return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
