package com.tochko.advertising_platform.service.impl;

import com.tochko.advertising_platform.controller.details.AnnouncementChartDetails;
import com.tochko.advertising_platform.controller.details.AnnouncementRatingDetails;
import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.Rating;
import com.tochko.advertising_platform.model.enums.AnnouncementStatus;
import com.tochko.advertising_platform.model.enums.AnnouncementType;
import com.tochko.advertising_platform.model.User;
import com.tochko.advertising_platform.repository.AnnouncementRepository;
import com.tochko.advertising_platform.repository.RatingRepository;
import com.tochko.advertising_platform.service.AnnouncementService;
import com.tochko.advertising_platform.service.search_criteries.AnnouncementSearchCriteria;
import com.tochko.advertising_platform.service.search_criteries.specifications.AnnouncementSpecifications;
import com.tochko.advertising_platform.util.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository annRepository;
    private final UserServiceImpl userServiceImpl;
    private final RatingRepository ratingRepository;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementRepository repository,
                                   UserServiceImpl userServiceImpl,
                                   RatingRepository ratingRepository) {
        this.annRepository = repository;
        this.userServiceImpl = userServiceImpl;
        this.ratingRepository = ratingRepository;
    }

    public Announcement get(Long id) {
        return annRepository.findById(id);
    }

    public List<Announcement> getAnnouncementWithCriteria(AnnouncementSearchCriteria searchCriteria) {
        Specification<Announcement> annSpecifications = AnnouncementSpecifications
                .createAnnouncementSpecifications(searchCriteria);
        return annRepository.findAll(annSpecifications);
    }

    public List<AnnouncementChartDetails> getAll() {
        return annRepository.findAll().stream()
                .map(AnnouncementChartDetails::new)
                .collect(Collectors.toList());
    }

    public Page<Announcement> getAnnouncementsByUsername(String username, Pageable pageable) {
        Optional<User> user = userServiceImpl.findByUsername(username);
        return annRepository.findByUser(user.get(), pageable);
    }

    public Page<Announcement> getByType(AnnouncementType type, AnnouncementStatus status, Pageable pageable) {
        if (status == null) {
            return annRepository.findByType(type, pageable);
        } else return annRepository.findByTypeAndStatus(type, status, pageable);

    }

    public Page<Announcement> getByTypeAndUsername(AnnouncementType type,
                                                   AnnouncementStatus status,
                                                   String username,
                                                   Pageable pageable) {
        User user = userServiceImpl.findByUsername(username).get();
        if (status == null) {
            return annRepository.findByTypeAndUser(type, user, pageable);
        } else
            return annRepository.findByTypeAndUserAndStatus(type, user, status, pageable);
    }

    public Announcement add(Announcement announcement) {
        announcement.setCreatedDate(new Date());
        announcement.setModifiedDate(new Date());
        announcement.setStatus(AnnouncementStatus.MODERATION);
        return annRepository.save(announcement);
    }

    public Announcement update(Long id, Announcement announcement) throws ExecutionException, InterruptedException {
        Announcement existingAnn = get(id);

        existingAnn.setTitle(announcement.getTitle());
        existingAnn.setCountry(announcement.getCountry());
        existingAnn.setBrand(announcement.getBrand());
        existingAnn.setPeriod(announcement.getPeriod());
        existingAnn.setPrice(announcement.getPrice());
        existingAnn.setDescription(announcement.getDescription());
        existingAnn.setImageData(announcement.getImageData());
        existingAnn.setModifiedDate(new Date());
        existingAnn.setStatus(AnnouncementStatus.MODERATION);

        return annRepository.save(existingAnn);
    }

    public void delete(long id) throws ExecutionException, InterruptedException {
        Announcement deletedAnn = get(id);
        if (deletedAnn != null) {
            annRepository.delete(deletedAnn);
        }
    }

    public void changeAnnouncementStatus(Long announcementId, AnnouncementStatus status) {
        annRepository.changeStatus(announcementId, status);
    }

    public void addRatingToAnnouncement(Long announcementId, Long userId, int ratingValue) {
        Rating rating = new Rating();
        rating.setAnnouncement(get(announcementId));
        rating.setUser(userServiceImpl.get(userId).get());
        rating.setRating(ratingValue);
        ratingRepository.save(rating);
    }

    public List<AnnouncementRatingDetails> getFirstTenAnnouncementsInRating() {
        List<Object[]> bla = ratingRepository.findByAnnouncements();
        List<AnnouncementRatingDetails> result = new ArrayList<>();
        for (Object[] obj : bla) {
            AnnouncementRatingDetails detail = new AnnouncementRatingDetails();
            Announcement ann = get(Long.valueOf(obj[0].toString()));
            detail.setId(ann.getId());
            detail.setAnnouncement(ann);
            detail.setRating(Float.valueOf(obj[1].toString()));
            result.add(detail);
        }
        return result;
    }

    public void uploadFile(long announcementId, MultipartFile file) throws IOException {
        Announcement updatedAnn = get(announcementId);
        Byte[] bytes = UploadFileUtil.getBytesFromMultipartFile(file);
        if (updatedAnn != null) {
            if (bytes.length == 0) {
                return;
            } else {
                updatedAnn.setImageData(bytes);
            }
            annRepository.save(updatedAnn);
        }
    }
}
