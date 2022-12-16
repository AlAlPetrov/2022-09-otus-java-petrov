package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private final DbExecutor dbExecutor;
    private final EntityClassMetaData entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor,
                            EntityClassMetaData entityClassMetaData,
                            EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    private Object[] getArgumentTypes(ResultSet resultSet) throws SQLException {
        List<Field> Fields = entityClassMetaData.getAllFields();
        return Fields.stream()
                .map(field -> {
                    try {
                        return resultSet.getObject(field.getName(),
                                field.getType());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList())
                .toArray();
    }

    private List<Object> getValues(List<Field> fields, T entity) {
        return fields.stream()
                .map(field -> ReflectionHelper.getFieldValue(entity, field.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return (Optional<T>) dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    var argumentTypes = getArgumentTypes(rs);
                    var entityConstructor = entityClassMetaData.getConstructor();
                    return  entityConstructor.newInstance(argumentTypes);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            try {
                var entitiesList = new ArrayList<T>();
                var entityConstructor = entityClassMetaData.getConstructor();
                while (rs.next()) {
                    var argumentTypes = getArgumentTypes(rs);
                    entitiesList.add((T)entityConstructor.newInstance(argumentTypes));
                }
                return entitiesList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                    getValues(entityClassMetaData.getFieldsWithoutId(), entity));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            var values = getValues(entityClassMetaData.getFieldsWithoutId(), entity);
            values.addAll(getValues(List.of(entityClassMetaData.getIdField()), entity));

            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                    values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
