package com.jayway.jaxrs.hateoas;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by IntelliJ IDEA.
 * User: kallestenflo
 * Date: 2/19/12
 * Time: 2:27 PM
 */

@Path("/root")
public class DummyRootResourceClass {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Linkable("dummy.root.resource.get")
    public Response get() {
        return Response.ok("hello from dummy root").type(MediaType.TEXT_PLAIN).build();
    }

    @Path("/{id}")
    public DummySubResourceClass sub(){
        return new DummySubResourceClass();
    }
}
