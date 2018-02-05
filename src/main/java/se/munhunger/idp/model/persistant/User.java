package se.munhunger.idp.model.persistant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
    @Column(name = "username", length = 64)
    @ApiModelProperty(value = "The unique identifier for a user")
    public String username;

    @Column(name = "password", length = 128)
    private String hashPassword;

    @JsonIgnore
    public String getHashPassword()
    {
        return hashPassword;
    }

    @JsonProperty("password")
    public void setHashPassword(String hashPassword)
    {
        this.hashPassword = hashPassword;
    }
}
