package guru.springframework.jdbc;

import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    void testEmptyResultException() {

        assertThrows(EmptyResultDataAccessException.class, () -> {
            bookRepository.readByTitle("foobar4");
        });
    }

    @Test
    void testNullParam() {
        assertNull(bookRepository.getByTitle(null));
    }

    @Test
    void testNoException() {
        assertNull(bookRepository.getByTitle("foo"));
    }

    @Test
    void testBookStream() {
        AtomicInteger counter = new AtomicInteger();

        bookRepository
                .findAllByTitleNotNull().forEach(book -> counter.incrementAndGet());

        assertThat(counter.get()).isGreaterThan(4);
    }

    @Test
    @SneakyThrows
    void testBookFuture() {
        Future<Book> bookFuture = bookRepository.queryByTitle("Clean Code");
        assertNotNull(bookFuture.get());
    }

    @Test
    void testBookQuery() {
        assertNotNull(bookRepository.giveMeMyBookWithATitleNOW("Clean Code"));
    }

    @Test
    void testNativeQuery() {
        assertNotNull(bookRepository.theCodeBookOfGloriousEvolution("Clean Code"));
    }
}