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
            if (!getUser(user.getUsername()).isPresent()){
                throw new NotInDatabaseException("User do not exist",
                        "User with username: " + user.getUsername() + " do not exist in DB");
            }
            User tempUser = getUser(user.getUsername()).get();
            // TODO fråga Marcus om session hanterar updateringar genom att bara passera objektet
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    public void deleteUser(String username) throws NotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            if (!getUser(username).isPresent()){
                throw new NotInDatabaseException("User do not exist",
                        "User with username: " + username + " do not exist in DB");
            }
            session.beginTransaction();
            User tempUser = getUser(username).get();
            session.delete(tempUser);
            session.getTransaction().commit();
        }
    }
}
