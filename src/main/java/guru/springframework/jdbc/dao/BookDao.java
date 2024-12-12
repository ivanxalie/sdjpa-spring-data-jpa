package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookDao {

    Book saveNewBook(Book book);

    void deleteBookById(Long id);

    Book getById(Long id);

    Book updateBook(Book book);

    Book findBookByTitle(String title);

    List<Book> findAllBooks();

    List<Book> findAllBooks(int pageSize, int offset);

    List<Book> findAllBooks(Pageable pageable);

    List<Book> findAllBooksSortByTitle(Pageable pageable);
}
