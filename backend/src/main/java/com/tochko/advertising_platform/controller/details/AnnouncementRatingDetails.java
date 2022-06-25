package com.tochko.advertising_platform.controller.details;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tochko.advertising_platform.model.Announcement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementRatingDetails {

    private Long id;
    @JsonIgnoreProperties(value={"userPlatformDetails", "comments", "imageData", "favorites", "user.imageData"})
    private Announcement announcement;
    private Float rating;
}
