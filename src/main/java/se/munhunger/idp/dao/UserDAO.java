package se.munhunger.idp.dao;

import org.hibernate.Session;
import se.munhunger.idp.exception.UserNotInDatabaseException;
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

    public void updateUser(User user) throws UserNotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            Optional<User> updateUser = getUser(user.getUsername());
            updateUser.orElseThrow(UserNotInDatabaseException::new);
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    public void deleteUser(String username) throws UserNotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            Optional<User> user = getUser(username);
            session.beginTransaction();
            session.delete(user.orElseThrow(UserNotInDatabaseException::new));
            session.getTransaction().commit();
        }
    }
}
