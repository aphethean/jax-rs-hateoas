package com.jayway.demo.library.rest.resources.hateoas;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.jayway.demo.library.rest.dto.RootDto;
import com.jayway.jaxrs.hateoas.core.HateoasResponse;

@Path("/")
public class LibraryResource {

	@GET
	@Produces("application/vnd.demo.library.root+json")
	public Response root() {
		return HateoasResponse.ok(new RootDto())
				.link(LinkableIds.BOOKS_LIST_ID)
				.link(LinkableIds.CUSTOMER_LIST_ID)
				.link(LinkableIds.LOANS_LIST_ID).build();
	}
}
