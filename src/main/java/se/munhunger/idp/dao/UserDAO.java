package se.munhunger.idp.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import se.munhunger.idp.exception.UserNotInDatabaseException;
import se.munhunger.idp.model.persistant.User;
import se.munhunger.idp.services.ClientService;

import java.util.Optional;

/**
 * @author Marcus Münger
 */
public class UserDAO extends DatabaseDAO {
    private static Logger log = LogManager.getLogger(UserDAO.class.getName());

    public Optional<User> getUser(String username) {
        log.info(() -> "Getting User: " + username);
        try(Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, username);
            if(user == null) {
                log.warn(() -> "Error user with username: " + username + " do not exist");
                return Optional.empty();
            }
            log.info(() -> "Getting User: " + username + " Successful");
            return Optional.of(user);
        }
    }

    public void createUser(User user) {
        try(Session session = sessionFactory.openSession()) {
            log.info(() -> "Creating User: " + user.getUsername());
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            log.info(() -> "Creating User: " + user.getUsername() + " Succcessful");
        }
    }

    public void updateUser(User user) throws UserNotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            log.info(() -> "Updating User: " + user.getUsername());
            Optional<User> updateUser = getUser(user.getUsername());
            updateUser.orElseThrow(UserNotInDatabaseException::new);
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            log.info(() -> "Updating User: " + user.getUsername() + " Successful");
        }
    }

    public void deleteUser(String username) throws UserNotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            log.info(() -> "Deleting User: " + username);
            Optional<User> user = getUser(username);
            session.beginTransaction();
            session.delete(user.orElseThrow(UserNotInDatabaseException::new));
            session.getTransaction().commit();
            log.info(() -> "Updating User: " + username + " Successful");
        }
    }
}
