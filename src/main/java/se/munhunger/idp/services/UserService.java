package se.munhunger.idp.services;

import se.munhunger.idp.Util.EmailValidation;
import se.munhunger.idp.dao.UserDAO;
import se.munhunger.idp.exception.NotInDatabaseException;
import se.munhunger.idp.model.ErrorMessage;
import se.munhunger.idp.model.persistant.User;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Marcus Münger
 */
public class UserService {

    @Inject
    private UserDAO userDAO;

    public void createUser(User user) throws NoSuchAlgorithmException, ErrorMessage {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(user.getHashPassword().getBytes(StandardCharsets.UTF_8));
        user.setHashPassword(new String(hash));
        if (!EmailValidation.isValidEmailAddress(user.getEmail())) {
            throw new ErrorMessage("No valid email", "Email: " + user.getEmail() + " is not valid");
        }
        userDAO.createUser(user);
    }

    public User getUser(String username) throws NotInDatabaseException {
        return userDAO.getUser(username).orElseThrow(NotInDatabaseException::new);
    }

    public void updateUser(User user) throws NoSuchAlgorithmException, NotInDatabaseException, ErrorMessage {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(user.getHashPassword().getBytes(StandardCharsets.UTF_8));
        user.setHashPassword(new String(hash));
        if (!EmailValidation.isValidEmailAddress(user.getEmail())) {
            throw new ErrorMessage("No valid email", "Email: " + user.getEmail() + " is not valid");
        }
        userDAO.updateUser(user);
    }

    public void deleteUser(String username) throws NotInDatabaseException {
        userDAO.deleteUser(username);
    }


}
