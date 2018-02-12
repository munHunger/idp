package se.munhunger.idp.services;

import se.munhunger.idp.dao.ClientDAO;
import se.munhunger.idp.exception.NotInDatabaseException;
import se.munhunger.idp.model.persistant.Client;

import javax.inject.Inject;

public class ClientService {

    @Inject
    private ClientDAO clientDAO;

    public void createClient(Client client) {
        clientDAO.createClient(client);
    }

    public Client getClient(String clientname) throws NotInDatabaseException {
        return clientDAO.getClient(clientname).orElseThrow(NotInDatabaseException::new);
    }

    public void updateClient(Client client) throws NotInDatabaseException {
        clientDAO.updateClient(client);
    }

    public void deleteClient(String clientname) throws NotInDatabaseException {
        clientDAO.deleteClient(clientname);
    }
}
