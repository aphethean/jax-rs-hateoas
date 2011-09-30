package com.jayway.jaxrs.hateoas;

public class HateoasInjectException extends RuntimeException {

	private static final long serialVersionUID = -7586666921228435121L;

	public HateoasInjectException(Exception e) {
		super(e);
	}

}
