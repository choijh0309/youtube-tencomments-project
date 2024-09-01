package me.choijunha.youtubecommentproject.video.service;

import me.choijunha.youtubecommentproject.category.entity.Category;
import me.choijunha.youtubecommentproject.category.repository.CategoryRepository;
import me.choijunha.youtubecommentproject.channel.entity.Channel;
import me.choijunha.youtubecommentproject.channel.repository.ChannelRepository;
import me.choijunha.youtubecommentproject.comment.dto.CommentDto;
import me.choijunha.youtubecommentproject.comment.service.CommentService;
import me.choijunha.youtubecommentproject.exception.ResourceNotFoundException;
import me.choijunha.youtubecommentproject.exception.YouTubeApiException;
import me.choijunha.youtubecommentproject.video.dto.VideoDto;
import me.choijunha.youtubecommentproject.video.entity.Video;
import me.choijunha.youtubecommentproject.video.mapper.VideoMapper;
import me.choijunha.youtubecommentproject.video.repository.VideoRepository;
import me.choijunha.youtubecommentproject.youtube.service.YoutubeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VideoService {

    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final VideoRepository videoRepository;
    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;
    private final VideoMapper videoMapper;
    private final YoutubeService youtubeService;
    private final CommentService commentService;

    @Autowired
    public VideoService(VideoRepository videoRepository, ChannelRepository channelRepository,
                        CategoryRepository categoryRepository, VideoMapper videoMapper,
                        YoutubeService youtubeService, CommentService commentService) {
        this.videoRepository = videoRepository;
        this.channelRepository = channelRepository;
        this.categoryRepository = categoryRepository;
        this.videoMapper = videoMapper;
        this.youtubeService = youtubeService;
        this.commentService = commentService;
    }

    @Transactional(readOnly = false)
    public void updateVideoAndComments(String videoId) {
        try {
            VideoDto youtubeVideoDto = youtubeService.getVideoInfo(videoId);
            if (youtubeVideoDto == null) {
                throw new ResourceNotFoundException("Video not found on YouTube: " + videoId);
            }
            Video video = saveOrUpdateVideo(youtubeVideoDto);
            commentService.initializeCommentsForVideo(videoId);
            logger.info("Updated video and comments for video: {}", videoId);
        } catch (YouTubeApiException e) {
            logger.error("Failed to update video and comments for video {}: {}", videoId, e.getMessage());
            throw e;
        }
    }

    @Transactional
    public List<VideoDto> getPopularVideosByCategory(Category.CategoryName categoryName) {
        String categoryId = getCategoryId(categoryName);
        List<VideoDto> youtubeVideos = youtubeService.getPopularVideosByCategory(categoryId);
        return youtubeVideos.stream()
                .filter(this::hasComments)
                .map(this::createOrUpdateVideo)
                .collect(Collectors.toList());
    }

    private boolean hasComments(VideoDto videoDto) {
        try {
            List<CommentDto> comments = youtubeService.getTopVideoComments(videoDto.getId());
            return !comments.isEmpty();
        } catch (YouTubeApiException e) {
            logger.warn("Unable to fetch comments for video {}: {}", videoDto.getId(), e.getMessage());
            return false;
        }
    }

    @Transactional
    public VideoDto createOrUpdateVideo(VideoDto youtubeVideoDto) {
        try {
            Video video = videoRepository.findById(youtubeVideoDto.getId())
                    .map(existingVideo -> {
                        videoMapper.updateEntityFromDto(youtubeVideoDto, existingVideo);
                        return existingVideo;
                    })
                    .orElseGet(() -> videoMapper.toEntity(youtubeVideoDto));

            Channel channel = channelRepository.findById(youtubeVideoDto.getChannel().getId())
                    .orElseGet(() -> {
                        Channel newChannel = new Channel();
                        newChannel.setId(youtubeVideoDto.getChannel().getId());
                        newChannel.setTitle(youtubeVideoDto.getChannel().getTitle());
                        return channelRepository.save(newChannel);
                    });
            video.setChannel(channel);

            Category category = categoryRepository.findById(youtubeVideoDto.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + youtubeVideoDto.getCategory().getId()));
            video.setCategory(category);

            Video savedVideo = videoRepository.save(video);
            logger.info("Video saved: {}", savedVideo);
            return videoMapper.toDto(savedVideo);
        } catch (Exception e) {
            logger.error("Error creating or updating video: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating or updating video: " + e.getMessage(), e);
        }
    }

    public VideoDto getVideoById(String id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));
        return videoMapper.toDto(video);
    }

    public List<VideoDto> getVideosByCategory(Category.CategoryName categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryName));
        return videoRepository.findByCategory(category).stream()
                .map(videoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VideoDto> getRecentVideosByCategory(Category.CategoryName categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryName));
        return videoRepository.findRecentVideosByCategory(category).stream()
                .map(videoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VideoDto> searchVideosByTitle(String keyword) {
        return videoRepository.findByTitleContaining(keyword).stream()
                .map(videoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VideoDto> getTopVideosByCommentCount() {
        return videoRepository.findTopVideosByCommentCount().stream()
                .map(videoMapper::toDto)
                .collect(Collectors.toList());
    }

    private String getCategoryId(Category.CategoryName categoryName) {
        return youtubeService.getCategoryId(categoryName);
    }

    @Transactional(readOnly = false)
    public Video saveOrUpdateVideo(VideoDto videoDto) {
        Video video = videoRepository.findById(videoDto.getId()).orElse(new Video());
        video.setId(videoDto.getId());
        video.setTitle(videoDto.getTitle());
        video.setThumbnailUrl(videoDto.getThumbnailUrl());

        Channel channel = channelRepository.findById(videoDto.getChannel().getId())
                .orElseGet(() -> {
                    Channel newChannel = new Channel();
                    newChannel.setId(videoDto.getChannel().getId());
                    newChannel.setTitle(videoDto.getChannel().getTitle());
                    newChannel.setThumbnailUrl(videoDto.getChannel().getThumbnailUrl());
                    return channelRepository.save(newChannel);
                });
        video.setChannel(channel);

        if (videoDto.getCategory() != null) {
            Category category = categoryRepository.findById(videoDto.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + videoDto.getCategory().getId()));
            video.setCategory(category);
        } else {
            logger.warn("Video with ID {} has no category. Setting default category.", videoDto.getId());
            Category defaultCategory = categoryRepository.findByName(Category.CategoryName.NOW)
                    .orElseThrow(() -> new ResourceNotFoundException("Default category not found"));
            video.setCategory(defaultCategory);
        }

        return videoRepository.save(video);
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }
}