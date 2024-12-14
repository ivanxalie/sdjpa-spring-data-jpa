package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static guru.springframework.jdbc.config.AppConfig.ENTITY_MANAGER_NAME;
import static guru.springframework.jdbc.domain.Author.FIND_BY_LAST_NAME;
import static guru.springframework.jdbc.domain.Author.FIND_BY_NAME;

/**
 * Created by jt on 8/28/21.
 */
@Component
public class AuthorDaoHibernate implements AuthorDao {

    @Override
    public Author getById(Long id) {
        return execute(manager -> manager.find(Author.class, id));
    }

    private <TYPE> TYPE execute(Function<EntityManager, TYPE> function) {
        EntityManager manager = manager();
        try {
            return function.apply(manager);
        } finally {
            manager.close();
        }
    }

    @Lookup(ENTITY_MANAGER_NAME)
    protected EntityManager manager() {
        return null;
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return execute(manager -> {
            TypedQuery<Author> query = manager.createNamedQuery(FIND_BY_NAME, Author.class);
            query.setParameter("firstName", firstName);
            query.setParameter("lastName", lastName);
            return query.getSingleResult();
        });
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return execute(manager -> {
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(author);
            transaction.commit();
            return author;
        });
    }

    @Override
    public Author updateAuthor(Author author) {
        return execute(manager -> {
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.merge(author);
            transaction.commit();
            return author;
        });
    }

    @Override
    public void deleteAuthorById(Long id) {
        execute(manager -> {
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.remove(manager.find(Author.class, id));
            transaction.commit();
            return Void.class;
        });
    }

    @Override
    public List<Author> findAuthorByLastName(String lastName) {
        return execute(entityManager ->
                {
                    TypedQuery<Author> query = entityManager.createNamedQuery(FIND_BY_LAST_NAME, Author.class);
                    query.setParameter("lastName", lastName);
                    return query.getResultList();
                }
        );
    }

    @Override
    public List<Author> findAuthorByLastName(String lastName, Pageable pageable) {
        return execute(entityManager -> {
                    StringBuilder hql = new StringBuilder("select a from Author a where a.lastName = :lastName");
                    Sort sort = pageable.getSort();
                    if (sort.isSorted()) {
                        hql.append(" order by ");
                        hql.append(sort.get().map(order -> order.getProperty() + " " +
                                order.getDirection().name()).collect(Collectors.joining(",")));
                    }
                    TypedQuery<Author> query = entityManager.createQuery(
                            hql.toString(), Author.class);
                    query.setFirstResult(Math.toIntExact(pageable.getOffset()));
                    query.setMaxResults(Math.toIntExact(pageable.getPageSize()));
                    query.setParameter("lastName", lastName);
                    return query.getResultList();
                }
        );
    }
}
