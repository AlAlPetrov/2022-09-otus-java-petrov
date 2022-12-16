package ru.otus;

import ru.otus.exceptions.ReflectionReaderException;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassMetaDataReflectionReader<T> implements EntityClassMetaData {
    private final Class<T> classType;
    private List<Field> fields;
    private Field idField;

    private Class<?>[] getConstructorArgs() {
        var fields = getAllFields();
        return fields.stream()
                    .map(field -> field.getType())
                    .collect(Collectors.toList())
                    .toArray(new Class<?>[fields.size()]);
    }

    private Field findIdField(List<Field> fields) {
        Field idField = null;

        for (var field: fields) {
            if (ReflectionHelper.isAnnotatedWith(field, ru.otus.annotations.Id.class)) {
                if (idField != null) {
                    throw new ReflectionReaderException(classType, "contains more than one field with @Id annotation");
                }
                idField = field;
            }
        }
        if (idField == null) {
            throw new ReflectionReaderException(classType, "does not have a field annotated with @Id");
        }

        return idField;
    }

    private List<Field> readClassFields(Class<?> classType) {
        var fields = Arrays.asList(classType.getDeclaredFields());
        if (fields.isEmpty()) {
            throw new ReflectionReaderException(classType, "class should have at least one field");
        }

        return fields;
    }

    public ClassMetaDataReflectionReader(Class<T> classType) {
        this.classType = classType;
        fields = readClassFields(this.classType);
        idField = findIdField(fields);
    }

    @Override
    public String getName() {
        return classType.getSimpleName();
    }

    @Override
    public Constructor getConstructor() {
        try {
            var argTypes = getConstructorArgs();
            return classType.getDeclaredConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fields.stream()
                .filter(field -> field != idField)
                .collect(Collectors.toList());
    }
}
