package com.tochko.advertising_platform.repository;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.Comment;
import com.tochko.advertising_platform.model.enums.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findById(long id);

    List<Comment> findByAnnouncement(Announcement announcement);

    @Modifying
    @Query("delete from Comment c where c.id = ?1")
    void deleteById(long id);

    @Modifying
    @Query("UPDATE Comment comment SET comment.status=?2 WHERE comment.id=?1")
    void changeStatus(Long commentId, CommentStatus status);
}
