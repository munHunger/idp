package se.munhunger.idp.model.persistant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "role")
@ApiModel(description = "A role")
public class Role {
    @Id
    @ApiModelProperty(value = "Id of a role")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ApiModelProperty(value = "The unique identifier for a client")
    @Column(name = "role")
    private String role;
    @ApiModelProperty(value = "The unique identifier for a client")
    /*@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")*/
    @Column(name = "user_id")
    private String username;
    @ApiModelProperty(value = "The client")
    /*@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clientname")*/
    @Column(name = "client_id")
    private String clientname;
    public Role() {
    }

    public Role(Long id, String role, String username) {
        this.id = id;
        this.role = role;
        this.username = username;
    }

    public Role(Long id, String role, String username, String clientname) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.clientname = clientname;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", clientname='" + clientname + '\'' +
                '}';
    }
}
