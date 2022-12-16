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
    private final Class<T> _classType;
    private List<Field> _fields;
    private Field _idField;

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
                    throw new ReflectionReaderException(_classType, "contains more than one field with @Id annotation");
                }
                idField = field;
            }
        }
        if (idField == null) {
            throw new ReflectionReaderException(_classType, "does not have a field annotated with @Id");
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
        _classType = classType;
        _fields = readClassFields(_classType);
        _idField = findIdField(_fields);
    }

    @Override
    public String getName() {
        return _classType.getSimpleName();
    }

    @Override
    public Constructor getConstructor() {
        try {
            var argTypes = getConstructorArgs();
            return _classType.getDeclaredConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return _idField;
    }

    @Override
    public List<Field> getAllFields() {
        return _fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return _fields.stream()
                .filter(field -> field != _idField)
                .collect(Collectors.toList());
    }
}
