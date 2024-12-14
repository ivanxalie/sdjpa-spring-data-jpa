package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static guru.springframework.jdbc.config.AppConfig.ENTITY_MANAGER_NAME;
import static guru.springframework.jdbc.domain.Book.FIND_BY_TITLE;

@Component
public class BookDaoHibernate implements BookDao {
    @Override
    public Book getById(Long id) {
        return execute(entityManager -> manager().find(Book.class, id));
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
    public Book saveNewBook(Book book) {
        return execute(manager -> {
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(book);
            transaction.commit();
            return book;
        });
    }

    @Override
    public void deleteBookById(Long id) {
        execute(manager -> {
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.remove(manager.find(Book.class, id));
            transaction.commit();
            return Void.class;
        });
    }

    @Override
    public Book updateBook(Book book) {
        return execute(manager -> {
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.merge(book);
            transaction.commit();
            return book;
        });
    }

    @Override
    public Book findBookByTitle(String title) {
        return execute(manager -> {
            TypedQuery<Book> query = manager.createNamedQuery(FIND_BY_TITLE, Book.class);
            query.setParameter("title", title);
            return query.getSingleResult();
        });
    }

    @Override
    public List<Book> findAllBooks() {
        return execute(entityManager ->
                entityManager
                        .createNamedQuery(Book.FIND_ALL, Book.class)
                        .getResultList()
        );
    }

    @Override
    public List<Book> findAllBooks(int pageSize, int offset) {
        return List.of();
    }

    @Override
    public List<Book> findAllBooks(Pageable pageable) {
        return execute(entityManager -> {
                    String hql = "select b from Book b";
                    Sort.Order order = pageable.getSort().getOrderFor("title");
                    if (order != null) hql += " order by b.title " + order.getDirection().name();
                    TypedQuery<Book> query = entityManager.createQuery(hql, Book.class);
                    query.setFirstResult(Math.toIntExact(pageable.getOffset()));
                    query.setMaxResults(Math.toIntExact(pageable.getPageSize()));
                    return query.getResultList();
                }
        );
    }

    @Override
    public List<Book> findAllBooksSortByTitle(Pageable pageable) {
        return findAllBooks(pageable);
    }
}
