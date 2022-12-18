package ru.otus;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLGenerator implements EntitySQLMetaData {
    private final EntityClassMetaData entityClassMetaDataClient;

    private static String mapToString(List<Field> list, boolean ReplaceWithQuestionMark)
    {
        return list.stream()
                .map(field -> ReplaceWithQuestionMark ? "?": field.getName())
                .collect(Collectors.joining(","));
    }

    private static String mapToUpdateString(List<Field> list)
    {
        return list.stream()
                .map(item -> item.getName() + " = ?")
                .collect(Collectors.joining(","));
    }

    public EntitySQLGenerator(EntityClassMetaData entityClassMetaDataClient) {
        this.entityClassMetaDataClient = entityClassMetaDataClient;
    }

    @Override
    public String getSelectAllSql() {
        return MessageFormat.format("select {0} from {1}",
                        mapToString(entityClassMetaDataClient.getAllFields(), false),
                        entityClassMetaDataClient.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return MessageFormat.format("{0} where {1} = ?",
                getSelectAllSql(),
                entityClassMetaDataClient.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        return  MessageFormat.format("insert into {0}({1}) values ({2})",
                        entityClassMetaDataClient.getName(),
                        mapToString(entityClassMetaDataClient.getFieldsWithoutId(), false),
                        mapToString(entityClassMetaDataClient.getFieldsWithoutId(), true));
    }

    @Override
    public String getUpdateSql() {
        return MessageFormat.format("update {0} set {1} where {2} = ?",
                        entityClassMetaDataClient.getName(),
                        mapToUpdateString(entityClassMetaDataClient.getFieldsWithoutId()),
                        entityClassMetaDataClient.getIdField().getName());
    }
}
