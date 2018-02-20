package se.munhunger.idp.services;

import se.munhunger.idp.dao.ClientDAO;
import se.munhunger.idp.exception.ClientNotInDatabaseException;
import se.munhunger.idp.exception.EmailNotValidException;
import se.munhunger.idp.exception.UserNotInDatabaseException;
import se.munhunger.idp.model.persistant.Client;
import se.munhunger.idp.model.persistant.User;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

public class ClientService {
    private static java.util.logging.Logger log = java.util.logging.Logger.getLogger(ClientService.class.getName());

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private UserService userService;

    public void createClient(Client client, String username) throws UserNotInDatabaseException {
        log.info(() -> "Creating Client: " + client.toString() + " with parent user: " + username);
        User user = userService.getUser(username);
        List<Client> clientList = user.getClients();
        clientList.add(client);
        user.setClients(clientList);
        log.info(() -> "Creating Client: " + client.toString() + " with parent user " + username + " Successful");
        try {
            userService.updateUser(user);
        } catch (NoSuchAlgorithmException e) {
            log.severe(() -> "Error NoSuchAlgorithmException Client: " + client.toString() + " with parent user " + username + ". Password could not be processed");
        } catch (EmailNotValidException e) {
            log.severe(() -> "Error EmailNotValidException Client: " + client.toString() + " with parent user " + username + ". Email is not valid");
        }
    }

    public Client getClient(String clientname) throws ClientNotInDatabaseException {
        log.info(() -> "Getting Client: " + clientname);
        return clientDAO.getClient(clientname).orElseThrow(ClientNotInDatabaseException::new);
    }

    public void updateClient(Client client) throws ClientNotInDatabaseException {
        log.info(() -> "Updating Client: " + client.toString());
        clientDAO.updateClient(client);
    }

    public void deleteClient(String clientname, String username) throws UserNotInDatabaseException, ClientNotInDatabaseException {
        log.info(() -> "Deleting Client: " + clientname + " for parent user: " + username);
        User user = userService.getUser(username);
        List<Client> filteredClients = user.getClients().stream()
                .filter(c -> !c.getName().equals(clientname))
                .collect(Collectors.toList());
        user.setClients(filteredClients);
        try {
            userService.updateUser(user);
        } catch (NoSuchAlgorithmException e) {
            log.severe(() -> "Error NoSuchAlgorithmException Client: " + clientname + " with parent user " + username + ". Password could not be processed");
        } catch (EmailNotValidException e) {
            log.severe(() -> "Error EmailNotValidException Client: " + clientname + " with parent user " + username + ". Email is not valid");
        }
        clientDAO.deleteClient(clientname);
        log.info(() -> "Deleting Client: " + clientname + " for parent user: " + username + " Successful");
    }
}
