package ru.otus.ioc;

import ru.otus.appcontainer.api.AppComponent;

import java.lang.reflect.Method;

public record AnnotatedMethod (
    AppComponent annotations,
    Method method
) {}
