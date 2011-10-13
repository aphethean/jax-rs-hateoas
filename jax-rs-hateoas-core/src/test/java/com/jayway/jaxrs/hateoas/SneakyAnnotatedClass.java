package com.jayway.jaxrs.hateoas;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/dummy/")
public class SneakyAnnotatedClass {

	@GET
	@Linkable(id = "test.dummy.sneaky.get", rel = "dummy2")
	public Response get() {
		return null;
	}

	@GET
	@Linkable(id = "test.dummy.sneaky.subpath", rel = "dummy2")
	@Path("/subpath")
	public Response getSubpath() {
		return null;
	}

}
