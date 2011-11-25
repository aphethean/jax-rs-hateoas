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
