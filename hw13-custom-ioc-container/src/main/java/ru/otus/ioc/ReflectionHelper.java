package ru.otus.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class ReflectionHelper {
    private ReflectionHelper() {
    }

    public static Object getFieldValue(Object object, String name) {
        try {
            var field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void setFieldValue(Object object, String name, Object value) {
        try {
            var field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object callMethod(Object object, String name, Object... args) {
        try {
            var method = object.getClass().getDeclaredMethod(name, toClasses(args));
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object callMethod(Object object,
                                    Method method,
                                    Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public static boolean tryCallMethod(Object object,
                                        Method method,
                                        Object... args) {
        try {
            method.setAccessible(true);
            method.invoke(object, args);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isAnnotatedWith(Method method, Class desiredAnnotation) {
        var annotations = method.getDeclaredAnnotations();
        for (var annotation : annotations) {
            if (annotation.annotationType().equals(desiredAnnotation)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnnotatedWith(Field field, Class desiredAnnotation) {
        var annotations = field.getDeclaredAnnotations();
        for (var annotation : annotations) {
            if (annotation.annotationType().equals(desiredAnnotation)) {
                return true;
            }
        }
        return false;
    }

    public static Optional<Annotation> getAnnotation(Method method, Class desiredAnnotation) {
        var annotations = method.getDeclaredAnnotations();
        for (var annotation : annotations) {
            if (annotation.annotationType().equals(desiredAnnotation)) {
                return Optional.of(annotation);
            }
        }
        return Optional.empty();
    }

    public static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
