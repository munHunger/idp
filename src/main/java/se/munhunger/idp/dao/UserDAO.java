package se.munhunger.idp.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
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

    public void updateUser(User user) throws ErrorMessage {
        try(Session session = sessionFactory.openSession()) {
            User tempUser = session.get(User.class, user.getUsername());
            System.out.println(tempUser.getUsername());
            if (tempUser.getUsername() == null || tempUser.getUsername() == ""){
                throw new ErrorMessage("No such user", "User with user name: " + user.getUsername() + " does not exist");
            }
            // TODO fråga Marcus om session hanterar updateringar genom att bara passera objektet
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    public void deleteUser(String username) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User tempUser = session.get(User.class, username);
            session.delete(tempUser);
            session.getTransaction().commit();
        }
    }
}
