package ru.otus;

import java.text.MessageFormat;

public class Runner {
    public static void run(Class<?> testClass, TestContainer test) {
        try {
            var testInstance = ReflectionHelper.instantiate(testClass);
            invokeTest(testInstance, test);
        } catch (Exception e) {
            System.out.println(MessageFormat.format("Failed to instantiate {0}",
                    e.toString()));
        }
    }

    private static void invokeTest(Object testInstance, TestContainer test) {
        try {
            for (var before : test.before()) {
                ReflectionHelper.callMethod(testInstance, before);
            }

            ReflectionHelper.callMethod(testInstance, test.test());
            System.out.println(MessageFormat.format("{0} passed",
                    test.test().getName().toString()));
        } catch (Exception ex) {
            System.out.println(MessageFormat.format("{0} failed: {1}",
                    test.test().getName(),
                    ex.toString()));

        } finally {
            for (var after : test.after()) {
                ReflectionHelper.tryCallMethod(testInstance, after);
            }
        }
    }
}