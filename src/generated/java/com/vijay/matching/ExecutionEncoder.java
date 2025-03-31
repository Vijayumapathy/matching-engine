/* Generated SBE (Simple Binary Encoding) message codec. */
package com.vijay.matching;

import org.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public final class ExecutionEncoder
{
    public static final int BLOCK_LENGTH = 29;
    public static final int TEMPLATE_ID = 3;
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


    public static int idId()
    {
        return 2;
    }

    public static int idSinceVersion()
    {
        return 0;
    }

    public static int idEncodingOffset()
    {
        return 8;
    }

    public static int idEncodingLength()
    {
        return 8;
    }

    public static String idMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long idNullValue()
    {
        return -9223372036854775808L;
    }

    public static long idMinValue()
    {
        return -9223372036854775807L;
    }

    public static long idMaxValue()
    {
        return 9223372036854775807L;
    }

    public ExecutionEncoder id(final long value)
    {
        buffer.putLong(offset + 8, value, BYTE_ORDER);
        return this;
    }


    public static int execQtyId()
    {
        return 3;
    }

    public static int execQtySinceVersion()
    {
        return 0;
    }

    public static int execQtyEncodingOffset()
    {
        return 16;
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
        buffer.putLong(offset + 16, value, BYTE_ORDER);
        return this;
    }


    public static int execPriceId()
    {
        return 4;
    }

    public static int execPriceSinceVersion()
    {
        return 0;
    }

    public static int execPriceEncodingOffset()
    {
        return 24;
    }

    public static int execPriceEncodingLength()
    {
        return 4;
    }

    public static String execPriceMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static float execPriceNullValue()
    {
        return Float.NaN;
    }

    public static float execPriceMinValue()
    {
        return 1.401298464324817E-45f;
    }

    public static float execPriceMaxValue()
    {
        return 3.4028234663852886E38f;
    }

    public ExecutionEncoder execPrice(final float value)
    {
        buffer.putFloat(offset + 24, value, BYTE_ORDER);
        return this;
    }


    public static int ackId()
    {
        return 5;
    }

    public static int ackSinceVersion()
    {
        return 0;
    }

    public static int ackEncodingOffset()
    {
        return 28;
    }

    public static int ackEncodingLength()
    {
        return 1;
    }

    public static String ackMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public ExecutionEncoder ack(final Boolean value)
    {
        buffer.putByte(offset + 28, (byte)value.value());
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
