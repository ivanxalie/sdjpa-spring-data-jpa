package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.repositories.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jt on 8/28/21.
 */
@Component
@RequiredArgsConstructor
@Primary
public class AuthorDaoImpl implements AuthorDao {
    private final AuthorRepository repository;

    @Override
    public Author getById(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return repository
                .getByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return repository.save(author);
    }

    @Override
    public Author updateAuthor(Author author) {
        return repository.save(author);
    }

    @Override
    public void deleteAuthorById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Author> findAuthorByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    @Override
    public List<Author> findAuthorByLastName(String lastName, Pageable pageable) {
        return repository.findByLastName(lastName, pageable);
    }
}
