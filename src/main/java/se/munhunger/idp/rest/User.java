package se.munhunger.idp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import se.munhunger.idp.exception.EmailNotValidException;
import se.munhunger.idp.exception.OrphanageException;
import se.munhunger.idp.exception.UserNotInDatabaseException;
import se.munhunger.idp.model.ErrorMessage;
import se.munhunger.idp.services.UserService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * @author Marcus Münger
 */
@Api(value = "User management")
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class User {
    private static Logger log = Logger.getLogger(UserService.class.getName());

    @Inject
    private UserService userService;

    @GET
    @Path("/{username}")
    @ApiOperation(value = "Fetches a user from the database")
    @ApiResponse(code = HttpServletResponse.SC_OK, message = "The user identified by the username", response = se
            .munhunger.idp.model.persistant.User.class)
    public Response getUser(@PathParam("username") String username) {
        log.info(() -> "RestService GET getUser called, with PathParam: " + username);
        try {
            return Response.ok(userService.getUser(username)).build();
        } catch (UserNotInDatabaseException e) {
            log.warning(() -> "Error UserNotInDatabaseException, could not get User with username: " + username + " do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("User do not exist",
                            "User with username: " + username + " do not exist in DB")).build();
        }
    }

    @POST
    @ApiOperation(value = "Creates a new user in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The user was created")
    public Response createUser(se.munhunger.idp.model.persistant.User user) {
        log.info(() -> "RestService POST createUser called, with object: " + user.toString());
        try {
            userService.createUser(user);
        } catch (EmailNotValidException e) {
            log.warning(() -> "Error EmailNotValidException, could not create User: " + user.toString() + " has no valid email");
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new ErrorMessage("Could not create user", "User with email: " + user.getEmail() + " is not valid"))
                    .build();
        } catch (NoSuchAlgorithmException e) {
            log.warning(() -> "Error NoSuchAlgorithmException, could not create User: " + user.toString() + " could not process password");
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new ErrorMessage("Could not create user", "Could not process password"))
                    .build();
        }
        return Response.noContent().build();
    }

    @PUT
    @ApiOperation(value = "Updates a user in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The user was updated")
    public Response updateUser(se.munhunger.idp.model.persistant.User user) {
        log.info(() -> "RestService PUT updateUser called, with object: " + user.toString());
        try {
            userService.updateUser(user);
        } catch (EmailNotValidException e) {
            log.warning(() -> "Error EmailNotValidException, could not update User: " + user.toString() + " has no valid email");
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new ErrorMessage("Could not update user", "User with email: " + user.getEmail() + " is not valid"))
                    .build();
        } catch (NoSuchAlgorithmException e) {
            log.warning(() -> "Error NoSuchAlgorithmException, could not update User: " + user.toString() + " could not process password");
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new ErrorMessage("Could not update user", "Could not process password"))
                    .build();
        } catch (UserNotInDatabaseException e) {
            log.warning(() -> "Error UserNotInDatabaseException, could not update User: " + user.toString() + " do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not update user", "User with username: " + user.getUsername() + " does not exist"))
                    .build();
        }
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{username}")
    @ApiOperation(value = "Deletes a user in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The user was deleted")
    public Response deleteUser(@PathParam("username") String username) {
        log.info(() -> "RestService DELETE deleteUser called, with object: " + username);
        try {
            userService.deleteUser(username);
        }  catch (UserNotInDatabaseException e) {
            log.warning(() -> "Error UserNotInDatabaseException, could not delete User: " + username + " do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not delete user", "User with username: " + username + " does not exist"))
                    .build();
        } catch (OrphanageException e) {
            log.warning(() -> "Error UserNotInDatabaseException, could not delete User: " + username + " do not exist in DB");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorMessage("Could not delete user", "User cannot be deleted due to the risk of the user clients being orphans"))
                    .build();
        }
        return Response.noContent().build();
    }
}
