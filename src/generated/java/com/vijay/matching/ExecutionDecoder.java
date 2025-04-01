/* Generated SBE (Simple Binary Encoding) message codec. */
package com.vijay.matching;

import org.agrona.DirectBuffer;

@SuppressWarnings("all")
public final class ExecutionDecoder
{
    public static final int BLOCK_LENGTH = 58;
    public static final int TEMPLATE_ID = 4;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "5.2";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ExecutionDecoder parentMessage = this;
    private DirectBuffer buffer;
    private int offset;
    private int limit;
    int actingBlockLength;
    int actingVersion;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public ExecutionDecoder wrap(
        final DirectBuffer buffer,
        final int offset,
        final int actingBlockLength,
        final int actingVersion)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public ExecutionDecoder wrapAndApplyHeader(
        final DirectBuffer buffer,
        final int offset,
        final MessageHeaderDecoder headerDecoder)
    {
        headerDecoder.wrap(buffer, offset);

        final int templateId = headerDecoder.templateId();
        if (TEMPLATE_ID != templateId)
        {
            throw new IllegalStateException("Invalid TEMPLATE_ID: " + templateId);
        }

        return wrap(
            buffer,
            offset + MessageHeaderDecoder.ENCODED_LENGTH,
            headerDecoder.blockLength(),
            headerDecoder.version());
    }

    public ExecutionDecoder sbeRewind()
    {
        return wrap(buffer, offset, actingBlockLength, actingVersion);
    }

    public int sbeDecodedLength()
    {
        final int currentLimit = limit();
        sbeSkip();
        final int decodedLength = encodedLength();
        limit(currentLimit);

        return decodedLength;
    }

    public int actingVersion()
    {
        return actingVersion;
    }

