package com.tochko.advertising_platform.repository;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query(nativeQuery = true,
            value = "SELECT r.announcement_id, avg(r.rating) as ann_R FROM Rating r group by r.announcement_id order by ann_R desc limit 10")
    List<Object[]> findByAnnouncements();

}


