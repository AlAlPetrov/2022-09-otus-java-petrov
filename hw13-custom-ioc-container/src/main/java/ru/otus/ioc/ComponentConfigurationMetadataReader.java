package ru.otus.ioc;

import java.util.List;

public interface ComponentConfigurationMetadataReader {
    List<AnnotatedMethod> getAnnotatedMethods();
}
