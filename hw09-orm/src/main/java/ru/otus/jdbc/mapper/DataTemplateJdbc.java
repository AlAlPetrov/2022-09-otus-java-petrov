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

    private final DbExecutor _dbExecutor;
    private final EntityClassMetaData _entityClassMetaData;
    private final EntitySQLMetaData _entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor,
                            EntityClassMetaData entityClassMetaData,
                            EntitySQLMetaData entitySQLMetaData) {
        _dbExecutor = dbExecutor;
        _entityClassMetaData = entityClassMetaData;
        _entitySQLMetaData = entitySQLMetaData;
    }

    private Object[] getArgs(ResultSet resultSet) throws SQLException {
        List<Field> Fields = _entityClassMetaData.getAllFields();
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

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return (Optional<T>) _dbExecutor.executeSelect(connection, _entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    var args = getArgs(rs);
                    var ctor = _entityClassMetaData.getConstructor();
                    return  ctor.newInstance(args);
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
        return _dbExecutor.executeSelect(connection, _entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            try {
                var entitiesList = new ArrayList<T>();
                var ctor = _entityClassMetaData.getConstructor();
                while (rs.next()) {
                    var args = getArgs(rs);
                    entitiesList.add((T)ctor.newInstance(args));
                }
                return entitiesList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    private List<Object> getValues(List<Field> fields, T entity) {
        return fields.stream()
                        .map(field -> ReflectionHelper.getFieldValue(entity, field.getName()))
                        .collect(Collectors.toList());
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            return _dbExecutor.executeStatement(connection, _entitySQLMetaData.getInsertSql(),
                    getValues(_entityClassMetaData.getFieldsWithoutId(), entity));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            var values = getValues(_entityClassMetaData.getFieldsWithoutId(), entity);
            values.addAll(getValues(List.of(_entityClassMetaData.getIdField()), entity));
            _dbExecutor.executeStatement(connection, _entitySQLMetaData.getUpdateSql(),
                    values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
