package com.expedia.content.media.processing.pipeline.util;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

public final class TestingUtil {

    private TestingUtil() {
        //non-op
    }

    /**
     * Set a field value using reflection
     *
     * @param obj Object that contains the field.
     * @param fieldName Field name. Private/Static fields are accessible too.
     * @param value Value to put on the field
     *
     * @throws NoSuchFieldException when the field name is not found.
     * @throws IllegalAccessException when there is another illegal access to the field. However private/static fields
     * are supported.
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        final Field field = FieldUtils.getField(obj.getClass(), fieldName, true);
        field.set(obj, value);
    }

    /**
     * Get a field value using reflection.
     *
     * @param obj Object that contains the field
     * @param fieldName Field name. Private/Static fields are accessible too.
     * @return
     * @throws NoSuchFieldException when the field name is not found.
     * @throws IllegalAccessException when there is another illegal access to the field. However private/static fields
     * are supported.
     */
    public static Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        final Field field = FieldUtils.getField(obj.getClass(), fieldName, true);
        return field.get(obj);
    }


}
