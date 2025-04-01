/* Generated SBE (Simple Binary Encoding) message codec. */
package com.vijay.matching;

import org.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public final class ExecutionEncoder
{
    public static final int BLOCK_LENGTH = 58;
    public static final int TEMPLATE_ID = 4;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "5.2";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ExecutionEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
    private int offset;
    private int limit;

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

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public ExecutionEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public ExecutionEncoder wrapAndApplyHeader(
        final MutableDirectBuffer buffer, final int offset, final MessageHeaderEncoder headerEncoder)
    {
        headerEncoder
            .wrap(buffer, offset)
            .blockLength(BLOCK_LENGTH)
            .templateId(TEMPLATE_ID)
            .schemaId(SCHEMA_ID)
            .version(SCHEMA_VERSION);

        return wrap(buffer, offset + MessageHeaderEncoder.ENCODED_LENGTH);
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

    public ExecutionEncoder execId(final long value)
    {
        buffer.putLong(offset + 0, value, BYTE_ORDER);
        return this;
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


    public ExecutionEncoder symbol(final int index, final byte value)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 8 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String symbolCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public ExecutionEncoder putSymbol(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 8, src, srcOffset, length);

        return this;
    }

    public ExecutionEncoder symbol(final String src)
    {
        final int length = 6;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 8, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 8 + start, (byte)0);
        }

        return this;
    }

    public ExecutionEncoder symbol(final CharSequence src)
    {
        final int length = 6;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 8, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 8 + start, (byte)0);
        }

        return this;
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


    public ExecutionEncoder dealtCcy(final int index, final byte value)
    {
        if (index < 0 || index >= 3)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 14 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }
    public ExecutionEncoder putDealtCcy(final byte value0, final byte value1, final byte value2)
    {
        buffer.putByte(offset + 14, value0);
        buffer.putByte(offset + 15, value1);
        buffer.putByte(offset + 16, value2);

        return this;
    }

    public static String dealtCcyCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public ExecutionEncoder putDealtCcy(final byte[] src, final int srcOffset)
    {
        final int length = 3;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 14, src, srcOffset, length);

        return this;
    }

    public ExecutionEncoder dealtCcy(final String src)
    {
        final int length = 3;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 14, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 14 + start, (byte)0);
        }

        return this;
    }

    public ExecutionEncoder dealtCcy(final CharSequence src)
    {
        final int length = 3;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 14, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 14 + start, (byte)0);
        }

        return this;
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

    public ExecutionEncoder side(final Side value)
    {
        buffer.putByte(offset + 17, value.value());
        return this;
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

    public ExecutionEncoder execQty(final long value)
    {
        buffer.putLong(offset + 18, value, BYTE_ORDER);
        return this;
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


    public ExecutionEncoder valueDate(final int index, final byte value)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 26 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String valueDateCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public ExecutionEncoder putValueDate(final byte[] src, final int srcOffset)
    {
        final int length = 8;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 26, src, srcOffset, length);

        return this;
    }

    public ExecutionEncoder valueDate(final String src)
    {
        final int length = 8;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 26, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 26 + start, (byte)0);
        }

        return this;
    }

    public ExecutionEncoder valueDate(final CharSequence src)
    {
        final int length = 8;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 26, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 26 + start, (byte)0);
        }

        return this;
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


    public ExecutionEncoder user(final int index, final byte value)
    {
        if (index < 0 || index >= 20)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 34 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String userCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public ExecutionEncoder putUser(final byte[] src, final int srcOffset)
    {
        final int length = 20;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 34, src, srcOffset, length);

        return this;
    }

    public ExecutionEncoder user(final String src)
    {
        final int length = 20;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 34, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 34 + start, (byte)0);
        }

        return this;
    }

    public ExecutionEncoder user(final CharSequence src)
    {
        final int length = 20;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 34, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 34 + start, (byte)0);
        }

        return this;
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

    public ExecutionEncoder matchPercentage(final float value)
    {
        buffer.putFloat(offset + 54, value, BYTE_ORDER);
        return this;
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        return appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final ExecutionDecoder decoder = new ExecutionDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
