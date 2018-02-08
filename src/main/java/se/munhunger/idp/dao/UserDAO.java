package se.munhunger.idp.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import se.munhunger.idp.exception.NotInDatabaseException;
import se.munhunger.idp.model.ErrorMessage;
import se.munhunger.idp.model.persistant.User;

import java.util.Optional;

/**
 * @author Marcus Münger
 */
public class UserDAO extends DatabaseDAO {

    public Optional<User> getUser(String username) {
        try(Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, username);
            if(user == null)
                return Optional.empty();
            return Optional.of(user);
        }
    }

    public void createUser(User user) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    public void updateUser(User user) throws NotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            Optional.ofNullable(getUser(user.getUsername()).isPresent()).orElseThrow(NotInDatabaseException::new);
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    public void deleteUser(String username) throws NotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            Optional.ofNullable(getUser(username).isPresent()).orElseThrow(NotInDatabaseException::new);
            session.beginTransaction();
            User tempUser = getUser(username).get();
            session.delete(tempUser);
            session.getTransaction().commit();
        }
    }
}
