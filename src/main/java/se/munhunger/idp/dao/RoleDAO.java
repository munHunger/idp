package se.munhunger.idp.dao;

import org.hibernate.Session;
import se.munhunger.idp.exception.RoleNotInDatabaseException;
import se.munhunger.idp.model.persistant.Role;

import java.util.Optional;
import java.util.logging.Logger;

public class RoleDAO extends DatabaseDAO {
    private static Logger log = Logger.getLogger(RoleDAO.class.getName());

    public Optional<Role> getRole(Long id) {
        log.info(() -> "Getting Role: " + id);
        try(Session session = sessionFactory.openSession()) {
            Role role = session.get(Role.class, id);
            if(role == null) {
                log.warning(() -> "Error role with id: " + id + " do not exist");
                return Optional.empty();
            }
            log.info(() -> "Getting Role: " + role.toString() + " Successful");
            return Optional.of(role);
        }
    }

    public void createRole(Role role) {
        try(Session session = sessionFactory.openSession()) {
            log.info(() -> "Creating Role: " + role.toString());
            session.beginTransaction();
            session.save(role);
            session.getTransaction().commit();
            log.info(() -> "Creating Role: " + role.toString() + " Succcessful");
        }
    }

    public void updateRole(Role role) throws RoleNotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            log.info(() -> "Updating Role: " + role.toString());
            Optional<Role> updateRole = getRole(role.getId());
            updateRole.orElseThrow(RoleNotInDatabaseException::new);
            session.beginTransaction();
            session.update(role);
            session.getTransaction().commit();
            log.info(() -> "Updating Role: " + role.toString() + " Successful");
        }
    }

    public void deleteRole(Long roleid) throws RoleNotInDatabaseException {
        try(Session session = sessionFactory.openSession()) {
            log.info(() -> "Deleting Role with id: " + roleid);
            Optional<Role> role = getRole(roleid);
            session.beginTransaction();
            session.delete(role.orElseThrow(RoleNotInDatabaseException::new));
            session.getTransaction().commit();
            log.info(() -> "Updating Role with id: " + roleid + " Successful");
        }
    }
}
