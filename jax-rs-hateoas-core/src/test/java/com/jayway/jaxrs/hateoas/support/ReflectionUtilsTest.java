package com.jayway.jaxrs.hateoas.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jayway.jaxrs.hateoas.HateoasInjectException;

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
