package com.tochko.advertising_platform.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tochko.advertising_platform.model.enums.AnnouncementStatus;
import com.tochko.advertising_platform.model.enums.AnnouncementType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "announcements")
public class Announcement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String title;
    private String description;
    private String country;
    private AnnouncementType type;

    @Column(name = "status", columnDefinition = "integer default 0" )
    private AnnouncementStatus status;
    private Double price;
    private Double period;
    private Date createdDate;
    private Date modifiedDate;

    @Basic
    private Byte[] imageData;

    @JsonIgnoreProperties(value = {"announcement"})
    @OneToMany(mappedBy = "announcement", cascade = {CascadeType.REMOVE, CascadeType.DETACH,
            CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @ManyToOne
    @JsonIgnoreProperties(value = {"announcements", "comments", "roles"})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"announcement"})
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"announcement", "user"})
    private List<Rating> rating;
}
