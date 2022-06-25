package com.tochko.advertising_platform.controller;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.Comment;
import com.tochko.advertising_platform.model.enums.CommentStatus;
import com.tochko.advertising_platform.service.AnnouncementService;
import com.tochko.advertising_platform.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final AnnouncementService announcementService;

    @Autowired
    public CommentController(CommentService commentService,
                             AnnouncementService announcementService) {
        this.commentService = commentService;
        this.announcementService = announcementService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Comment>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size) {
        try {
            Pageable paging = PageRequest.of(page - 1, size);
            Page<Comment> comments = commentService.getAll(paging);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Comment>> getAllByAnnouncementId(
            @RequestParam("id") Long announcementId) {
        try {
            Announcement announcement = announcementService.get(announcementId);
            List<Comment> comments = commentService.getAllByAnnouncement(announcement);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> get(@PathVariable("id") Long id) {
        try {
            Comment comment = commentService.get(id);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<Comment> add(@RequestBody Comment comment,
                                       @RequestParam(value = "id") Long announcementId) {
        try {
            Announcement ann = announcementService.get(announcementId);
            comment.setAnnouncement(ann);
            commentService.add(comment);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("")
    public ResponseEntity<Comment> update(@RequestParam(value = "announcementId") Long announcementId,
                                          @RequestBody Comment comment) {
        try {
            Announcement ann = announcementService.get(announcementId);
            comment.setAnnouncement(ann);
            commentService.update(comment);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/status")
    public ResponseEntity<?> changeCommentStatus(@RequestBody CommentStatus status,
                                                 @RequestParam("commentId") Long commentId) {
        try {
            commentService.changeCommentStatus(commentId, status);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> delete(@PathVariable("id") Long id,
                                          @RequestParam(value = "annId", required = false) Long announcementId) {
        try {
            commentService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
