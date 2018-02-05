package se.munhunger.idp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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
            return Response.serverError()
                           .entity(new ErrorMessage("Could not get user",
                                                    "The user with that username was not found in the database"))
                           .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates a new user in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The user was created")
    public Response createUser(se.munhunger.idp.model.persistant.User user) {
        try {
            userService.createUser(user);
        } catch (NoSuchAlgorithmException e) {
            return Response.serverError()
                           .entity(new ErrorMessage("Could not save user",
                                                    "Could not find the correct hashing algorithm")).build();
        }
        return Response.noContent().build();
    }
}
