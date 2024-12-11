package guru.springframework.jdbc.repositories;


import guru.springframework.jdbc.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);

    Book readByTitle(String title);

    @Nullable
    Book getByTitle(@Nullable String title);

    Stream<Book> findAllByTitleNotNull();

    @Async
    Future<Book> queryByTitle(String title);

    @Query("select b from Book b where b.title = :title")
    Book giveMeMyBookWithATitleNOW(String title);

    @Query(nativeQuery = true, value = "select * from book where title = :theSacredBook")
    Book theCodeBookOfGloriousEvolution(String theSacredBook);
}
