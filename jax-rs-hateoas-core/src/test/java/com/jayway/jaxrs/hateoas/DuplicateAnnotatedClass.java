package com.jayway.jaxrs.hateoas;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/dummy/")
public class DuplicateAnnotatedClass {

	@GET
	@Linkable(id = "test.dummy.get", rel = "dummy2")
	public Response get() {
		return null;
	}
}
