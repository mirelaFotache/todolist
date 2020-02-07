package todo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import todo.repository.models.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;

public class CustomRepoImpl implements CustomRepo {

    private final JdbcTemplate jdbcTemplate;

    private final EntityManager entityManager;

    @Autowired
    public CustomRepoImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Override
    public Collection<User> findAll(BaseFilter filter) {
        //TODO Spring-data suporta query-uri cu numar variabil de parametri
        // sau parametri care pot fi dati sau sa lipseasca din endpoint
        // De vazut si cum se face un integration test pt metoda din CustomRepoImpl...
        //varianta mai naspa
       /* jdbcTemplate.query("", () -> {
            return null;
        });*/

        final Query query = entityManager.createQuery("");
        //

        return null;
    }

    private String createQueryString(BaseFilter f) {
        // create query
        return null;
    }
}
