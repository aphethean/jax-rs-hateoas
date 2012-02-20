package com.jayway.demo.library.rest.resources.hateoas;

import com.jayway.demo.library.domain.Book;
import com.jayway.demo.library.domain.BookRepository;
import com.jayway.demo.library.rest.dto.BookDto;
import com.jayway.jaxrs.hateoas.Linkable;
import com.jayway.jaxrs.hateoas.core.HateoasResponse;
import com.jayway.jaxrs.hateoas.support.AtomRels;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by IntelliJ IDEA.
 * User: kallestenflo
 * Date: 2/19/12
 * Time: 3:44 PM
 */
public class BookResource {

    private final BookRepository bookRepository;

    public BookResource(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GET
    @Linkable(LinkableIds.BOOK_DETAILS_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") Integer id) {
        Book book = bookRepository.getBookById(id);
        HateoasResponse.HateoasResponseBuilder builder = HateoasResponse
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") Integer id, BookDto updatedBook) {
        Book book = bookRepository.getBookById(id);
        book.setAuthor(updatedBook.getAuthor());
        book.setTitle(updatedBook.getTitle());

        return getBookById(id);
    }
}
