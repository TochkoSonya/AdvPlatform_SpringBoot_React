package com.tochko.advertising_platform.service.impl;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.Comment;
import com.tochko.advertising_platform.model.enums.CommentStatus;
import com.tochko.advertising_platform.repository.CommentRepository;
import com.tochko.advertising_platform.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.commentRepository = repository;
    }

    public Page<Comment> getAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    public List<Comment> getAllByAnnouncement(Announcement announcement) {
        return commentRepository.findByAnnouncement(announcement);
    }

    //    @Async("asyncExecutor")
    public Comment get(Long id) {
        return commentRepository.findById(id);
    }

    public void add(Comment comment) {
        comment.setCreatedDate(new Date());
        comment.setModifiedDate(new Date());
        comment.setStatus(CommentStatus.NOT_CHECKED);
        commentRepository.save(comment);
    }

    public Comment update(Comment comment) {
        comment.setModifiedDate(new Date());
        comment.setStatus(CommentStatus.NOT_CHECKED);
        return commentRepository.save(comment);
    }

    public void changeCommentStatus(Long commentId, CommentStatus status) {
        commentRepository.changeStatus(commentId, status);
    }

    public void delete(Long id) throws ExecutionException, InterruptedException {
        Comment deletedCom = get(id);
        if (deletedCom != null) {
            commentRepository.deleteById(deletedCom.getId());
        }
    }
}
