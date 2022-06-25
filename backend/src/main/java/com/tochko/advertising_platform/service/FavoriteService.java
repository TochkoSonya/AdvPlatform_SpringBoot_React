package com.tochko.advertising_platform.service;

import com.tochko.advertising_platform.model.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {

    void addAnnouncementToFavorite(Long userId, Long annId);

    Page<Announcement> findFavoriteAnnouncementsByUserId(Long userId, Pageable pageable);

    void delete(Long userId, Long annId);
}
