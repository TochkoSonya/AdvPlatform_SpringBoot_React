package com.tochko.advertising_platform.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rating")
public class Rating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;

    @ManyToOne
    @JsonIgnoreProperties(value = {"rating", "user", "comments"})
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @ManyToOne
    @JsonIgnoreProperties(value = {"rating", "announcements", "comments"})
    @JoinColumn(name = "user_id")
    private User user;
}
