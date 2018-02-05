package se.munhunger.idp.dao;

import org.hibernate.Session;
import se.munhunger.idp.model.persistant.User;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author Marcus Münger
 */
public class UserDAO extends DatabaseDAO {

    private static Logger logger = Logger.getLogger(UserDAO.class.getName());

    public Optional<User> getUser(String username) {
        try(Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, username);
            if(user == null)
            {
                logger.fine(() -> "Could not find user with name:" + username);
                return Optional.empty();
            }
            return Optional.of(user);
        }
    }

    public void createUser(User user) {
        logger.fine(() -> "Creating user:" + user.username);
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }
}
