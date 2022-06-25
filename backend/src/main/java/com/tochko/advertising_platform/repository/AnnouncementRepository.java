package com.tochko.advertising_platform.repository;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.enums.AnnouncementStatus;
import com.tochko.advertising_platform.model.enums.AnnouncementType;
import com.tochko.advertising_platform.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer>, JpaSpecificationExecutor<Announcement> {

    Announcement findById(Long id);

    Page<Announcement> findAll(Pageable pageable);

    @Query("SELECT ann FROM Announcement ann WHERE ann.type=?1")
    Page<Announcement> findByType(AnnouncementType type, Pageable pageable);

    Page<Announcement> findByTypeAndStatus(AnnouncementType type, AnnouncementStatus status, Pageable pageable);

    Page<Announcement> findByUser(User user, Pageable pageable);

    List<Announcement> findByType(AnnouncementType type);

    List<Announcement> findByTypeAndUser(AnnouncementType type, User user);

    Page<Announcement> findByTypeAndUser(AnnouncementType type, User user, Pageable pageable);

    Page<Announcement> findByTypeAndUserAndStatus(AnnouncementType type, User user, AnnouncementStatus status, Pageable pageable);

    Page<Announcement> findAll(Specification<Announcement> spec, Pageable pageable);

    List<Announcement> findAll(Specification<Announcement> spec);

    @Query("SELECT ann FROM Announcement ann INNER JOIN Favorite f " +
            "ON ann.id = f.announcement.id " +
            "WHERE f.user.id=?1")
    Page<Announcement> findFavoriteAnnouncementsByUserId(Long userId, Pageable pageable);

    @Modifying
    @Query("UPDATE Announcement ann SET ann.status=?2 WHERE ann.id=?1")
    void changeStatus(Long announcementId, AnnouncementStatus status);
}
