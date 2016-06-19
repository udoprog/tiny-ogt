package eu.toolchain.ogt.entitymapper;

import eu.toolchain.ogt.EntityResolver;
import eu.toolchain.ogt.JavaType;

import java.util.Optional;

public interface TypeNameDetector {
    Optional<String> detect(final EntityResolver resolver, final JavaType type);
}