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

import com.jayway.jaxrs.hateoas.HateoasInjectException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Mattias Hellborg Arthursson
 */
public class ReflectionUtilsTest {

	@Test
	public void givenThatPrivateFieldExistsItWillBeRetrievedByGetFieldValue() {
		Object result = ReflectionUtils.getFieldValue(new DummyBean(), "value");
		assertEquals("expectedValue", result);
	}

	@Test
	public void givenThatPublicFieldExistsItWillBeRetrievedByGetFieldValue() {
		Object result = ReflectionUtils.getFieldValue(new DummyBean(),
				"otherValue");
		assertEquals("otherExpectedValue", result);
	}

	@Test(expected = HateoasInjectException.class)
	public void nonexistingFieldThrowsException() {
		ReflectionUtils.getFieldValue(new DummyBean(), "thisfielddoesntexist");
	}

	@Test
	public void givenThatFieldExistsItCanBeSet() {
		DummyBean dummyBean = new DummyBean();
		ReflectionUtils.setField(dummyBean, "value", "newValue");

		assertEquals("newValue", dummyBean.getValue());
	}

	private class DummyBean {
		private String value = "expectedValue";
		public String otherValue = "otherExpectedValue";

		public String getValue() {
			return value;
		}

		public String getOtherValue() {
			return otherValue;
		}
	}

}
