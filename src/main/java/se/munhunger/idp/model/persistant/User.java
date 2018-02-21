package se.munhunger.idp.model.persistant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcus Münger
 */
@Entity
@Table(name = "user")
@ApiModel(description = "A user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @Id
    @Column(name = "username", length = 64)
    @ApiModelProperty(value = "The unique identifier for a user")
    private String username;
    @ApiModelProperty(value = "A SHA-256 hashed password")
    @Column(name = "password", length = 128)
    private String password;
    @ApiModelProperty(value = "The users firstname")
    @Column(name = "firstname", length = 128)
    private String firstname;
    @ApiModelProperty(value = "The users lastname")
    @Column(name = "lastname", length = 128)
    private String lastname;
    @ApiModelProperty(value = "The users email")
    @Column(name = "email", length = 128)
    private String email;
    @ApiModelProperty(value = "The list of user clients")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_clients",
    joinColumns = {@JoinColumn(name = "user_name")},
    inverseJoinColumns = {@JoinColumn(name = "client_name")})
    private List<Client> clients = new ArrayList<>();
    public User () {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String firstname, String lastname, String email) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", clients=" + clients +
                '}';
    }
}
