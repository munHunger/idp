package se.munhunger.idp.dao;

import org.hibernate.Session;
import se.munhunger.idp.exception.ClientNotInDatabaseException;
import se.munhunger.idp.model.persistant.Client;

import java.util.Optional;
import java.util.logging.Logger;

public class ClientDAO extends DatabaseDAO {
    private static Logger log = Logger.getLogger(ClientDAO.class.getName());

    public Optional<Client> getClient(String clientname) {
        try(Session session = sessionFactory.openSession()) {
            log.info(() -> "Getting Client: " + clientname);
            Client client = session.get(Client.class, clientname);
            if(client == null) {
                log.warning(() -> "Error client with clientname: " + clientname + " do not exist");
                return Optional.empty();
            }
            log.info(() -> "Getting Client: " + clientname + " Successful");
            return Optional.of(client);
        }
    }

    public void createClient(Client client) {
        try(Session session = sessionFactory.openSession()) {
            log.info(() -> "Creating Client: " + client.toString());
            session.beginTransaction();
            session.save(client);
            session.getTransaction().commit();
            log.info(() -> "Creating Client: " + client.toString() + " Successful");
        }
    }

    public void updateClient(Client client) throws ClientNotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            log.info(() -> "Updating Client: " + client.toString());
            Optional<Client> updateClient = getClient(client.getName());
            updateClient.orElseThrow(ClientNotInDatabaseException::new);
            session.beginTransaction();
            session.update(client);
            session.getTransaction().commit();
            log.info(() -> "Updating Client: " + client.toString() + " Successful");
        }
    }

    public void deleteClient(String clientname) throws ClientNotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            log.info(() -> "Deleting Client: " + clientname);
            Optional<Client> client = getClient(clientname);
            session.beginTransaction();
            session.delete(client.orElseThrow(ClientNotInDatabaseException::new));
            session.getTransaction().commit();
            log.info(() -> "Deleting Client: " + clientname + " Successful");
        }
    }
}