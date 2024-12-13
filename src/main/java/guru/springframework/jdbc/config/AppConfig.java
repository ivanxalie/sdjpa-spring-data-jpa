package guru.springframework.jdbc.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
    public static final String ENTITY_MANAGER_NAME = "myEntityManagerName";

    @Bean(ENTITY_MANAGER_NAME)
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public EntityManager entityManager(EntityManagerFactory factory) {
        return factory.createEntityManager();
    }
}
