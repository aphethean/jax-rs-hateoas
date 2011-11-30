/*
 * Copyright 2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	@Linkable(id = "test.dummy.fully.documented", description = "test description", label = "test label", templateClass = DummyDto.class)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.TEXT_HTML, MediaType.TEXT_PLAIN })
	public Response fullyDocumented(DummyDto input) {
		return null;
	}

	@GET
	@Linkable(id = "test.dummy.get")
	public Response get() {
		return null;
	}

	@GET
	@Linkable(id = "test.dummy.get.subpath")
	@Path("/subpath")
	public Response getSubpath() {
		return null;
	}

	@POST
	@Linkable(id = "test.dummy.post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(DummyDto input) {
		return null;
	}

	@DELETE
	@Linkable(id = "test.dummy.delete")
	public Response delete(DummyDto input) {
		return null;
	}
}
