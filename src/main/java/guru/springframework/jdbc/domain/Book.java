package guru.springframework.jdbc.domain;

import jakarta.persistence.*;
import lombok.*;

import static guru.springframework.jdbc.domain.Book.FIND_BY_TITLE;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NamedQuery(name = FIND_BY_TITLE, query = "from Book b where b.title = :title")
@ToString
public class Book {
    public static final String FIND_BY_TITLE = "Book.findByTitleReallyQuick";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String title;
    private String isbn;
    private String publisher;
    @OneToOne
    @ToString.Exclude
    private Author author;
}
