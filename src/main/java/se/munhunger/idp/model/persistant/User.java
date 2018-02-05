package se.munhunger.idp.model.persistant;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ApiModelProperty(value = "A SHA-256 hashed password")
    @Column(name = "password", length = 128)
    @JsonIgnore
    public String hashPassword;
}
