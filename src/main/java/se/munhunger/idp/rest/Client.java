package se.munhunger.idp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import se.munhunger.idp.exception.ClientNotInDatabaseException;
import se.munhunger.idp.exception.EmailNotValidException;
import se.munhunger.idp.exception.UserNotInDatabaseException;
import se.munhunger.idp.model.ErrorMessage;
import se.munhunger.idp.services.ClientService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;

@Api(value = "Client management")
@Path("/client")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Client {

    @Inject
    private ClientService clientService;

    @GET
    @Path("/{clientname}")
    @ApiOperation(value = "Fetches a client from the database")
    @ApiResponse(code = HttpServletResponse.SC_OK, message = "The client identified by the clientname", response = se
            .munhunger.idp.model.persistant.Client.class)
    public Response getClient(@PathParam("clientname") String clientname) {
        try {
            return Response.ok(clientService.getClient(clientname)).build();
        } catch (ClientNotInDatabaseException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Client do not exist",
                            "Client with clientname: " + clientname + " do not exist in DB")).build();
        }
    }

    @POST
    @Path("/{username}")
    @ApiOperation(value = "Creates a new client in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The client was created")
    public Response createClient(@PathParam("username") String username, se.munhunger.idp.model.persistant.Client client) {
        try {
            clientService.createClient(client, username);
        } catch (EmailNotValidException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not update user", "User with email for user: " + username + " is not valid"))
                    .build();
        } catch (NoSuchAlgorithmException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not update user", "Could not process password"))
                    .build();
        } catch (UserNotInDatabaseException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not update user", "User with username: " + username + " does not exist"))
                    .build();
        }
        return Response.noContent().build();
    }

    @PUT
    @ApiOperation(value = "Updates a client in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The client was updated")
    public Response updateClient(se.munhunger.idp.model.persistant.Client client) {
        try {
            clientService.updateClient(client);
        } catch (ClientNotInDatabaseException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not update client", "Client with clientname: " + client.getName() + " could not be updated"))
                    .build();
        }
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{clientname}")
    @ApiOperation(value = "Deletes a user in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The user was deleted")
    public Response deleteUser(@PathParam("clientname") String clientname) {
        try {
            clientService.deleteClient(clientname);
        }  catch (ClientNotInDatabaseException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not delete client", "User with clientname: " + clientname + " does not exist"))
                    .build();
        }
        return Response.noContent().build();
    }
}
