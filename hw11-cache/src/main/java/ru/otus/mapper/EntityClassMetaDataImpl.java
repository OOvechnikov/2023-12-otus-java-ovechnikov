package ru.otus.mapper;

import lombok.SneakyThrows;
import lombok.val;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private static final HwCache<Class<?>, ClassMetaDataHolder> CACHE = new MyCache<>();

    private final Class<T> clazz;

    @SneakyThrows
    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;

        if (CACHE.get(clazz) != null) {
            return;
        }
        val name = clazz.getSimpleName().toLowerCase();
        Field idField = null;
        val allFields = new ArrayList<Field>();
        for (val field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            }
            allFields.add(field);
        }
        val allFieldsWithoutId = new ArrayList<>(allFields);
        allFieldsWithoutId.remove(idField);
        val constructor = clazz.getDeclaredConstructor();
        CACHE.put(clazz, new ClassMetaDataHolder(name, idField, allFields, allFieldsWithoutId, constructor));
    }


    @Override
    public String getName() {
        return CACHE.get(clazz).name();
    }

    /**
     * @noinspection unchecked
     */
    @Override
    @SneakyThrows
    public Constructor<T> getConstructor() {
        return (Constructor<T>) CACHE.get(clazz).constructor;
    }

    @Override
    public Field getIdField() {
        return CACHE.get(clazz).idField();
    }

    @Override
    public List<Field> getAllFields() {
        return CACHE.get(clazz).allFields();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return CACHE.get(clazz).fieldsWithoutId();
    }

    private record ClassMetaDataHolder(String name, Field idField, List<Field> allFields, List<Field> fieldsWithoutId,
                                       Constructor<?> constructor) {
    }

}
