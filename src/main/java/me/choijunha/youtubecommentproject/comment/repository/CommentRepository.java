package me.choijunha.youtubecommentproject.comment.repository;

import me.choijunha.youtubecommentproject.comment.entity.Comment;
import me.choijunha.youtubecommentproject.category.entity.Category;
import me.choijunha.youtubecommentproject.video.entity.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    Logger logger = LoggerFactory.getLogger(CommentRepository.class);

    List<Comment> findByVideo(Video video);

    List<Comment> findByCategory(Category category);

    @Query("SELECT c FROM Comment c WHERE c.category = :category ORDER BY c.likeCount DESC")
    List<Comment> findTopCommentsByCategory(@Param("category") Category category);

    @Query("SELECT c FROM Comment c WHERE c.category = :category AND c.lastUpdatedAt > :cutoffTime ORDER BY c.likeCount DESC")
    List<Comment> findTopCommentsByCategoryAndLastUpdatedAfter(@Param("category") Category category, @Param("cutoffTime") LocalDateTime cutoffTime);

    default List<Comment> findTopCommentsByCategoryAndLastUpdatedAfterWithLogging(Category category, LocalDateTime cutoffTime) {
        List<Comment> comments = findTopCommentsByCategoryAndLastUpdatedAfter(category, cutoffTime);
        logger.info("Found {} comments for category {} after {}", comments.size(), category.getName(), cutoffTime);
        return comments;
    }

    List<Comment> findByLastUpdatedAtBefore(LocalDateTime dateTime);

    void deleteByLastUpdatedAtBefore(LocalDateTime dateTime);

    @Query("SELECT c FROM Comment c WHERE c.video = :video ORDER BY c.likeCount DESC")
    List<Comment> findTopCommentsByVideo(@Param("video") Video video);

    default List<Comment> findTopCommentsByVideoWithLogging(Video video) {
        List<Comment> comments = findTopCommentsByVideo(video);
        logger.info("Found {} top comments for video {}", comments.size(), video.getId());
        return comments;
    }

    default List<Comment> findByCategoryWithLogging(Category category) {
        List<Comment> comments = findByCategory(category);
        logger.info("Found {} comments for category {}", comments.size(), category.getName());
        return comments;
    }

    default void deleteByLastUpdatedAtBeforeWithLogging(LocalDateTime dateTime) {
        long countBefore = count();
        deleteByLastUpdatedAtBefore(dateTime);
        long countAfter = count();
        long deletedCount = countBefore - countAfter;
        logger.info("Deleted {} comments older than {}", deletedCount, dateTime);
    }

    @Query("SELECT DISTINCT c FROM Comment c JOIN FETCH c.video v JOIN FETCH v.channel WHERE c.category = :category AND c.lastUpdatedAt > :cutoffTime ORDER BY c.likeCount DESC")
    List<Comment> findTopCommentsByCategoryAndLastUpdatedAfterWithFetchJoin(@Param("category") Category category, @Param("cutoffTime") LocalDateTime cutoffTime);
}