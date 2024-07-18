package com.example.finalProject_synrgy.entity.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

@Entity()
@Table(
        name = "oauth_role",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "role_name_and_type",
                        columnNames = {"type", "name"}
                )
        }
)
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(length = 20)
    private String name;

    @JsonIgnore
    private String type;

    @JsonIgnore
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RolePath> rolePaths;

    @JsonIgnore
    @ManyToMany(targetEntity = User.class, mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return this.name;
    }

    public List<RolePath> getRolePaths() {
        return rolePaths;
    }

    public void setRolePaths(List<RolePath> rolePaths) {
        this.rolePaths = rolePaths;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
