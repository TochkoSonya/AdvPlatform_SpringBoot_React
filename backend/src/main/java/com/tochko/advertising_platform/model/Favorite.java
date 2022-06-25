package com.tochko.advertising_platform.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="favourites")
public class Favorite implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties(value = {"favorites", "announcements", "comments", "roles"})
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @JsonIgnoreProperties(value = {"favorites", "user", "comments"})
    @ManyToOne
    @JoinColumn(name="announcement_id")
    private Announcement announcement;
}
