package com.tochko.advertising_platform.service;

import com.tochko.advertising_platform.controller.details.AnnouncementChartDetails;
import com.tochko.advertising_platform.controller.details.AnnouncementRatingDetails;
import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.enums.AnnouncementStatus;
import com.tochko.advertising_platform.model.enums.AnnouncementType;
import com.tochko.advertising_platform.service.search_criteries.AnnouncementSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AnnouncementService {

    Announcement get(Long id);

    List<AnnouncementChartDetails> getAll();

    List<Announcement> getAnnouncementWithCriteria(AnnouncementSearchCriteria searchCriteria);

    Page<Announcement> getAnnouncementsByUsername(String username, Pageable pageable);

    Page<Announcement> getByType(AnnouncementType type, AnnouncementStatus status, Pageable pageable);

    Page<Announcement> getByTypeAndUsername(AnnouncementType type, AnnouncementStatus status, String username, Pageable pageable);

    Announcement add(Announcement announcement);

    Announcement update(Long id, Announcement announcement) throws ExecutionException, InterruptedException;

    void delete(long id) throws ExecutionException, InterruptedException;

    void changeAnnouncementStatus(Long announcementId, AnnouncementStatus status);

    void uploadFile(long announcementId, MultipartFile file) throws IOException;

    void addRatingToAnnouncement(Long announcementId, Long userId, int rating);

    List<AnnouncementRatingDetails> getFirstTenAnnouncementsInRating();
}
