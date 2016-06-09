package eu.toolchain.ogt;

import java.io.IOException;
import java.util.Optional;

public enum PrimitiveType {
    // @formatter:off
    SHORT(TypeDecoder::decodeShort, (e, v) -> e.encodeShort((short) v)),
    INTEGER(TypeDecoder::decodeInteger, (e, v) -> e.encodeInteger((int) v)),
    LONG(TypeDecoder::decodeLong, (e, v) -> e.encodeLong((long) v)),
    FLOAT(TypeDecoder::decodeFloat, (e, v) -> e.encodeFloat((float) v)),
    DOUBLE(TypeDecoder::decodeDouble, (e, v) -> e.encodeDouble((double) v)),
    BOOLEAN(TypeDecoder::decodeBoolean, (e, v) -> e.encodeBoolean((boolean) v)),
    BYTE(TypeDecoder::decodeByte, (e, v) -> e.encodeByte((byte) v)),
    CHAR(TypeDecoder::decodeCharacter, (e, v) -> e.encodeCharacter((char) v));
    // @formatter:on

    private final DecodingFunction decoding;
    private final EncodingFunction encoding;

    private PrimitiveType(final DecodingFunction accessor, final EncodingFunction setter) {
        this.decoding = accessor;
        this.encoding = setter;
    }

    public static Optional<PrimitiveType> detect(final JavaType type) {
        final Class<?> raw = type.getRawClass();

        if (raw == Boolean.class || raw == boolean.class) {
            return Optional.of(BOOLEAN);
        }

        if (raw == Byte.class || raw == byte.class) {
            return Optional.of(BYTE);
        }

        if (raw == Character.class || raw == char.class) {
            return Optional.of(CHAR);
        }

        if (raw == Short.class || raw == short.class) {
            return Optional.of(SHORT);
        }

        if (raw == Integer.class || raw == int.class) {
            return Optional.of(INTEGER);
        }

        if (raw == Long.class || raw == long.class) {
            return Optional.of(LONG);
        }

        if (raw == Float.class || raw == float.class) {
            return Optional.of(FLOAT);
        }

        if (raw == Double.class || raw == double.class) {
            return Optional.of(DOUBLE);
        }

        return Optional.empty();
    }

    public <T> Object get(TypeDecoder<T> a, T instance) throws IOException {
        return decoding.decode(a, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T set(TypeEncoder<T> encoder, Object value) throws IOException {
        return (T) this.encoding.encode(encoder, value);
    }

    public static interface DecodingFunction {
        public <T> Object decode(TypeDecoder<T> decoder, T instance) throws IOException;
    }

    public static interface EncodingFunction {
        public Object encode(TypeEncoder<?> encoder, Object value) throws IOException;
    }
}
