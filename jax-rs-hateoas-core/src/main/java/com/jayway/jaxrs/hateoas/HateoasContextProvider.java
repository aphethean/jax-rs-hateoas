package com.jayway.jaxrs.hateoas;

public class HateoasContextProvider {
	private final static HateoasContext defaultContext = new DefaultHateoasContext();

	public static HateoasContext getDefaultContext() {
		return defaultContext;
	}
}
