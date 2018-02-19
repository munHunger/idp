package se.munhunger.idp.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.munhunger.idp.dao.ClientDAO;
import se.munhunger.idp.exception.ClientNotInDatabaseException;
import se.munhunger.idp.exception.EmailNotValidException;
import se.munhunger.idp.exception.UserNotInDatabaseException;
import se.munhunger.idp.model.persistant.Client;
import se.munhunger.idp.model.persistant.User;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientService {
    private static Logger log = LogManager.getLogger(ClientService.class.getName());

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private UserService userService;

    public void createClient(Client client, String username) throws UserNotInDatabaseException, EmailNotValidException, NoSuchAlgorithmException {
        log.info(() -> "Creating Client: " + client.toString() + " with parent user: " + username);
        List clientList;
        User user = userService.getUser(username);
        clientList = user.getClients();
        clientList.add(client);
        user.setClients(clientList);
        userService.updateUser(user);
        log.info(() -> "Creating Client: " + client.toString() + " with parent user " + username + " Successful");
    }

    public Client getClient(String clientname) throws ClientNotInDatabaseException {
        log.info(() -> "Getting Client: " + clientname);
        return clientDAO.getClient(clientname).orElseThrow(ClientNotInDatabaseException::new);
    }

    public void updateClient(Client client) throws ClientNotInDatabaseException {
        log.info(() -> "Updating Client: " + client.toString());
        clientDAO.updateClient(client);
    }

    public void deleteClient(String clientname, String username) throws UserNotInDatabaseException, EmailNotValidException, NoSuchAlgorithmException, ClientNotInDatabaseException {
        log.info(() -> "Deleting Client: " + clientname + " for parent user: " + username);
        User user = null;
        user = userService.getUser(username);
        List<Client> filteredClients = user.getClients().stream()
                .filter(c -> !c.getName().equals(clientname))
                .collect(Collectors.toList());
        user.setClients(filteredClients);
        userService.updateUser(user);
        clientDAO.deleteClient(clientname);
        log.info(() -> "Deleting Client: " + clientname + " for parent user: " + username + " Successful");
    }
}
