package se.munhunger.idp.services;

import se.munhunger.idp.dao.RoleDAO;
import se.munhunger.idp.exception.ClientNotInDatabaseException;
import se.munhunger.idp.exception.RoleNotInDatabaseException;
import se.munhunger.idp.exception.UserNotInDatabaseException;
import se.munhunger.idp.model.persistant.Role;

import javax.inject.Inject;
import java.util.logging.Logger;

public class RoleService {
    private static Logger log = Logger.getLogger(RoleService.class.getName());

    @Inject
    private RoleDAO roleDAO;

    @Inject
    private ClientService clientService;

    public void createRole(Role role, String clientname) throws ClientNotInDatabaseException {
        log.info(() -> "Creating Role: " + role.toString() + " with parent client: " + clientname);
        role.setClientname(clientService.getClient(clientname).getName());
        try {
            role.setUsername(clientService.findUserByClient(clientname).getUsername());
            roleDAO.createRole(role);
            log.info(() -> "Creating Role: " + role.toString() + " with parent Client " + clientname + " Successful");
        } catch (UserNotInDatabaseException e) {
            log.severe(() -> "Error UserNotInDatabaseException Role: " + role.toString() + " with parent Client with clientname " + clientname + ". User for client do not exist in DB");
        }
    }

    public Role getRole(String username, String clientname) throws RoleNotInDatabaseException {
        log.info(() -> "Getting Role for client: " + clientname);
        return roleDAO.getRoleByUserAndClient(username, clientname).orElseThrow(RoleNotInDatabaseException::new);
    }

    public void updateRole(Role role) throws RoleNotInDatabaseException {
        log.info(() -> "Updating Role: " + role.toString());
        roleDAO.updateRole(role);
    }

    public void deleteRole(Long roleId) throws RoleNotInDatabaseException {
        log.info(() -> "Deleting Role: " + roleId);
        roleDAO.deleteRole(roleId);
        log.info(() -> "Deleting Role: " + roleId);
    }
}
