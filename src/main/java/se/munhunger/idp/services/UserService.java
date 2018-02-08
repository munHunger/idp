package se.munhunger.idp.services;

import se.munhunger.idp.util.EmailValidation;
import se.munhunger.idp.util.HashPass;
import se.munhunger.idp.dao.UserDAO;
import se.munhunger.idp.exception.EmailNotValidException;
import se.munhunger.idp.exception.NotInDatabaseException;
import se.munhunger.idp.model.persistant.User;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * @author Marcus Münger
 */
public class UserService {

    @Inject
    private UserDAO userDAO;

    public void createUser(User user) throws NoSuchAlgorithmException, EmailNotValidException {
        user.setHashPassword(HashPass.hashPassword(user.getHashPassword()));
        Optional.ofNullable(!EmailValidation.isValidEmailAddress(user.getEmail())).orElseThrow(EmailNotValidException::new);
        userDAO.createUser(user);
    }

    public User getUser(String username) throws NotInDatabaseException {
        return userDAO.getUser(username).orElseThrow(NotInDatabaseException::new);
    }

    public void updateUser(User user) throws NoSuchAlgorithmException, NotInDatabaseException, EmailNotValidException {
        user.setHashPassword(HashPass.hashPassword(user.getHashPassword()));
        Optional.ofNullable(!EmailValidation.isValidEmailAddress(user.getEmail())).orElseThrow(EmailNotValidException::new);
        userDAO.updateUser(user);
    }

    public void deleteUser(String username) throws NotInDatabaseException {
        userDAO.deleteUser(username);
    }
}
