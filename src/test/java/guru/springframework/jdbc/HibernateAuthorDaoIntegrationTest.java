package guru.springframework.jdbc;

import guru.springframework.jdbc.config.AppConfig;
import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.dao.AuthorDaoHibernate;
import guru.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Created by jt on 8/28/21.
 */
@ActiveProfiles("local")
@DataJpaTest
@Import({AuthorDaoHibernate.class, AppConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HibernateAuthorDaoIntegrationTest {
    @Autowired
    AuthorDao authorDao;

    @Test
    void testDeleteAuthor() {
        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("t");

        Author saved = authorDao.saveNewAuthor(author);

        authorDao.deleteAuthorById(saved.getId());

        Author deleted = authorDao.getById(saved.getId());
        assertThat(deleted).isNull();
    }

    @Test
    void testUpdateAuthor() {
        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("t");

        Author saved = authorDao.saveNewAuthor(author);

        saved.setLastName("Thompson");
        Author updated = authorDao.updateAuthor(saved);

        assertThat(updated.getLastName()).isEqualTo("Thompson");

        authorDao.deleteAuthorById(saved.getId());
    }

    @Test
    void testSaveAuthor() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Thompson");
        Author saved = authorDao.saveNewAuthor(author);

        assertThat(saved).isNotNull();

        authorDao.deleteAuthorById(saved.getId());
    }

    @Test
    void testGetAuthorByName() {
        Author author = authorDao.findAuthorByName("Craig", "Walls");

        assertThat(author).isNotNull();
    }

    @Test
    void testGetAuthor() {
        Author author = authorDao.getById(1L);

        assertThat(author).isNotNull();
    }

    @Test
    void testAuthorsByLastName() {
        List<Author> authors = authorDao.findAuthorByLastName("Smith");

        assertThat(authors).isNotNull();
        assertThat(authors.size()).isGreaterThan(0);
    }
}
