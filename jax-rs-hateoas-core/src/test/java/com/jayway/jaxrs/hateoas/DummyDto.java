package com.jayway.jaxrs.hateoas;

public class DummyDto {
	@Override
	public boolean equals(Object obj) {
		return obj instanceof DummyDto;
	}

	@Override
	public int hashCode() {
		return 1;
	}
}
