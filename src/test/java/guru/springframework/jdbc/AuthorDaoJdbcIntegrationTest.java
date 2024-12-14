package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.dao.AuthorDaoJdbcTemplate;
import guru.springframework.jdbc.dao.AuthorMapper;
import guru.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AuthorDaoJdbcTemplate.class, AuthorMapper.class})
class AuthorDaoJdbcIntegrationTest {
    @Autowired
    AuthorDao authorDao;

    @Test
    void testGetAuthorById() {
        Author author = authorDao.getById(1L);

        assertThat(author).isNotNull().satisfies(selectedAuthor -> {
            assertThat(selectedAuthor.getFirstName()).isEqualTo("Craig");
            assertThat(selectedAuthor.getLastName()).isEqualTo("Walls");
        });
    }

    @Test
    void testGetAuthorByName() {
        Author author = authorDao.findAuthorByName("Craig", "Walls");

        assertThat(author).isNotNull().satisfies(selectedAuthor -> assertThat(selectedAuthor.getId())
                .isEqualTo(1L));
    }

    @Test
    void testFindAuthorByLastName() {
        List<Author> authors = authorDao.findAuthorByLastName("Evans");

        assertThat(authors).isNotNull().hasSize(2);
    }

    @Test
    void testFindAuthorByLastNameOrderByFirstNameSecondPage() {
        List<Author> authors = authorDao.findAuthorByLastName("Smith", PageRequest.of(2, 10,
                Sort.by(Sort.Direction.DESC, "first_name")));

        assertThat(authors).isNotNull().hasSize(10);
    }

    @Test
    void testInsert() {
        Author author = authorDao.saveNewAuthor(Author.builder()
                .firstName("Andrew")
                .lastName("Bean")
                .build());

        Author saved = authorDao.getById(author.getId());

        assertThat(saved).isNotNull().satisfies(selectedAuthor -> {
            assertThat(selectedAuthor.getFirstName()).isEqualTo("Andrew");
            assertThat(selectedAuthor.getLastName()).isEqualTo("Bean");
        });

        authorDao.deleteAuthorById(saved.getId());
    }

    @Test
    void testUpdate() {
        Author authorToUpdate = authorDao.getById(1L);
        String previousLastName = authorToUpdate.getLastName();
        authorToUpdate.setLastName("Carlione");

        authorDao.updateAuthor(authorToUpdate);

        Author fetched = authorDao.getById(authorToUpdate.getId());

        assertThat(fetched).isNotNull();
        assertThat(fetched.getLastName()).isEqualTo("Carlione");

        authorToUpdate.setLastName(previousLastName);
        authorDao.updateAuthor(authorToUpdate);
    }

    @Test
    void testDeleteById() {
        Author author = authorDao.saveNewAuthor(Author.builder().build());

        authorDao.deleteAuthorById(author.getId());

        assertThat(authorDao.getById(author.getId())).isNull();
    }
}