package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exceptions.IocContainerException;
import ru.otus.ioc.AnnotatedMethod;
import ru.otus.ioc.ComponentConfigurationMetadataReader;
import ru.otus.ioc.ComponentConfigurationReflectionReader;
import ru.otus.ioc.ReflectionHelper;

import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private ComponentConfigurationMetadataReader configurationReader;

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        configurationReader = new ComponentConfigurationReflectionReader(initialConfigClass);

        processConfig(initialConfigClass);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        C appComponent = null;
        for (var component: appComponents) {
            var cass = component.getClass();

            if (componentClass.isAssignableFrom(component.getClass())){
                if (appComponent != null) {
                    throw new IocContainerException("component of type" + componentClass.getName() + " has multiple instances");
                }
                appComponent = (C)component;
            }
        }
        if (appComponent == null) {
            throw new IocContainerException("component of type" + componentClass.getName() + " was not found");
        }

        return appComponent;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C)appComponentsByName.get(componentName);
    }

    private void processConfig(Class<?> configClass) {
        try {
            checkConfigClass(configClass);
            var constructor = configClass.getConstructor();
            var config = constructor.newInstance();
            var annotatedMethods =  configurationReader.getAnnotatedMethods();
            Collections.sort(annotatedMethods,
                    (AnnotatedMethod a1, AnnotatedMethod a2) -> a1.annotations().order()-a2.annotations().order());
            for (var annotatedMethod:
                    annotatedMethods) {
                var component = InstantiateComponent(config, annotatedMethod);
                putComponent(annotatedMethod.annotations().name(), component);
            }
        } catch (Exception e) {
            throw new IocContainerException(e.getMessage());
        }
    }

    private void putComponent(String name, Object component) {
        if (appComponentsByName.containsKey(name)) {
            throw new IocContainerException("component with the name" + name + " already exists");
        }
        appComponentsByName.put(name, component);
        appComponents.add(component);
    }

    private Object InstantiateComponent(Object configClass, AnnotatedMethod annotatedMethod) {
        var argValues = getMethodArgValues(annotatedMethod);
        return ReflectionHelper.callMethod(configClass, annotatedMethod.method(), argValues.toArray());
    }

    private List<Object> getMethodArgValues(AnnotatedMethod annotatedMethod) {
        var argumentObjects = new ArrayList<Object>();
        for (var parameter : annotatedMethod.method().getParameters()) {
            argumentObjects.add(appComponentsByName.get(parameter.getName()));
        }

        return argumentObjects;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
