package ru.otus.jdbc.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData<T> {

    @Getter
    private final EntityClassMetaData<T> entityClassMetaData;

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT * FROM " + entityClassMetaData.getName() + " WHERE " + entityClassMetaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        return "INSERT INTO " + entityClassMetaData.getName() +
                "(" + entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(",")) + ") " +
                "VALUES (" + entityClassMetaData.getFieldsWithoutId().stream().map(f -> "?").collect(Collectors.joining(",")) + ")";
    }

    @Override
    public String getUpdateSql() {
        return "UPDATE " + entityClassMetaData.getName() + " SET " +
                "(" + entityClassMetaData.getFieldsWithoutId().stream().map(f -> f.getName() + " = ?").collect(Collectors.joining(", ")) + ") " +
                "WHERE " + entityClassMetaData.getIdField().getName() + " = ?";
    }

}
