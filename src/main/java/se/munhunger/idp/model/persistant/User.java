package se.munhunger.idp.model.persistant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Marcus Münger
 */
@Entity
@Table(name = "user")
@ApiModel(description = "A user")
public class User {
    @Id
    @Cascade(CascadeType.MERGE)
    @Column(name = "username", length = 64)
    @ApiModelProperty(value = "The unique identifier for a user")
    private String username;
    @ApiModelProperty(value = "A SHA-256 hashed password")
    @Column(name = "password", length = 128)
    private String hashPassword;
    @ApiModelProperty(value = "The users firstname")
    @Column(name = "firstname", length = 128)
    private String firstname;
    @ApiModelProperty(value = "The users lastname")
    @Column(name = "lastname", length = 128)
    private String lastname;
    @ApiModelProperty(value = "The users email")
    @Column(name = "email", length = 128)
    private String email;
    public User () {

    }

    public User(String username, String hashPassword) {
        this.username = username;
        this.hashPassword = hashPassword;
    }

    public User(String username, String hashPassword, String firstname, String lastname, String email) {
        this.username = username;
        this.hashPassword = hashPassword;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
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

}
