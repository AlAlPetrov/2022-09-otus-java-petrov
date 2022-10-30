package ru.otus;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestClassParser {

    private static boolean isAnnotatedWith(Method method, Class desiredAnnotation) {
        var annotations = method.getDeclaredAnnotations();
        for (var annotation : annotations) {
            if (annotation.annotationType().equals(desiredAnnotation)) {
                return true;
            }
        }
        return false;
    }

    private static ArrayList<TestContainer> toTestContainers(ArrayList<Method> before, ArrayList<Method> after, ArrayList<Method> tests) {
        var testContainers = new ArrayList<TestContainer>();
        for (var test: tests) {
            testContainers.add(new TestContainer(before,
                    after,
                    test));
        }
        return testContainers;
    }

    public static ArrayList<TestContainer> extractTestsFromClass(Method[] methods) {
        var before = new ArrayList<Method>();
        var after = new ArrayList<Method>();
        var test = new ArrayList<Method>();
        for (var method : methods) {
            if (isAnnotatedWith(method, ru.otus.annotations.Test.class)) {
                test.add(method);
            } else if (isAnnotatedWith(method, ru.otus.annotations.Before.class)) {
                before.add(method);
            } else if (isAnnotatedWith(method, ru.otus.annotations.After.class)) {
                after.add(method);
            }
        };

        return toTestContainers(before, after, test);
    }
}
