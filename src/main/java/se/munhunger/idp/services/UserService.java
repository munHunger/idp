package se.munhunger.idp.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.munhunger.idp.dao.UserDAO;
import se.munhunger.idp.exception.EmailNotValidException;
import se.munhunger.idp.exception.OrphanageException;
import se.munhunger.idp.exception.UserNotInDatabaseException;
import se.munhunger.idp.model.persistant.User;
import se.munhunger.idp.util.EmailValidation;
import se.munhunger.idp.util.HashPass;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;



/**
 * @author Marcus Münger
 */
public class UserService {
    final static Logger log = LogManager.getLogger(UserService.class.getName());

    @Inject
    private UserDAO userDAO;

    public void createUser(User user) throws NoSuchAlgorithmException, EmailNotValidException {
        log.info(() -> "Creating User: " + user.toString());
        user.setHashPassword(HashPass.hashPassword(user.getHashPassword()));
        if (!EmailValidation.isValidEmailAddress(user.getEmail())) {
            log.error(() -> "Error could not create User due to invalid email: " + user.getEmail());
            throw new EmailNotValidException();
        }
        userDAO.createUser(user);
        log.info(() -> "Creating User Successful with User: " + user.toString());
    }

    public User getUser(String username) throws UserNotInDatabaseException {
        log.info(() -> "Getting user: " + username);
        return userDAO.getUser(username).orElseThrow(UserNotInDatabaseException::new);
    }

    public void updateUser(User user) throws NoSuchAlgorithmException, UserNotInDatabaseException, EmailNotValidException {
        log.info(() -> "Updating user: " + user.getUsername());
        user.setHashPassword(HashPass.hashPassword(user.getHashPassword()));
        if (!EmailValidation.isValidEmailAddress(user.getEmail())) {
            log.error(() -> "Error could not create User due to invalid email: " + user.getEmail());
            throw new EmailNotValidException();
        }
        userDAO.updateUser(user);
        log.info(() -> "Updating user: " + user.getUsername() + " Successful");
    }

    public void deleteUser(String username) throws UserNotInDatabaseException, OrphanageException {
        log.info(() -> "Deleting user: " + username);
        User user = getUser(username);
        if (user.getClients().size() != 0){
            log.error(() -> "Error could not create User due to OrphanException: " + user.getEmail());
            throw new OrphanageException();
        }
        userDAO.deleteUser(username);
        log.info(() -> "Deleting user: " + username + " Successful");
    }
}
