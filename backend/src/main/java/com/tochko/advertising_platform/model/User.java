package com.tochko.advertising_platform.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tochko.advertising_platform.model.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String username;
    private String password;
    private String email;
    private String link;
    private Date createdDate = new Date();
    @Basic
    private Byte[] imageData;

    @Column(name = "status", columnDefinition = "integer default 0")
    private UserStatus status;


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"users"})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @JsonIgnoreProperties(value = {"user"})
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @JsonIgnoreProperties(value = {"user"})
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.DETACH,
            CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Announcement> announcements;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"user"})
    private List<Favorite> favorites;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id", referencedColumnName = "id")
    private UserPlatformDetails userPlatformDetails;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"user", "announcement"})
    private List<Rating> rating;
}
