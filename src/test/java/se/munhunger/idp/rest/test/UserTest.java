package se.munhunger.idp.rest.test;

import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import se.munhunger.idp.model.persistant.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(OleasterRunner.class)
public class UserTest
{
    private String baseURL = (System.getenv().get("IDP_URL") != null ? System.getenv().get("IDP_URL") : "http://localhost:81") + "/idp";
    private Client client;

    {
        before(() -> {
            System.out.println("Starting test on base url: " + baseURL);
            client = ClientBuilder.newClient();
        });
        describe("System is up and running", () -> {
            it("can get a 200 from swagger", () -> {
                Assert.assertEquals(200,
                                    client.target(baseURL + "/swagger")
                                          .request()
                                          .get()
                                          .getStatus());
            });
            describe("Creating a user", () -> {
                it("returns 204 upon creation", () -> {
                    Map<String, String> user = new HashMap<>();
                    user.put("username", "username");
                    user.put("password", "password");
                    user.put("email", "mail@mail.com");
                    Response response = client.target(baseURL + "/api/user")
                                              .request()
                                              .header("accept", "application/json")
                                              .header("content-type", "application/json")
                                              .post(Entity.json(user));
                    System.out.println("Reading response");
                    System.out.println(response.readEntity(String.class));
                    Assert.assertEquals(204, response.getStatus());
                });
                it("returns an error if the email is invalid", () -> {
                    Map<String, String> user = new HashMap<>();
                    user.put("username", "username");
                    user.put("password", "password");
                    user.put("email", "mail.com");
                    Assert.assertEquals(500,
                                        client.target(baseURL + "/api/user")
                                              .request()
                                              .post(Entity.json(user))
                                              .getStatus());
                });
                afterEach(() -> {
                    client.target(baseURL + "/api/user/username").request().delete();
                });
            });
        });
    }
}
