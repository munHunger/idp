package se.munhunger.idp.services;

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

    @Inject
    private UserDAO userDAO;

    public void createUser(User user) throws NoSuchAlgorithmException, EmailNotValidException {
        user.setHashPassword(HashPass.hashPassword(user.getHashPassword()));
        if (!EmailValidation.isValidEmailAddress(user.getEmail()))
                throw new EmailNotValidException();
        userDAO.createUser(user);
    }

    public User getUser(String username) throws UserNotInDatabaseException {
        return userDAO.getUser(username).orElseThrow(UserNotInDatabaseException::new);
    }

    public void updateUser(User user) throws NoSuchAlgorithmException, UserNotInDatabaseException, EmailNotValidException {
        user.setHashPassword(HashPass.hashPassword(user.getHashPassword()));
        if (!EmailValidation.isValidEmailAddress(user.getEmail()))
            throw new EmailNotValidException();
        userDAO.updateUser(user);
    }

    public void deleteUser(String username) throws UserNotInDatabaseException, OrphanageException {
        User user = getUser(username);
        if (user.getClients().size() != 0){
            throw new OrphanageException("The user cannot be deleted, it will create client orpahans");
        }
        userDAO.deleteUser(username);
    }
}
