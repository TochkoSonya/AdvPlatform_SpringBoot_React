package com.tochko.advertising_platform.controller.details;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.enums.AnnouncementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementChartDetails {

    private Long id;
    private Date createdDate;
    private AnnouncementType type;

    public AnnouncementChartDetails(Announcement announcement) {
        this.id = announcement.getId();
        this.createdDate = announcement.getCreatedDate();
        this.type = announcement.getType();
    }
}
