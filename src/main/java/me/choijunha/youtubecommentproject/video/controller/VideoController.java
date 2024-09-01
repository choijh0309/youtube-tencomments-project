package me.choijunha.youtubecommentproject.video.controller;

import me.choijunha.youtubecommentproject.category.entity.Category;
import me.choijunha.youtubecommentproject.video.dto.VideoDto;
import me.choijunha.youtubecommentproject.video.service.VideoService;
import me.choijunha.youtubecommentproject.youtube.service.YoutubeService;
import me.choijunha.youtubecommentproject.exception.YouTubeApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;
    private final YoutubeService youtubeService;

    @Autowired
    public VideoController(VideoService videoService, YoutubeService youtubeService) {
        this.videoService = videoService;
        this.youtubeService = youtubeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDto> getVideoById(@PathVariable String id) {
        VideoDto video = videoService.getVideoById(id);
        return ResponseEntity.ok(video);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<VideoDto>> getVideosByCategory(@PathVariable Category.CategoryName categoryName) {
        List<VideoDto> videos = videoService.getVideosByCategory(categoryName);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/recent/category/{categoryName}")
    public ResponseEntity<List<VideoDto>> getRecentVideosByCategory(@PathVariable Category.CategoryName categoryName) {
        List<VideoDto> videos = videoService.getRecentVideosByCategory(categoryName);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<VideoDto>> searchVideosByTitle(@RequestParam String keyword) {
        List<VideoDto> videos = videoService.searchVideosByTitle(keyword);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/top-commented")
    public ResponseEntity<List<VideoDto>> getTopVideosByCommentCount() {
        List<VideoDto> videos = videoService.getTopVideosByCommentCount();
        return ResponseEntity.ok(videos);
    }

    @PostMapping("/sync/{id}")
    public ResponseEntity<VideoDto> syncVideoFromYoutube(@PathVariable String id) {
        try {
            VideoDto youtubeVideoDto = youtubeService.getVideoInfo(id);
            if (youtubeVideoDto == null) {
                return ResponseEntity.notFound().build();
            }
            VideoDto video = videoService.createOrUpdateVideo(youtubeVideoDto);
            return ResponseEntity.ok(video);
        } catch (YouTubeApiException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/popular/{categoryName}")
    public ResponseEntity<List<VideoDto>> getPopularVideosByCategory(@PathVariable Category.CategoryName categoryName) {
        try {
            List<VideoDto> videos = videoService.getPopularVideosByCategory(categoryName);
            return ResponseEntity.ok(videos);
        } catch (YouTubeApiException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}