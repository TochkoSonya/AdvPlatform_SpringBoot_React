package com.tochko.advertising_platform.service.search_criteries.specifications;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.service.search_criteries.AnnouncementSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class AnnouncementSpecifications {

    public static Specification<Announcement> createAnnouncementSpecifications(AnnouncementSearchCriteria searchCriteria) {
        return brandLike(searchCriteria.getBrand())
                .and(countryLike(searchCriteria.getCountry())
                        .and(periodBetween(searchCriteria.getPeriodValue1(), searchCriteria.getPeriodValue2()))
                        .and(priceBetween(searchCriteria.getPriceValue1(), searchCriteria.getPriceValue2())))
                .and(followersBetween(searchCriteria.getFollowersValue1(), searchCriteria.getFollowersValue2()));
    }

    static Specification<Announcement> brandLike(Optional<String> searchTerm) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);
            return cb.like(cb.lower(root.<String>get("brand")), containsLikePattern);
        };
    }

    static Specification<Announcement> countryLike(Optional<String> searchTerm) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);
            return cb.like(cb.lower(root.<String>get("country")), containsLikePattern);
        };
    }

    static Specification<Announcement> priceBetween(Optional<String> searchTerm1, Optional<String> searchTerm2) {
        if (!searchTerm1.get().isBlank() && !searchTerm2.get().isBlank()) {
            return (root, query, cb) -> cb.between(root.<Long>get("price"), Long.valueOf(searchTerm1.get()), Long.valueOf(searchTerm2.get()));
        } else return null;
    }

    static Specification<Announcement> periodBetween(Optional<String> searchTerm1, Optional<String> searchTerm2) {
        if (!searchTerm1.get().isBlank() && !searchTerm2.get().isBlank()) {
            return (root, query, cb) -> cb.between(root.<Long>get("period"), Long.valueOf(searchTerm1.get()), Long.valueOf(searchTerm2.get()));
        } else return null;
    }

    static Specification<Announcement> followersBetween(Optional<String> searchTerm1, Optional<String> searchTerm2) {
        if (!searchTerm1.get().isBlank() && !searchTerm2.get().isBlank()) {
            return (root, query, cb) -> cb.between(root.get("user").get("userPlatformDetails").get("followers"), Long.valueOf(searchTerm1.get()), Long.valueOf(searchTerm2.get()));
        } else return null;
    }

    private static String getContainsLikePattern(Optional<String> searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm.get().toLowerCase() + "%";
        }
    }
}
