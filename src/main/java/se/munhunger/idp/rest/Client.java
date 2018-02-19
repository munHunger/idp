package se.munhunger.idp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static java.util.logging.Logger log = java.util.logging.Logger.getLogger(Client.class.getName());

    @Inject
    private ClientService clientService;

    @GET
    @Path("/{clientname}")
    @ApiOperation(value = "Fetches a client from the database")
    @ApiResponse(code = HttpServletResponse.SC_OK, message = "The client identified by the clientname", response = se
            .munhunger.idp.model.persistant.Client.class)
    public Response getClient(@PathParam("clientname") String clientname) {
        log.info(() -> "RestService GET getClient called, with PathParam: " + clientname);
        try {
            return Response.ok(clientService.getClient(clientname)).build();
        } catch (ClientNotInDatabaseException e) {
            log.warning(() -> "Error ClientNotInDatabaseException, could not get Client with clientname: " + clientname + " do not exist in DB");
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
        log.info(() -> "RestService POST createClient called, with PathParam: " + username + " and object: " + client.toString());
        try {
            clientService.createClient(client, username);
        } catch (UserNotInDatabaseException e) {
            log.warning(() -> "Error UserNotInDatabaseException, could not create Client with client: " + client.toString() + " and username: " + username + ". User do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not create client", "Client with clientname: " + client.getName() + " could not be created due to user do not exist"))
                    .build();
        }
        return Response.noContent().build();
    }

    @PUT
    @ApiOperation(value = "Updates a client in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The client was updated")
    public Response updateClient(se.munhunger.idp.model.persistant.Client client) {
        log.info(() -> "RestService PUT updateClient called, with object: " + client.toString());
        try {
            clientService.updateClient(client);
        } catch (ClientNotInDatabaseException e) {
            log.warning(() -> "Error ClientNotInDatabaseException, could not update Client with client: " + client.toString() + " client do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not update client", "Client with clientname: " + client.getName() + " could not be updated"))
                    .build();
        }
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{clientname}/{username}")
    @ApiOperation(value = "Deletes a client in the DB")
    @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "The client was deleted")
    public Response deleteClient(@PathParam("clientname") String clientname,@PathParam("username") String username) {
        log.info(() -> "RestService DELETE deleteClient called, with PathParam: " + clientname + " and Pathparam: " + username);
        try {
            clientService.deleteClient(clientname, username);
        } catch (ClientNotInDatabaseException e) {
            log.warning(() -> "Error ClientNotInDatabaseException, could not delete Client with clientname: " + clientname + " and username: " + username + ". Client do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not delete client", "Client with clientname: " + clientname + " could not be deleted due to the client do not exist"))
                    .build();
        } catch (UserNotInDatabaseException e) {
            log.warning(() -> "Error UserNotInDatabaseException, could not delete Client with clientname: " + clientname + " and username: " + username + ". User do not exist in DB");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Could not delete client", "Client with clientname: " + clientname + " could not be deleted due to user do not exist"))
                    .build();
        }
        return Response.noContent().build();
    }
}
