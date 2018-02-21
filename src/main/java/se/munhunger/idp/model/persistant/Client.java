package se.munhunger.idp.model.persistant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "client")
@ApiModel(description = "A client")
public class Client {
    @Id
    @Column(name = "name", length = 64)
    @ApiModelProperty(value = "The unique identifier for a client")
    private String name;
    @ApiModelProperty(value = "The clients description")
    @Column(name = "description", length = 600)
    private String description;
    @ApiModelProperty(value = "creationdate")
    @Column(name = "creationdate", length = 100, updatable = false, nullable = false)
    @CreationTimestamp
    private Date creationdate;
    public Client() {
    }

    public Client(String name, String description, Date creationdate) {
        this.name = name;
        this.description = description;
        this.creationdate = creationdate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creationdate=" + creationdate +
                '}';
    }
}
