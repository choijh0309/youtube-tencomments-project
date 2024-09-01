package me.choijunha.youtubecommentproject.comment.scheduler;

import me.choijunha.youtubecommentproject.comment.service.CommentService;
import me.choijunha.youtubecommentproject.exception.YouTubeApiException;
import me.choijunha.youtubecommentproject.video.entity.Video;
import me.choijunha.youtubecommentproject.video.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CommentScheduler.class);

    private final CommentService commentService;
    private final VideoService videoService;

    @Autowired
    public CommentScheduler(CommentService commentService, VideoService videoService) {
        this.commentService = commentService;
        this.videoService = videoService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void updateComments() {
        logger.info("Starting scheduled comment update");
        try {
            List<Video> videos = videoService.getAllVideos();
            for (Video video : videos) {
                videoService.updateVideoAndComments(video.getId());
            }
            logger.info("Scheduled comment update completed successfully");
        } catch (YouTubeApiException e) {
            logger.error("Error occurred during scheduled comment update", e);
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void deleteOldComments() {
        logger.info("Starting scheduled old comment deletion");
        commentService.deleteOldComments();
        logger.info("Scheduled old comment deletion completed successfully");
    }

    @Scheduled(cron = "0 0 * * * *")
    public void updateCommentsForAllVideos() {
        logger.info("Starting scheduled comment update for all videos");
        try {
            List<Video> videos = videoService.getAllVideos();
            for (Video video : videos) {
                videoService.updateVideoAndComments(video.getId());
            }
            logger.info("Completed scheduled comment update for all videos");
        } catch (Exception e) {
            logger.error("Error occurred during scheduled comment update", e);
        }
    }
}