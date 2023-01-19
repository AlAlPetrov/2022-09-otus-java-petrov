package ru.otus.ioc;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.exceptions.ReflectionReaderException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComponentConfigurationReflectionReader<T> implements ComponentConfigurationMetadataReader {
    private final Class<T> classType;

    private List<AnnotatedMethod> annotatedMethods;

    public ComponentConfigurationReflectionReader(Class<T> classType) {
        this.classType = classType;
        var methods = readConfigurationMethods(this.classType);
        annotatedMethods = applyAnnotation(methods);
    }

    @Override
    public List<AnnotatedMethod> getAnnotatedMethods() {
        return annotatedMethods;
    }

    private List<AnnotatedMethod> applyAnnotation(List<Method> methods) {
        var annotatedMethods = new ArrayList<AnnotatedMethod>();
        for (var method: methods) {
            var appComponent = method.getAnnotation(AppComponent.class);
            if (appComponent != null) {
                annotatedMethods.add(new AnnotatedMethod(appComponent, method));
            }
        }

        return annotatedMethods;
    }

    private List<Method> readConfigurationMethods(Class<?> classType) {
        var methods = Arrays.asList(classType.getDeclaredMethods());
        if (methods.isEmpty()) {
            throw new ReflectionReaderException(classType, "class should have at least one method");
        }

        return methods;
    }
}
