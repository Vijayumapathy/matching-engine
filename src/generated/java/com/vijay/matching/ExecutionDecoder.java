/* Generated SBE (Simple Binary Encoding) message codec. */
package com.vijay.matching;

import org.agrona.DirectBuffer;

@SuppressWarnings("all")
public final class ExecutionDecoder
{
    public static final int BLOCK_LENGTH = 29;
    public static final int TEMPLATE_ID = 3;
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

    public long id()
    {
        return buffer.getLong(offset + 8, BYTE_ORDER);
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

    public long execQty()
    {
        return buffer.getLong(offset + 16, BYTE_ORDER);
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

    public float execPrice()
    {
        return buffer.getFloat(offset + 24, BYTE_ORDER);
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

    public short ackRaw()
    {
        return ((short)(buffer.getByte(offset + 28) & 0xFF));
    }

    public Boolean ack()
    {
        return Boolean.get(((short)(buffer.getByte(offset + 28) & 0xFF)));
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
        builder.append("id=");
        builder.append(this.id());
        builder.append('|');
        builder.append("execQty=");
        builder.append(this.execQty());
        builder.append('|');
        builder.append("execPrice=");
        builder.append(this.execPrice());
        builder.append('|');
        builder.append("ack=");
        builder.append(this.ack());

        limit(originalLimit);

        return builder;
    }
    
    public ExecutionDecoder sbeSkip()
    {
        sbeRewind();

        return this;
    }
}
