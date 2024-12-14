package guru.springframework.jdbc.domain;

import jakarta.persistence.*;
import lombok.*;

import static guru.springframework.jdbc.domain.Author.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedQueries({
        @NamedQuery(name = FIND_ALL, query = "from Author"),
        @NamedQuery(name = FIND_BY_LAST_NAME, query = "from Author a where a.lastName = :lastName"),
        @NamedQuery(name = FIND_BY_NAME, query = "from Author a where a.lastName = :lastName and a.firstName = :firstName")
})
@ToString
public class Author {
    public static final String FIND_ALL = "selectAuthors";
    public static final String FIND_BY_LAST_NAME = "findAuthorByLastName";
    public static final String FIND_BY_NAME = "findAuthorByName";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
}
