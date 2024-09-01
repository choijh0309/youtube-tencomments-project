package me.choijunha.youtubecommentproject.comment.controller;

import me.choijunha.youtubecommentproject.category.entity.Category;
import me.choijunha.youtubecommentproject.comment.dto.CommentDto;
import me.choijunha.youtubecommentproject.comment.service.CommentService;
import me.choijunha.youtubecommentproject.exception.YouTubeApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/top/{categoryId}")
    public ResponseEntity<List<CommentDto>> getTopCommentsByCategory(@PathVariable int categoryId) {
        Category.CategoryName categoryName = Category.CategoryName.values()[categoryId - 1];
        List<CommentDto> comments = commentService.getTopCommentsByCategory(categoryName);

        comments.forEach(comment -> {
            logger.info("Comment ID: {}, Video ID: {}, Video Title: {}, Thumbnail URL: {}",
                    comment.getId(),
                    comment.getVideo() != null ? comment.getVideo().getId() : "null",
                    comment.getVideo() != null ? comment.getVideo().getTitle() : "null",
                    comment.getVideo() != null ? comment.getVideo().getThumbnailUrl() : "null"
            );
        });

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/update/video/{videoId}")
    public ResponseEntity<List<CommentDto>> updateCommentsForVideo(@PathVariable String videoId) {
        try {
            List<CommentDto> updatedComments = commentService.updateCommentsForVideo(videoId);
            return ResponseEntity.ok(updatedComments);
        } catch (YouTubeApiException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/update/all")
    public ResponseEntity<Void> updateAllComments() {
        try {
            commentService.updateAllComments();
            return ResponseEntity.ok().build();
        } catch (YouTubeApiException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> deleteOldComments() {
        commentService.deleteOldComments();
        return ResponseEntity.ok().build();
    }
}