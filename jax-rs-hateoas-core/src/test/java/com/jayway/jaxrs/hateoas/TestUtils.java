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

package com.jayway.jaxrs.hateoas;

import java.util.Collection;
import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

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
