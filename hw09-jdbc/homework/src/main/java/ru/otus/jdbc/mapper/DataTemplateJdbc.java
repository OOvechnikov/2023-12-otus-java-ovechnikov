package ru.otus.jdbc.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
@RequiredArgsConstructor
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData<T> entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), this::getNextFromResultSet);
    }

    @SneakyThrows
    private T getNextFromResultSet(ResultSet rs) {
        if (!rs.next()) {
            return null;
        }
        T instance = entityClassMetaData.getConstructor().newInstance();
        val instanceFields = instance.getClass().getDeclaredFields();
        for (val instanceField : instanceFields) {
            for (val metaDataField : entityClassMetaData.getAllFields()) {
                if (metaDataField.equals(instanceField)) {
                    instanceField.setAccessible(true);
                    instanceField.set(instance, rs.getObject(instanceField.getName(), instanceField.getType()));
                    instanceField.setAccessible(false);
                }
            }
        }
        return instance;
    }

    @Override
    public List<T> findAll(Connection connection) {
        val result = new ArrayList<T>();
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(),
                rs -> {
                    var instance = getNextFromResultSet(rs);
                    result.add(instance);
                    while (true) {
                        instance = getNextFromResultSet(rs);
                        if (instance == null) {
                            break;
                        }
                        result.add(instance);
                    }
                    return result;
                }
        ).orElseThrow();
    }

    @SneakyThrows
    private Object extractIdField(ResultSet rs) {
        return rs.getObject(entityClassMetaData.getIdField().getName());
    }

    @Override
    @SneakyThrows
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getFieldValues(client, false));
    }

    @Override
    public void update(Connection connection, T client) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getFieldValues(client, true));
    }

    @SneakyThrows
    private List<Object> getFieldValues(T client, boolean includeId) {
        val params = new ArrayList<>();
        Object idValue = null;
        for (val field : client.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.equals(entityClassMetaData.getIdField())) {
                idValue = field.get(client);
                continue;
            }
            params.add(field.get(client));
        }
        if (includeId) {
            params.add(idValue);
        }
        return params;
    }

}
