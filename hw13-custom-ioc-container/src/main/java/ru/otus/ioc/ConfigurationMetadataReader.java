package ru.otus.ioc;

import java.util.List;

public interface ConfigurationMetadataReader {
    List<AnnotatedMethod> getAnnotatedMethods();
}
