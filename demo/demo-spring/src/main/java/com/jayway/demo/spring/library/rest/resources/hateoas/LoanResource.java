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

package com.jayway.demo.spring.library.rest.resources.hateoas;

import com.jayway.demo.library.domain.Book;
import com.jayway.demo.library.domain.BookRepository;
import com.jayway.demo.library.domain.Customer;
import com.jayway.demo.library.domain.CustomerRepository;
import com.jayway.demo.library.rest.dto.LoanDto;
import com.jayway.jaxrs.hateoas.Linkable;
import com.jayway.jaxrs.hateoas.core.HateoasResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Collection;

@Path("/library/loans")
@Component
public class LoanResource {

    @Autowired
	private BookRepository bookRepository;
    @Autowired
	private CustomerRepository customerRepository;

    @POST
    @Consumes("application/vnd.demo.library.loan+json")
    @Produces("application/vnd.demo.library.loan+json")
    @Linkable(value = LinkableIds.LOAN_NEW_ID, templateClass = LoanDto.class)
    public Response newLoan(LoanDto loan) {
        Book book = bookRepository.getBookById(loan.getBookId());
        if (book.isBorrowed()) {
            return Response.status(Status.CONFLICT).build();
        }

        Customer customer = customerRepository.getById(loan.getCustomerId());
        book.setBorrowedBy(customer);

        return HateoasResponse
                .created(LinkableIds.LOAN_DETAILS_ID, book.getId())
                .entity(loan)
                .selfLink(LinkableIds.LOAN_DETAILS_ID, book.getId())
                .link(LinkableIds.BOOK_DETAILS_ID, Rels.BOOK, book.getId())
                .link(LinkableIds.CUSTOMER_DETAILS_ID, Rels.CUSTOMER, customer.getId())
                .selfLink(LinkableIds.LOAN_RETURN_ID, book.getId()).build();
    }

    @GET
    @Produces("application/vnd.demo.library.list.loan+json")
    @Linkable(LinkableIds.LOANS_LIST_ID)
    public Response getLoans() {
        Collection<Book> books = bookRepository.getLoans();

        Collection<LoanDto> dtos = LoanDto.fromBeanCollection(books);

        return HateoasResponse.ok(dtos).selfLink(LinkableIds.LOANS_LIST_ID)
                .selfLink(LinkableIds.LOAN_NEW_ID)
                .selfEach(LinkableIds.LOAN_DETAILS_ID, "bookId").build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/vnd.demo.library.loan+json")
    @Linkable(LinkableIds.LOAN_DETAILS_ID)
    public Response getLoan(@PathParam("id") Integer id) {
        Book book = bookRepository.getBookById(id);
        if (book == null || !book.isBorrowed()) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Customer customer = book.getBorrowedBy();
        return HateoasResponse.ok(new LoanDto(customer.getId(), book.getId()))
                .selfLink(LinkableIds.LOAN_DETAILS_ID, book.getId())
                .link(LinkableIds.BOOK_DETAILS_ID, Rels.BOOK, book.getId())
                .link(LinkableIds.CUSTOMER_DETAILS_ID, Rels.CUSTOMER, customer.getId())
                .selfLink(LinkableIds.LOAN_RETURN_ID, id).build();
    }

    @DELETE
    @Path("/{id}")
    @Linkable(LinkableIds.LOAN_RETURN_ID)
    public Response returnLoan(@PathParam("id") Integer id) {
        Book book = bookRepository.getBookById(id);
        book.returned();

		HateoasResponse.HateoasResponseBuilder builder = HateoasResponse.ok();
		return builder.location(builder.makeLink(LinkableIds.LOANS_LIST_ID, Rels.LOANS)).build();
    }
}
