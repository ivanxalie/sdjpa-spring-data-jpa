package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;

public interface BookDao {

    Book saveNewBook(Book book);

    void deleteBookById(Long id);

    Book getById(Long id);

    Book updateBook(Book book);

    Book findBookByTitle(String title);
}
