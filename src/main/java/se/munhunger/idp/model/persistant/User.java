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

    public User () {

    }

    public User(String username, String hashPassword) {
        this.username = username;
        this.hashPassword = hashPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                '}';
    }
}
