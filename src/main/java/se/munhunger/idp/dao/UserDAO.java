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
            Optional<User> tempUser = getUser(user.getUsername());
            tempUser.orElseThrow(NotInDatabaseException::new);
            session.merge(tempUser.get());
            session.getTransaction().commit();
        }
    }

    public void deleteUser(String username) throws NotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            Optional<User> tempUser = getUser(username);
            tempUser.orElseThrow(NotInDatabaseException::new);
            session.beginTransaction();
            session.delete(tempUser.get());
            session.getTransaction().commit();
        }
    }
}
