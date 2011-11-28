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

package com.jayway.demo.library.rest.resources.plain;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.jayway.demo.library.domain.Book;
import com.jayway.demo.library.domain.BookRepository;
import com.jayway.demo.library.domain.factory.RepositoryFactory;
import com.jayway.demo.library.rest.dto.BookDto;
import com.jayway.demo.library.rest.dto.BookListDto;

@Path("/library/books")
public class PlainBookResource {
	private final BookRepository bookRepository;

	public PlainBookResource() {
		bookRepository = RepositoryFactory.getBookRepository();
	}

	@GET
	@Produces("application/vnd.demo.library.list.book+json")
	public Response getAllBooks() {
		return Response.ok(
				BookListDto.fromBeanCollection(bookRepository.getAllBooks()))
				.build();
	}

	@POST
	@Consumes("application/vnd.demo.library.book+json")
	@Produces("application/vnd.demo.library.book+json")
	public Response newBook(BookDto book, @Context UriInfo uriInfo) {
		Book newBook = bookRepository
				.newBook(book.getAuthor(), book.getTitle());

		URI bookUri = uriInfo.getAbsolutePathBuilder()
				.path(PlainBookResource.class, "getBookById")
				.build(newBook.getId());
		return Response.created(bookUri).entity(BookDto.fromBean(newBook))
				.build();
	}

	@GET
	@Path("/{id}")
	@Produces("application/vnd.demo.library.book+json")
	public Response getBookById(@PathParam("id") Integer id) {
		Book book = bookRepository.getBookById(id);
		return Response.ok(BookDto.fromBean(book)).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes("application/vnd.demo.library.book+json")
	@Produces("application/vnd.demo.library.book+json")
	public Response updateBook(@PathParam("id") Integer id, BookDto updatedBook) {
		Book book = bookRepository.getBookById(id);
		book.setAuthor(updatedBook.getAuthor());
		book.setTitle(updatedBook.getTitle());

		return getBookById(id);
	}
}
