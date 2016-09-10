package eu.toolchain.scribe;

import java.util.Optional;
import java.util.stream.Stream;

public interface ClassMapping extends Mapping {
  default Optional<String> typeName() {
    return Optional.empty();
  }

  @SuppressWarnings("unchecked")
  @Override
  default <Target, Source> Stream<Encoder<Target, Source>> newEncoder(
      final EntityResolver resolver, final EncoderFactory<Target> factory, final Flags flags
  ) {
    return Stream.of((Encoder<Target, Source>) newEntityTypeEncoder(resolver, factory));
  }

  @SuppressWarnings("unchecked")
  @Override
  default <Target, Source> Stream<StreamEncoder<Target, Source>> newStreamEncoder(
      final EntityResolver resolver, final StreamEncoderFactory<Target> factory, final Flags flags
  ) {
    return Stream.of((StreamEncoder<Target, Source>) newEntityTypeStreamEncoder(resolver, factory));
  }

  @SuppressWarnings("unchecked")
  @Override
  default <Target, Source> Stream<Decoder<Target, Source>> newDecoder(
      EntityResolver resolver, DecoderFactory<Target> factory, final Flags flags
  ) {
    return Stream.of((Decoder<Target, Source>) newEntityTypeDecoder(resolver, factory));
  }

  <Target> EntityEncoder<Target, Object> newEntityTypeEncoder(
      EntityResolver resolver, EncoderFactory<Target> factory
  );

  <Target> EntityStreamEncoder<Target, Object> newEntityTypeStreamEncoder(
      EntityResolver resolver, StreamEncoderFactory<Target> factory
  );

  <Target> EntityDecoder<Target, Object> newEntityTypeDecoder(
      EntityResolver resolver, DecoderFactory<Target> factory
  );
}
