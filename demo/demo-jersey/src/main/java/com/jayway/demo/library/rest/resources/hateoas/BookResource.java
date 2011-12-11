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

import com.jayway.demo.library.domain.Book;
import com.jayway.demo.library.domain.BookRepository;
import com.jayway.demo.library.domain.factory.RepositoryFactory;
import com.jayway.demo.library.rest.dto.BookDto;
import com.jayway.demo.library.rest.dto.BookListDto;
import com.jayway.jaxrs.hateoas.Linkable;
import com.jayway.jaxrs.hateoas.core.HateoasResponse;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.jayway.jaxrs.hateoas.support.AtomRels;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/library/books")
public class BookResource {

    private final BookRepository bookRepository;

    public BookResource() {
        bookRepository = RepositoryFactory.getBookRepository();
    }

    @GET
    @Linkable(LinkableIds.BOOKS_LIST_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        return HateoasResponse
                .ok(BookListDto.fromBeanCollection(bookRepository.getAllBooks()))
                .selfLink(LinkableIds.BOOK_NEW_ID)
                .selfEach(LinkableIds.BOOK_DETAILS_ID, "id").build();
    }

    @POST
    @Linkable(value = LinkableIds.BOOK_NEW_ID, templateClass = BookDto.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newBook(BookDto book) {
        Book newBook = bookRepository
                .newBook(book.getAuthor(), book.getTitle());
        return HateoasResponse
                .created(LinkableIds.BOOK_DETAILS_ID, newBook.getId())
                .entity(BookDto.fromBean(newBook)).build();
    }

    @GET
    @Linkable(LinkableIds.BOOK_DETAILS_ID)
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") Integer id) {
        Book book = bookRepository.getBookById(id);
        HateoasResponseBuilder builder = HateoasResponse
                .ok(BookDto.fromBean(book))
                .link(LinkableIds.BOOK_UPDATE_ID, AtomRels.SELF, id);

        if (!book.isBorrowed()) {
            builder.link(LinkableIds.LOAN_NEW_ID, Rels.LOANS);
        } else {
            builder.link(LinkableIds.LOAN_DETAILS_ID, Rels.LOAN, book.getId());
        }
        return builder.build();
    }

    @PUT
    @Linkable(value = LinkableIds.BOOK_UPDATE_ID, templateClass = BookDto.class)
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") Integer id, BookDto updatedBook) {
        Book book = bookRepository.getBookById(id);
        book.setAuthor(updatedBook.getAuthor());
        book.setTitle(updatedBook.getTitle());

        return getBookById(id);
    }
}
