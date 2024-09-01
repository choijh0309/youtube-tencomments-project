package me.choijunha.youtubecommentproject.comment.service;

import jakarta.annotation.PostConstruct;
import me.choijunha.youtubecommentproject.category.entity.Category;
import me.choijunha.youtubecommentproject.category.repository.CategoryRepository;
import me.choijunha.youtubecommentproject.channel.entity.Channel;
import me.choijunha.youtubecommentproject.channel.repository.ChannelRepository;
import me.choijunha.youtubecommentproject.comment.dto.CommentDto;
import me.choijunha.youtubecommentproject.comment.entity.Comment;
import me.choijunha.youtubecommentproject.comment.mapper.CommentMapper;
import me.choijunha.youtubecommentproject.comment.repository.CommentRepository;
import me.choijunha.youtubecommentproject.exception.ResourceNotFoundException;
import me.choijunha.youtubecommentproject.exception.YouTubeApiException;
import me.choijunha.youtubecommentproject.video.dto.VideoDto;
import me.choijunha.youtubecommentproject.video.entity.Video;
import me.choijunha.youtubecommentproject.video.repository.VideoRepository;
import me.choijunha.youtubecommentproject.youtube.service.YoutubeService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final ChannelRepository channelRepository;
    private final CommentMapper commentMapper;
    private final YoutubeService youtubeService;

    @Autowired
    public CommentService(CommentRepository commentRepository, VideoRepository videoRepository,
                          CategoryRepository categoryRepository, ChannelRepository channelRepository,
                          CommentMapper commentMapper, YoutubeService youtubeService) {
        this.commentRepository = commentRepository;
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
        this.channelRepository = channelRepository;
        this.commentMapper = commentMapper;
        this.youtubeService = youtubeService;
    }

    @PostConstruct
    @Transactional(readOnly = false)
    public void initializeData() {
        if (categoryRepository.count() == 0) {
            logger.info("Initializing categories...");
            for (Category.CategoryName categoryName : Category.CategoryName.values()) {
                Category category = new Category(categoryName);
                categoryRepository.save(category);
                logger.info("Category initialized: {}", categoryName);
            }
        }

        for (Category.CategoryName categoryName : Category.CategoryName.values()) {
            try {
                logger.info("Initializing data for category: {}", categoryName);
                String categoryId = youtubeService.getCategoryId(categoryName);
                List<VideoDto> videos = youtubeService.getPopularVideosByCategory(categoryId);
                for (VideoDto videoDto : videos) {
                    Video video = saveOrUpdateVideo(videoDto, categoryName);
                    initializeCommentsForVideo(video.getId());
                }
            } catch (YouTubeApiException e) {
                logger.error("Error initializing data for category {}: {}", categoryName, e.getMessage());
            }
        }
        logger.info("Comment data initialization completed.");
    }

    @Transactional(readOnly = false)
    public void initializeCommentsForVideo(String videoId) {
        try {
            Video video = videoRepository.findById(videoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + videoId));

            List<CommentDto> youtubeComments = youtubeService.getTopVideoComments(videoId);

            if (!youtubeComments.isEmpty()) {
                for (CommentDto commentDto : youtubeComments) {
                    saveOrUpdateComment(commentDto, video);
                }
                logger.info("Initialized {} comments for video: {}", youtubeComments.size(), videoId);
            } else {
                logger.info("No comments available for video: {}. Comments might be disabled.", videoId);
            }
        } catch (YouTubeApiException e) {
            logger.warn("Unable to initialize comments for video {}: {}", videoId, e.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public Video saveOrUpdateVideo(VideoDto videoDto, Category.CategoryName categoryName) {
        Channel channel = channelRepository.findById(videoDto.getChannel().getId())
                .orElseGet(() -> {
                    Channel newChannel = new Channel();
                    newChannel.setId(videoDto.getChannel().getId());
                    newChannel.setTitle(videoDto.getChannel().getTitle());
                    newChannel.setThumbnailUrl(videoDto.getChannel().getThumbnailUrl());
                    return channelRepository.save(newChannel);
                });

        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryName));

        Video video = videoRepository.findById(videoDto.getId()).orElse(new Video());
        video.setId(videoDto.getId());
        video.setTitle(videoDto.getTitle());
        video.setThumbnailUrl(videoDto.getThumbnailUrl());
        video.setChannel(channel);
        video.setCategory(category);
        return videoRepository.save(video);
    }

    public List<CommentDto> getTopCommentsByCategory(Category.CategoryName categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryName));
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        return commentRepository.findTopCommentsByCategoryAndLastUpdatedAfterWithFetchJoin(category, cutoffTime).stream()
                .limit(10)
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public void saveOrUpdateComment(CommentDto commentDto, Video video) {
        Comment comment = commentRepository.findById(commentDto.getId())
                .orElse(new Comment());
        comment.setId(commentDto.getId());
        comment.setContent(commentDto.getContent());
        comment.setLikeCount(commentDto.getLikeCount());
        comment.setVideo(video);
        comment.setCategory(video.getCategory());
        comment.setLastUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Transactional
    public void updateAllComments() {
        logger.info("Updating all comments");
        List<Video> videos = videoRepository.findAll();
        for (Video video : videos) {
            try {
                updateCommentsForVideo(video.getId());
            } catch (YouTubeApiException e) {
                logger.warn("Unable to update comments for video {}: {}", video.getId(), e.getMessage());
            }
        }
        logger.info("Finished updating all comments");
    }

    @Transactional
    public List<CommentDto> updateCommentsForVideo(String videoId) {
        logger.info("Updating comments for video: {}", videoId);
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + videoId));

        Hibernate.initialize(video.getChannel());
        Hibernate.initialize(video.getCategory());

        List<CommentDto> youtubeComments;
        try {
            youtubeComments = youtubeService.getTopVideoComments(videoId);
        } catch (YouTubeApiException e) {
            logger.warn("Unable to fetch comments for video {}: {}", videoId, e.getMessage());
            return new ArrayList<>();
        }

        List<CommentDto> updatedComments = youtubeComments.stream()
                .map(commentDto -> {
                    saveOrUpdateComment(commentDto, video);
                    return commentDto;
                })
                .collect(Collectors.toList());
        logger.info("Updated {} comments for video: {}", updatedComments.size(), videoId);
        return updatedComments;
    }

    @Transactional
    public void deleteOldComments() {
        logger.info("Deleting old comments");
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        commentRepository.deleteByLastUpdatedAtBefore(cutoffTime);
        logger.info("Deleted old comments before {}", cutoffTime);
    }
}