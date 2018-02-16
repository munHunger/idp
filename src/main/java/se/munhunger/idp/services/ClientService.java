package se.munhunger.idp.services;

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

public class ClientService {

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private UserService userService;

    public void createClient(Client client, String username) throws Exception {
        List clientList;
        User user = null;
        try {
            user = userService.getUser(username);
            clientList = user.getClients();
            clientList.add(client);
            user.setClients(clientList);
            userService.updateUser(user);
        } catch (UserNotInDatabaseException e) {
            throw new UserNotInDatabaseException("The user for client: " + client.getName() + " does not exist, check user");
        } catch (EmailNotValidException e) {
            throw new EmailNotValidException("The user email for client: " + user.getEmail() + " is not valid, check email");
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("The user password for client, was never processed, check password");
        }
    }

    public Client getClient(String clientname) throws ClientNotInDatabaseException {
        return clientDAO.getClient(clientname).orElseThrow(ClientNotInDatabaseException::new);
    }

    public void updateClient(Client client) throws ClientNotInDatabaseException {
        clientDAO.updateClient(client);
    }

    public void deleteClient(String clientname, String username) throws Exception {
        List<Client> oldList;
        List<Client> newList = new ArrayList<>();
        User user = null;
        try {
            user = userService.getUser(username);
            oldList = user.getClients();
            Client client = getClient(clientname);
            for (Client tempClient: oldList) {
                if(!tempClient.getName().equals(client.getName())) {
                    newList.add(tempClient);
                }
            }
            user.setClients(newList);
            userService.updateUser(user);
        } catch (UserNotInDatabaseException e) {
            throw new UserNotInDatabaseException("The user for client: " + clientname + " does not exist, check user");
        } catch (ClientNotInDatabaseException e) {
            throw new ClientNotInDatabaseException("The client with client: " + clientname + " does not exist");
        } catch (EmailNotValidException e) {
            throw new EmailNotValidException("The user email for client: " + user.getEmail() + " is not valid, check email");
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("The user password for client, was never processed, check password");
        }
    }
}
