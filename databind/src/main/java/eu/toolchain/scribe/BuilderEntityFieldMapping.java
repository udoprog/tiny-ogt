package eu.toolchain.scribe;

import eu.toolchain.scribe.reflection.JavaType;
import lombok.Data;

import java.util.stream.Stream;

@Data
class BuilderEntityFieldMapping<Source> implements EntityFieldMapping<Source> {
  private final String name;
  private final Mapping<Source> mapping;
  private final FieldReader reader;
  private final JavaType.Method setter;
  private final Flags flags;

  @Override
  public <Target, EntityTarget> Stream<BuilderEntityFieldEncoder<Target, Source>>
  newEntityFieldEncoder(
      final EntityResolver resolver, final EncoderFactory<Target, EntityTarget> factory
  ) {
    return mapping
        .newEncoder(resolver, factory, flags)
        .map(parent -> new BuilderEntityFieldEncoder<>(name, reader, mapping, setter, flags,
            parent));
  }

  @Override
  public <Target> Stream<BuilderEntityFieldStreamEncoder<Target, Source>>
  newEntityFieldStreamEncoder(
      final EntityResolver resolver, final StreamEncoderFactory<Target> factory
  ) {
    return mapping
        .newStreamEncoder(resolver, factory, flags)
        .map(
            parent -> new BuilderEntityFieldStreamEncoder<>(name, reader, mapping, setter, parent));
  }

  @Override
  public <Target, EntityTarget> Stream<BuilderEntityFieldDecoder<Target, Source>>
  newEntityFieldDecoder(
      final EntityResolver resolver, final DecoderFactory<Target, EntityTarget> factory
  ) {
    return mapping
        .newDecoder(resolver, factory, flags)
        .map(parent -> new BuilderEntityFieldDecoder<>(name, reader, mapping, setter, flags,
            parent));
  }
}
