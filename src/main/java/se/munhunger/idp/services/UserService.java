package se.munhunger.idp.services;

import se.munhunger.idp.dao.UserDAO;
import se.munhunger.idp.exception.NotInDatabaseException;
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

    public void createUser(User user) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(user.hashPassword.getBytes(StandardCharsets.UTF_8));
        user.hashPassword = new String(hash);
        userDAO.createUser(user);
    }

    public User getUser(String username) throws NotInDatabaseException {
        return userDAO.getUser(username).orElseThrow(NotInDatabaseException::new);
    }
}
