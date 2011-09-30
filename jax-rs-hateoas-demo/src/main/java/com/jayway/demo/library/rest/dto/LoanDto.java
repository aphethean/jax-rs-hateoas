package com.jayway.demo.library.rest.dto;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jayway.demo.library.domain.Book;
import com.jayway.demo.library.domain.Customer;

import java.util.Collection;

public class LoanDto {
    private Integer customerId;
    private Integer bookId;

    public LoanDto() {

    }

    public LoanDto(Integer customerId, Integer bookId) {
        this.customerId = customerId;
        this.bookId = bookId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer userId) {
        this.customerId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public static LoanDto fromBean(Book book) {
        return new LoanDto(book.getBorrowedBy().getId(), book.getId());
    }

    public static Collection<LoanDto> fromBeanCollection(
            Collection<Book> books) {
        return Collections2.transform(books, new Function<Book, LoanDto>() {
            @Override
            public LoanDto apply(Book book) {
                return fromBean(book);
            }
        });
    }

}
