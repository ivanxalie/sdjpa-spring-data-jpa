package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorDaoJdbcTemplate implements AuthorDao {
    private final ObjectProvider<RowMapper<Author>> mapper;
    private final JdbcTemplate template;

    @Override
    public Author getById(Long id) {
        try {
            return template.queryForObject("select * from author where id = ?", mapper.getObject(), id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        try {
            return template.queryForObject("select * from author where first_name = ? and last_name = ?", mapper.getObject(), firstName, lastName);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Author saveNewAuthor(Author author) {
        String sql = "INSERT INTO author (first_name, last_name) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, author.getFirstName());
            ps.setObject(2, author.getLastName());
            return ps;
        }, keyHolder);
        author.setId(keyHolder.getKey().longValue());
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        template.update("update author set first_name = ?, last_name = ? where id = ?", author.getFirstName(), author.getLastName(), author.getId());
        return author;
    }

    @Override
    public void deleteAuthorById(Long id) {
        template.update("delete from author where id = ?", id);
    }

    @Override
    public List<Author> findAuthorByLastName(String lastName) {
        try {
            return template.query("select * from author where last_name = ?", mapper.getObject(), lastName);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Author> findAuthorByLastName(String lastName, Pageable pageable) {
        try {
            StringBuilder sql = new StringBuilder("select * from author where last_name = ?");
            Sort.Order order = pageable.getSort().getOrderFor("first_name");
            if (order != null) {
                sql.append(" order by first_name");
                if (order.isAscending()) sql.append(" asc");
                else sql.append(" desc");
            }
            sql.append(" limit ? offset ?");
            return template.query(sql.toString(), mapper.getObject(), lastName,
                    pageable.getPageSize(),
                    pageable.getOffset()
            );
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
