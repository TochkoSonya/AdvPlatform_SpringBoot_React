package com.tochko.advertising_platform.service;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.Comment;
import com.tochko.advertising_platform.model.enums.CommentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CommentService {

    List<Comment> getAllByAnnouncement(Announcement announcement);

    Comment get(Long id);

    void add(Comment comment);

    Comment update(Comment comment) throws InterruptedException, ExecutionException;

    void delete(Long id) throws ExecutionException, InterruptedException;

    void changeCommentStatus(Long commentId, CommentStatus status);

    Page<Comment> getAll(Pageable paging);
}
