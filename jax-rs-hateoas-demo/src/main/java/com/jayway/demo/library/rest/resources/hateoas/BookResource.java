package com.jayway.demo.library.rest.resources.hateoas;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jayway.demo.library.domain.Book;
import com.jayway.demo.library.domain.BookRepository;
import com.jayway.demo.library.domain.factory.RepositoryFactory;
import com.jayway.demo.library.rest.dto.BookDto;
import com.jayway.demo.library.rest.dto.BookListDto;
import com.jayway.jaxrs.hateoas.Linkable;
import com.jayway.jaxrs.hateoas.core.HateoasResponse;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;

@Path("/library/books")
public class BookResource {

	private final BookRepository bookRepository;

	public BookResource() {
		bookRepository = RepositoryFactory.getBookRepository();
	}

	@GET
	@Linkable(id = LinkableIds.BOOKS_LIST_ID, rel = Rels.BOOKS_REL)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBooks() {
		return HateoasResponse
				.ok(BookListDto.fromBeanCollection(bookRepository.getAllBooks()))
				.link(LinkableIds.BOOK_NEW_ID)
				.each(LinkableIds.BOOK_DETAILS_ID, "id").build();
	}

	@POST
	@Linkable(id = LinkableIds.BOOK_NEW_ID, rel = Rels.BOOK_REL, templateClass = BookDto.class)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newBook(BookDto book) {
		Book newBook = bookRepository
				.newBook(book.getAuthor(), book.getTitle());
		return HateoasResponse
				.created(LinkableIds.BOOK_DETAILS_ID, newBook.getId())
				.entity(BookDto.fromBean(newBook))
				.selfLink(LinkableIds.BOOK_DETAILS_ID, newBook.getId()).build();
	}

	@GET
	@Linkable(id = LinkableIds.BOOK_DETAILS_ID, rel = Rels.BOOK_REL)
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBookById(@PathParam("id") Integer id) {
		Book book = bookRepository.getBookById(id);
		HateoasResponseBuilder builder = HateoasResponse
				.ok(BookDto.fromBean(book))
				.selfLink(LinkableIds.BOOK_DETAILS_ID, id)
				.selfLink(LinkableIds.BOOK_UPDATE_ID, id);

		if (!book.isBorrowed()) {
			builder.link(LinkableIds.LOAN_NEW_ID);
		} else {
			builder.link(LinkableIds.LOAN_DETAILS_ID, book.getId());
		}
		return builder.build();
	}

	@PUT
	@Linkable(id = LinkableIds.BOOK_UPDATE_ID, rel = Rels.BOOK_REL, templateClass = BookDto.class)
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
