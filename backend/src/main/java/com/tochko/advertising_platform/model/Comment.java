package com.tochko.advertising_platform.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tochko.advertising_platform.model.enums.CommentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private Date createdDate = new Date();
    private Date modifiedDate = new Date();
    @Column(name = "status", columnDefinition = "integer default 0")
    private CommentStatus status;

    @JsonIgnoreProperties(value = {"user", "comments"})
    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @JsonIgnoreProperties(value = {"announcements", "comments"})
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;
}
