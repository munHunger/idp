package se.munhunger.idp.dao;

import org.hibernate.Session;
import se.munhunger.idp.exception.NotInDatabaseException;
import se.munhunger.idp.model.persistant.Client;

import java.util.Optional;

public class ClientDAO extends DatabaseDAO {

    public Optional<Client> getClient(String clientname) {
        try(Session session = sessionFactory.openSession()) {
            Client client = session.get(Client.class, clientname);
            if(client == null)
                return Optional.empty();
            return Optional.of(client);
        }
    }

    public void createClient(Client client) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(client);
            session.getTransaction().commit();
        }
    }

    public void updateClient(Client client) throws NotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            Optional.ofNullable(getClient(client.getName())).orElseThrow(NotInDatabaseException::new);
            session.beginTransaction();
            session.update(client);
            session.getTransaction().commit();
        }
    }

    public void deleteClient(String clientname) throws NotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            Optional<Client> client = getClient(clientname);
            session.beginTransaction();
            session.delete(client.orElseThrow(NotInDatabaseException::new));
            session.getTransaction().commit();
        }
    }
}