    public int encodedLength()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        this.limit = limit;
    }

    public static int execIdId()
    {
        return 1;
    }

    public static int execIdSinceVersion()
    {
        return 0;
    }

    public static int execIdEncodingOffset()
    {
        return 0;
    }

    public static int execIdEncodingLength()
    {
        return 8;
    }

    public static String execIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long execIdNullValue()
    {
        return -9223372036854775808L;
    }

    public static long execIdMinValue()
    {
        return -9223372036854775807L;
    }

    public static long execIdMaxValue()
    {
        return 9223372036854775807L;
    }

    public long execId()
    {
        return buffer.getLong(offset + 0, BYTE_ORDER);
    }


    public static int symbolId()
    {
        return 2;
    }

    public static int symbolSinceVersion()
    {
        return 0;
    }

    public static int symbolEncodingOffset()
    {
        return 8;
    }

    public static int symbolEncodingLength()
    {
        return 6;
    }

    public static String symbolMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte symbolNullValue()
    {
        return (byte)0;
    }

    public static byte symbolMinValue()
    {
        return (byte)32;
    }

    public static byte symbolMaxValue()
    {
        return (byte)126;
    }

    public static int symbolLength()
    {
        return 6;
    }


    public byte symbol(final int index)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 8 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String symbolCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getSymbol(final byte[] dst, final int dstOffset)
    {
        final int length = 6;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 8, dst, dstOffset, length);

        return length;
    }

    public String symbol()
    {
        final byte[] dst = new byte[6];
        buffer.getBytes(offset + 8, dst, 0, 6);

        int end = 0;
        for (; end < 6 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getSymbol(final Appendable value)
    {
        for (int i = 0; i < 6; ++i)
        {
            final int c = buffer.getByte(offset + 8 + i) & 0xFF;
            if (c == 0)
            {
                return i;
            }

            try
            {
                value.append(c > 127 ? '?' : (char)c);
            }
            catch (final java.io.IOException ex)
            {
                throw new java.io.UncheckedIOException(ex);
            }
        }

        return 6;
    }


    public static int dealtCcyId()
    {
        return 3;
    }

    public static int dealtCcySinceVersion()
    {
        return 0;
    }

    public static int dealtCcyEncodingOffset()
    {
        return 14;
    }

    public static int dealtCcyEncodingLength()
    {
        return 3;
    }

    public static String dealtCcyMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte dealtCcyNullValue()
    {
        return (byte)0;
    }

    public static byte dealtCcyMinValue()
    {
        return (byte)32;
    }

    public static byte dealtCcyMaxValue()
    {
        return (byte)126;
    }

    public static int dealtCcyLength()
    {
        return 3;
    }


    public byte dealtCcy(final int index)
    {
        if (index < 0 || index >= 3)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 14 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String dealtCcyCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getDealtCcy(final byte[] dst, final int dstOffset)
    {
        final int length = 3;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 14, dst, dstOffset, length);

        return length;
    }

    public String dealtCcy()
    {
        final byte[] dst = new byte[3];
        buffer.getBytes(offset + 14, dst, 0, 3);

        int end = 0;
        for (; end < 3 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getDealtCcy(final Appendable value)
    {
        for (int i = 0; i < 3; ++i)
        {
            final int c = buffer.getByte(offset + 14 + i) & 0xFF;
            if (c == 0)
            {
                return i;
            }

            try
            {
                value.append(c > 127 ? '?' : (char)c);
            }
            catch (final java.io.IOException ex)
            {
                throw new java.io.UncheckedIOException(ex);
            }
        }

        return 3;
    }


    public static int sideId()
    {
        return 4;
    }

    public static int sideSinceVersion()
    {
        return 0;
    }

    public static int sideEncodingOffset()
    {
        return 17;
    }

    public static int sideEncodingLength()
    {
        return 1;
    }

    public static String sideMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public byte sideRaw()
    {
        return buffer.getByte(offset + 17);
    }

    public Side side()
    {
        return Side.get(buffer.getByte(offset + 17));
    }


    public static int execQtyId()
    {
        return 5;
    }

    public static int execQtySinceVersion()
    {
        return 0;
    }

    public static int execQtyEncodingOffset()
    {
        return 18;
    }

    public static int execQtyEncodingLength()
    {
        return 8;
    }

    public static String execQtyMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long execQtyNullValue()
    {
        return -9223372036854775808L;
    }

    public static long execQtyMinValue()
    {
        return -9223372036854775807L;
    }

    public static long execQtyMaxValue()
    {
        return 9223372036854775807L;
    }

    public long execQty()
    {
        return buffer.getLong(offset + 18, BYTE_ORDER);
    }


    public static int valueDateId()
    {
        return 6;
    }

    public static int valueDateSinceVersion()
    {
        return 0;
    }

    public static int valueDateEncodingOffset()
    {
        return 26;
    }

    public static int valueDateEncodingLength()
    {
        return 8;
    }

    public static String valueDateMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte valueDateNullValue()
    {
        return (byte)0;
    }

    public static byte valueDateMinValue()
    {
        return (byte)32;
    }

    public static byte valueDateMaxValue()
    {
        return (byte)126;
    }

    public static int valueDateLength()
    {
        return 8;
    }


    public byte valueDate(final int index)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 26 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String valueDateCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getValueDate(final byte[] dst, final int dstOffset)
    {
        final int length = 8;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 26, dst, dstOffset, length);

        return length;
    }

    public String valueDate()
    {
        final byte[] dst = new byte[8];
        buffer.getBytes(offset + 26, dst, 0, 8);

        int end = 0;
        for (; end < 8 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getValueDate(final Appendable value)
    {
        for (int i = 0; i < 8; ++i)
        {
            final int c = buffer.getByte(offset + 26 + i) & 0xFF;
            if (c == 0)
            {
                return i;
            }

            try
            {
                value.append(c > 127 ? '?' : (char)c);
            }
            catch (final java.io.IOException ex)
            {
                throw new java.io.UncheckedIOException(ex);
            }
        }

        return 8;
    }


    public static int userId()
    {
        return 7;
    }

    public static int userSinceVersion()
    {
        return 0;
    }

    public static int userEncodingOffset()
    {
        return 34;
    }

    public static int userEncodingLength()
    {
        return 20;
    }

    public static String userMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte userNullValue()
    {
        return (byte)0;
    }

    public static byte userMinValue()
    {
        return (byte)32;
    }

    public static byte userMaxValue()
    {
        return (byte)126;
    }

    public static int userLength()
    {
        return 20;
    }


    public byte user(final int index)
    {
        if (index < 0 || index >= 20)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 34 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String userCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getUser(final byte[] dst, final int dstOffset)
    {
        final int length = 20;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 34, dst, dstOffset, length);

        return length;
    }

    public String user()
    {
        final byte[] dst = new byte[20];
        buffer.getBytes(offset + 34, dst, 0, 20);

        int end = 0;
        for (; end < 20 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getUser(final Appendable value)
    {
        for (int i = 0; i < 20; ++i)
        {
            final int c = buffer.getByte(offset + 34 + i) & 0xFF;
            if (c == 0)
            {
                return i;
            }

            try
            {
                value.append(c > 127 ? '?' : (char)c);
            }
            catch (final java.io.IOException ex)
            {
                throw new java.io.UncheckedIOException(ex);
            }
        }

        return 20;
    }


    public static int matchPercentageId()
    {
        return 8;
    }

    public static int matchPercentageSinceVersion()
    {
        return 0;
    }

    public static int matchPercentageEncodingOffset()
    {
        return 54;
    }

    public static int matchPercentageEncodingLength()
    {
        return 4;
    }

    public static String matchPercentageMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static float matchPercentageNullValue()
    {
        return Float.NaN;
    }

    public static float matchPercentageMinValue()
    {
        return 1.401298464324817E-45f;
    }

    public static float matchPercentageMaxValue()
    {
        return 3.4028234663852886E38f;
    }

    public float matchPercentage()
    {
        return buffer.getFloat(offset + 54, BYTE_ORDER);
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final ExecutionDecoder decoder = new ExecutionDecoder();
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        return decoder.appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final int originalLimit = limit();
        limit(offset + actingBlockLength);
        builder.append("[Execution](sbeTemplateId=");
        builder.append(TEMPLATE_ID);
        builder.append("|sbeSchemaId=");
        builder.append(SCHEMA_ID);
        builder.append("|sbeSchemaVersion=");
        if (parentMessage.actingVersion != SCHEMA_VERSION)
        {
            builder.append(parentMessage.actingVersion);
            builder.append('/');
        }
        builder.append(SCHEMA_VERSION);
        builder.append("|sbeBlockLength=");
        if (actingBlockLength != BLOCK_LENGTH)
        {
            builder.append(actingBlockLength);
            builder.append('/');
        }
        builder.append(BLOCK_LENGTH);
        builder.append("):");
        builder.append("execId=");
        builder.append(this.execId());
        builder.append('|');
        builder.append("symbol=");
        for (int i = 0; i < symbolLength() && this.symbol(i) > 0; i++)
        {
            builder.append((char)this.symbol(i));
        }
        builder.append('|');
        builder.append("dealtCcy=");
        for (int i = 0; i < dealtCcyLength() && this.dealtCcy(i) > 0; i++)
        {
            builder.append((char)this.dealtCcy(i));
        }
        builder.append('|');
        builder.append("side=");
        builder.append(this.side());
        builder.append('|');
        builder.append("execQty=");
        builder.append(this.execQty());
        builder.append('|');
        builder.append("valueDate=");
        for (int i = 0; i < valueDateLength() && this.valueDate(i) > 0; i++)
        {
            builder.append((char)this.valueDate(i));
        }
        builder.append('|');
        builder.append("user=");
        for (int i = 0; i < userLength() && this.user(i) > 0; i++)
        {
            builder.append((char)this.user(i));
        }
        builder.append('|');
        builder.append("matchPercentage=");
        builder.append(this.matchPercentage());

        limit(originalLimit);

        return builder;
    }
    
    public ExecutionDecoder sbeSkip()
    {
        sbeRewind();

        return this;
    }
}
