package eu.toolchain.scribe;

import eu.toolchain.scribe.detector.DecodeValueDetector;
import eu.toolchain.scribe.detector.Match;
import eu.toolchain.scribe.detector.MatchPriority;
import eu.toolchain.scribe.reflection.JavaType;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class StaticMethodEntityDecodeValue implements DecodeValue {
  private final JavaType sourceType;
  private final Mapping targetMapping;
  private final JavaType.Method method;

  @SuppressWarnings("unchecked")
  @Override
  public <Target, Source> Optional<Decoder<Target, Source>> newDecoder(
      final EntityResolver resolver, final DecoderFactory<Target> factory
  ) {
    return targetMapping
        .newDecoder(resolver, Flags.empty(), factory)
        .map(parent -> (Decoder<Target, Source>) (path, instance) -> {
          final Object value = parent.decode(path, instance);

          try {
            return Decoded.of((Source) method.invoke(null, value));
          } catch (Exception e) {
            throw path.error("failed to get value", e);
          }
        });
  }

  public static DecodeValueDetector forAnnotation(
      final Class<? extends Annotation> annotation
  ) {
    return (resolver, sourceType, targetType) -> sourceType
        .findByAnnotation(JavaType::getMethods, annotation)
        .filter(m -> m.isPublic() && m.isStatic())
        .filter(m -> m
            .getParameters()
            .stream()
            .map(JavaType.Parameter::getParameterType)
            .collect(Collectors.toList())
            .equals(Collections.singletonList(targetType)))
        .flatMap(m -> {
          final Mapping targetMapping = resolver.mapping(targetType);
          return Stream.of(new StaticMethodEntityDecodeValue(sourceType, targetMapping, m));
        })
        .map(Match.withPriority(MatchPriority.HIGH));
  }
}