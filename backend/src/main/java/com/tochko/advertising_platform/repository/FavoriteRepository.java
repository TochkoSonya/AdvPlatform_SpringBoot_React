package com.tochko.advertising_platform.repository;

import com.tochko.advertising_platform.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.user.id=?1 AND f.announcement.id=?2")
    void deleteByUserIdAndAnnouncementId(Long userId, Long annId);
}
