package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {
    private final BookRepository repository;

    @Override
    public Book saveNewBook(Book book) {
        return repository.save(book);
    }

    @Override
    public void deleteBookById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Book getById(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public Book updateBook(Book book) {
        return repository.save(book);
    }

    @Override
    public Book findBookByTitle(String title) {
        return repository.findByTitle(title).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Book> findAllBooks() {
        return repository.findAll();
    }
}
