package se.munhunger.idp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import se.munhunger.idp.exception.EmailNotValidException;
import se.munhunger.idp.exception.NotInDatabaseException;
import se.munhunger.idp.model.ErrorMessage;
import se.munhunger.idp.services.UserService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;

/**
 * @author Marcus Münger
 */
@Api(value = "User management")
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class User {

    @Inject
    private UserService userService;

    @GET
    @Path("/{username}")
    @ApiOperation(value = "Fetches a user from the database")
    @ApiResponse(code = HttpServletResponse.SC_OK, message = "The user identified by the username", response = se
            .munhunger.idp.model.persistant.User.class)
    public Response getUser(@PathParam("username") String username) {
        try {
            return Response.ok(userService.getUser(username)).build();
        } catch (NotInDatabaseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("User do not exist",
                            "User with username: " + username + " do not exist in DB")).build();
        }
    }

    @POST
    @ApiOperation(value = "Creates a new user in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The user was created")
    public Response createUser(se.munhunger.idp.model.persistant.User user) {
        try {
            userService.createUser(user);
        } catch (EmailNotValidException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new ErrorMessage("Could not create user", "User with email: " + user.getEmail() + " is not valid"))
                    .build();
        } catch (NoSuchAlgorithmException e) {
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
        try {
            userService.updateUser(user);
        } catch (EmailNotValidException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new ErrorMessage("Could not update user", "User with email: " + user.getEmail() + " is not valid"))
                    .build();
        } catch (NoSuchAlgorithmException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new ErrorMessage("Could not update user", "Could not process password"))
                    .build();
        } catch (NotInDatabaseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
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
        try {
            userService.deleteUser(username);
        }  catch (NotInDatabaseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Could not delete user", "User with username: " + username + " does not exist"))
                    .build();
        }
        return Response.noContent().build();
    }
}
