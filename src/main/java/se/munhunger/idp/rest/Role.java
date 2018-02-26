package se.munhunger.idp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import se.munhunger.idp.exception.ClientNotInDatabaseException;
import se.munhunger.idp.exception.RoleNotInDatabaseException;
import se.munhunger.idp.model.ErrorMessage;
import se.munhunger.idp.services.RoleService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Api(value = "Role management")
@Path("/role")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Role {
    private static Logger log = Logger.getLogger(Role.class.getName());

    @Inject
    private RoleService roleService;

    @GET
    @Path("/{username}/{clientname}")
    @ApiOperation(value = "Fetches a role for a specific client from the database")
    @ApiResponse(code = HttpServletResponse.SC_OK, message = "The role identified by the roleId", response = se
            .munhunger.idp.model.persistant.Role.class)
    public Response getRole(@PathParam("username") String username, @PathParam("clientname") String clientname) {
        log.info(() -> "RestService GET getRole called, with PathParam clientname: " + clientname);
        try {
            return Response.ok(roleService.getRole(username, clientname)).build();
        } catch (RoleNotInDatabaseException e) {
            log.warning(() -> "Error RoleNotInDatabaseException, could not get Role with clientname: " + clientname + " do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Role do not exist",
                            "Role with clientname: " + clientname + " do not exist in DB")).build();
        }
    }

    @POST
    @Path("/{clientname}")
    @ApiOperation(value = "Creates a new Role in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The Role was created")
    public Response createRole(@PathParam("clientname") String clientname, se.munhunger.idp.model.persistant.Role role) {
        log.info(() -> "RestService POST createRole called, with PathParam: " + clientname + " and object: " + role.toString());
        try {
            roleService.createRole(role, clientname);
        } catch (ClientNotInDatabaseException e) {
            log.warning(() -> "Error ClientNotInDatabaseException, could not create Role with role: " + role.toString() + " and clientname: " + clientname + ". Client do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not create Role", "Role: " + role.toString() + " could not be created due to client do not exist"))
                    .build();
        }
        return Response.noContent().build();
    }

    @PUT
    @ApiOperation(value = "Updates a Role in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The role was updated")
    public Response updateRole(se.munhunger.idp.model.persistant.Role role) {
        log.info(() -> "RestService PUT updateRole called, with object: " + role.toString());
        try {
            roleService.updateRole(role);
        } catch (RoleNotInDatabaseException e) {
            log.warning(() -> "Error RoleNotInDatabaseException, could not update Role: " + role.toString() + " role do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not update role", "Role: " + role.toString() + " could not be updated"))
                    .build();
        }
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{roleId}")
    @ApiOperation(value = "Deletes a Role in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The role was deleted")
    public Response deleteRole(@PathParam("roleId") Long roleId) {
        log.info(() -> "RestService DELETE deleteClient called, with PathParam: " + roleId);
        try {
            roleService.deleteRole(roleId);
        } catch (RoleNotInDatabaseException e) {
            log.warning(() -> "Error RoleNotInDatabaseException, could not delete Role with roleId: " + roleId + ". Role do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not delete role", "Role with roleId: " + roleId + " could not be deleted due to the role do not exist"))
                    .build();
        }
        return Response.noContent().build();
    }
}