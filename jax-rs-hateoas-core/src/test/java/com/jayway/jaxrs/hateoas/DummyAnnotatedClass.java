package com.jayway.jaxrs.hateoas;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/dummy")
public class DummyAnnotatedClass {

	@PUT
	@Linkable(id = "test.dummy.fully.documented", rel = "dummy", description = "test description", label = "test label", templateClass = DummyDto.class)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.TEXT_HTML, MediaType.TEXT_PLAIN })
	public Response fullyDocumented(DummyDto input) {
		return null;
	}

	@GET
	@Linkable(id = "test.dummy.get", rel = "dummy2")
	public Response get() {
		return null;
	}

	@GET
	@Linkable(id = "test.dummy.get.subpath", rel = "dummy2")
	@Path("/subpath")
	public Response getSubpath() {
		return null;
	}

	@POST
	@Linkable(id = "test.dummy.post", rel = "dummy")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(DummyDto input) {
		return null;
	}

	@DELETE
	@Linkable(id = "test.dummy.delete", rel = "dummy")
	public Response delete(DummyDto input) {
		return null;
	}
}
