package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.AuthorDaoImpl;
import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.dao.BookDaoJdbcTemplate;
import guru.springframework.jdbc.dao.BookMapper;
import guru.springframework.jdbc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({BookDaoJdbcTemplate.class, AuthorDaoImpl.class, BookMapper.class})
class BookDaoIntegrationTest {
    @Autowired
    BookDao bookDao;

    @Test
    void testFindAll() {
        List<Book> books = bookDao.findAllBooks();

        assertThat(books).isNotNull().hasSize(5);
    }

    @Test
    void testFindAllPageAndSize() {
        List<Book> books = bookDao.findAllBooks(2, 2);

        assertThat(books).isNotNull().hasSize(2);
    }

    @Test
    void testFindAllPageAndSizePageable() {
        List<Book> books = bookDao.findAllBooks(PageRequest.ofSize(2));

        assertThat(books).isNotNull().hasSize(2);
    }

    @Test
    void testFindAllPageAndSizePageableOrderByTitle() {
        List<Book> books = bookDao.findAllBooksSortByTitle(PageRequest.ofSize(2));

        assertThat(books).isNotNull().hasSize(2);
    }

    @Test
    void testGetBookById() {
        Book book = bookDao.getById(1L);

        assertThat(book).isNotNull().satisfies(selectedBook -> {
            assertThat(selectedBook.getTitle()).isEqualTo("Spring in Action, 5th Edition");
            assertThat(selectedBook.getPublisher()).isEqualTo("Simon & Schuster");
            assertThat(selectedBook.getIsbn()).isEqualTo("978-1617294945");
        });
    }

    @Test
    void testGetAuthorByTitle() {
        Book author = bookDao.findBookByTitle("Spring in Action, 5th Edition");

        assertThat(author).isNotNull().satisfies(selectedAuthor -> assertThat(selectedAuthor.getId())
                .isEqualTo(1L));
    }

    @Test
    void testInsert() {
        Book author = bookDao.saveNewBook(Book.builder()
                .title("Andrew")
                .publisher("Bean")
                .isbn("Bean")
                .build());

        Book saved = bookDao.getById(author.getId());

        assertThat(saved).isNotNull().satisfies(selectedAuthor -> {
            assertThat(selectedAuthor.getTitle()).isEqualTo("Andrew");
            assertThat(selectedAuthor.getPublisher()).isEqualTo("Bean");
            assertThat(selectedAuthor.getIsbn()).isEqualTo("Bean");
        });

        bookDao.deleteBookById(author.getId());
    }

    @Test
    void testUpdate() {
        Book authorToUpdate = bookDao.getById(1L);
        String previousLastName = authorToUpdate.getIsbn();
        authorToUpdate.setIsbn("Carlione");

        bookDao.updateBook(authorToUpdate);

        Book fetched = bookDao.getById(authorToUpdate.getId());

        assertThat(fetched).isNotNull();
        assertThat(fetched.getIsbn()).isEqualTo("Carlione");

        authorToUpdate.setIsbn(previousLastName);
        bookDao.updateBook(authorToUpdate);
    }

    @Test
    void testDeleteById() {
        Book author = bookDao.saveNewBook(Book.builder().build());

        bookDao.deleteBookById(author.getId());

        assertThat(bookDao.getById(author.getId())).isNull();
    }
}