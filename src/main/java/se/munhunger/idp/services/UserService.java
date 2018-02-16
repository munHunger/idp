package se.munhunger.idp.services;

import se.munhunger.idp.util.EmailValidation;
import se.munhunger.idp.util.HashPass;
import se.munhunger.idp.dao.UserDAO;
import se.munhunger.idp.exception.EmailNotValidException;
import se.munhunger.idp.exception.NotInDatabaseException;
import se.munhunger.idp.model.persistant.User;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * @author Marcus Münger
 */
public class UserService {

    private static Logger logger = Logger.getLogger(UserService.class.getName());

    @Inject
    private UserDAO userDAO;

    public void createUser(User user) throws NoSuchAlgorithmException, EmailNotValidException {
        logger.fine("Creating user: " + user.getUsername());
        user.setPassword(HashPass.hashPassword(user.getPassword()));
        if (!EmailValidation.isValidEmailAddress(user.getEmail()))
                throw new EmailNotValidException();
        userDAO.createUser(user);
    }

    public User getUser(String username) throws NotInDatabaseException {
        return userDAO.getUser(username).orElseThrow(NotInDatabaseException::new);
    }

    public void updateUser(User user) throws NoSuchAlgorithmException, NotInDatabaseException, EmailNotValidException {
        user.setPassword(HashPass.hashPassword(user.getPassword()));
        if (!EmailValidation.isValidEmailAddress(user.getEmail()))
            throw new EmailNotValidException();
        userDAO.updateUser(user);
    }

    public void deleteUser(String username) throws NotInDatabaseException {
        userDAO.deleteUser(username);
    }
}
