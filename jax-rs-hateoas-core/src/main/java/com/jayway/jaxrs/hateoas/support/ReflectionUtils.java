/*
 * Copyright 2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayway.jaxrs.hateoas.support;

import com.google.common.collect.Lists;
import com.jayway.jaxrs.hateoas.HateoasInjectException;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * General reflection utilities. Not intended for external use.
 *
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public class ReflectionUtils {
    private ReflectionUtils() {

    }

    public static boolean hasField(Object entity, String fieldName) {
        try {
            getField(entity, fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static boolean hasFieldHierarchical(Object entity, String fieldName) {
        try {
            getFieldHierarchical(entity.getClass(), fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static boolean hasField(Class clazz, String fieldName) {
        try {
            //clazz.getField(fieldName);
            clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static void setFieldAccessible(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static Object getFieldValue(Object entity, String fieldName) {
        try {
            Field field = getField(entity, fieldName);
            return field.get(entity);
        } catch (Exception e) {
            throw new HateoasInjectException(e);
        }
    }

    public static Object getFieldValueHierarchical(Object entity, String fieldName) {
        try {
            Field field = getFieldHierarchical(entity.getClass(), fieldName);
            return field.get(entity);
        } catch (Exception e) {
            throw new HateoasInjectException(e);
        }
    }

    public static Field getField(Object entity, String fieldName)
            throws NoSuchFieldException {
        Field field = entity.getClass().getDeclaredField(fieldName);
        setFieldAccessible(field);
        return field;
    }

    public static Field getField(Class clazz, String fieldName)
            throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        setFieldAccessible(field);
        return field;
    }

    public static Collection<Field> getFieldsHierarchical(Class clazz) {
        Collection<Field> fields = Lists.newLinkedList();

        for (Field field : clazz.getDeclaredFields()) {
            fields.add(field);
        }
        Class superClass = clazz.getSuperclass();

        if (!clazz.getSuperclass().equals(Object.class)) {
            fields.addAll(getFieldsHierarchical(clazz.getSuperclass()));
        }
        return fields;
    }

    public static Field getFieldHierarchical(Class clazz, String fieldName)
            throws NoSuchFieldException {

        if (clazz.equals(Object.class)) {
            throw new NoSuchFieldException("Field not found: " + fieldName);
        }

        if (hasField(clazz, fieldName)) {
            return getField(clazz, fieldName);
        } else {
            return getFieldHierarchical(clazz.getSuperclass(), fieldName);
        }
    }

    public static void setFieldHierarchical(Object entity, String fieldName, Object value) {
        boolean done = false;
        try {
            if (hasField(entity, fieldName)) {
                setField(entity, fieldName, value);
                done = true;
            } else {
                Class superClass = entity.getClass().getSuperclass();
                do {
                    if (hasField(superClass, fieldName)) {
                        Field field = getField(superClass, fieldName);
                        field.set(entity, value);
                        done = true;
                        break;
                    } else {
                        superClass = superClass.getSuperclass();
                    }

                } while (!Object.class.equals(superClass));
            }
        } catch (Exception e) {
            throw new HateoasInjectException(e);
        }
        if (!done) {
            throw new HateoasInjectException("Field not found: " + fieldName);
        }
    }

    public static void setField(Object entity, String fieldName, Object value) {
        try {
            Field field = getField(entity, fieldName);
            field.set(entity, value);
        } catch (Exception e) {
            throw new HateoasInjectException(e);
        }
    }
}
