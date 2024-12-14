package guru.springframework.jdbc.repositories;


import guru.springframework.jdbc.domain.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> getByFirstNameAndLastName(String firstName, String lastName);

    List<Author> findByLastName(String lastName);

    Page<Author> findByLastName(String lastName, Pageable pageable);
}
