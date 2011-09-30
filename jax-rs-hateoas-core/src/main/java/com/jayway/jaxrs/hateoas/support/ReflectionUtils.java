package com.jayway.jaxrs.hateoas.support;

import java.lang.reflect.Field;

import com.jayway.jaxrs.hateoas.HateoasInjectException;

public class ReflectionUtils {
	private ReflectionUtils() {

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

	public static Field getField(Object entity, String fieldName)
			throws NoSuchFieldException {
		Field field = entity.getClass().getDeclaredField(fieldName);
		setFieldAccessible(field);
		return field;
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
