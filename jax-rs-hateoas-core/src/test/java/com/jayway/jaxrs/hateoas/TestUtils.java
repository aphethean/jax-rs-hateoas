package com.jayway.jaxrs.hateoas;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;

public class TestUtils {
	private TestUtils() {

	}

	public static void assertArray(String[] actualValues,
			String... expectedValues) {
		assertEquals(actualValues.length, expectedValues.length);
		Collection<String> actualValuesCollection = asCollection(actualValues);

		for (String expectedValue : expectedValues) {
			assertTrue("Array did not contain expected value" + expectedValue,
					actualValuesCollection.contains(expectedValue));
		}
	}

	public static <T> Collection<T> asCollection(T[] array) {
		Collection<T> result = new HashSet<T>();
		for (T t : array) {
			result.add(t);
		}

		return result;
	}

}
