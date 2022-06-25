package com.tochko.advertising_platform.service.impl;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.Favorite;
import com.tochko.advertising_platform.model.User;
import com.tochko.advertising_platform.repository.AnnouncementRepository;
import com.tochko.advertising_platform.repository.FavoriteRepository;
import com.tochko.advertising_platform.repository.UserRepository;
import com.tochko.advertising_platform.service.FavoriteService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;


    public FavoriteServiceImpl(FavoriteRepository repository,
                               AnnouncementRepository announcementRepository,
                               UserRepository userRepository) {
        this.favoriteRepository = repository;
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
    }

    public void addAnnouncementToFavorite(Long userId, Long annId) {
        Optional<User> user = userRepository.findById(userId);
        Announcement announcement = announcementRepository.findById(annId);

        Favorite favorite = new Favorite();
        favorite.setAnnouncement(announcement);
        favorite.setUser(user.get());

        Example<Favorite> example = Example.of(favorite);

        if (!favoriteRepository.exists(example)) {
            favoriteRepository.save(favorite);
        }
    }

    public Page<Announcement> findFavoriteAnnouncementsByUserId(Long userId, Pageable pageable) {
        return announcementRepository.findFavoriteAnnouncementsByUserId(userId, pageable);
    }

    public void delete(Long userId, Long annId) {
        favoriteRepository.deleteByUserIdAndAnnouncementId(userId, annId);
    }
}
