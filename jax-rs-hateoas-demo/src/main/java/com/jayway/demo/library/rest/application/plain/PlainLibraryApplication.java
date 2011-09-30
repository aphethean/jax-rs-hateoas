package com.jayway.demo.library.rest.application.plain;

import com.sun.jersey.api.core.PackagesResourceConfig;

public class PlainLibraryApplication extends PackagesResourceConfig {
	public PlainLibraryApplication() {
		super("com.jayway.demo.library.rest.resources.plain");
	}
}
