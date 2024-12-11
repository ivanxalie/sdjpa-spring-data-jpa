package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 8/28/21.
 */
@Component
@RequiredArgsConstructor
public class AuthorDaoImpl implements AuthorDao {
    private final AuthorRepository repository;

    @Override
    public Author getById(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return repository.getAuthorByFirstNameAndLastName(firstName, lastName);
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
}
