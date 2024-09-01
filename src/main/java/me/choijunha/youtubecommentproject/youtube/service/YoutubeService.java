package me.choijunha.youtubecommentproject.youtube.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.choijunha.youtubecommentproject.category.dto.CategoryDto;
import me.choijunha.youtubecommentproject.category.entity.Category;
import me.choijunha.youtubecommentproject.comment.dto.CommentDto;
import me.choijunha.youtubecommentproject.video.dto.VideoDto;
import me.choijunha.youtubecommentproject.channel.dto.ChannelDto;
import me.choijunha.youtubecommentproject.exception.YouTubeApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class YoutubeService {

    private static final Logger logger = LoggerFactory.getLogger(YoutubeService.class);

    private final YouTube youtube;
    private final String apiKey;
    private final Cache<String, String> channelThumbnailCache;

    @Autowired
    public YoutubeService(YouTube youtube, @Value("${youtube.api.key}") String apiKey) {
        this.youtube = youtube;
        this.apiKey = apiKey;
        this.channelThumbnailCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
        validateApiKey();
    }

    private void validateApiKey() {
        try {
            YouTube.Videos.List request = youtube.videos()
                    .list(List.of("snippet"))
                    .setChart("mostPopular")
                    .setMaxResults(1L)
                    .setKey(apiKey);
            request.execute();
            logger.info("API key validation successful");
        } catch (GoogleJsonResponseException e) {
            logger.error("API key validation failed: {}", e.getDetails().getMessage());
            throw new YouTubeApiException("Invalid YouTube API key", e);
        } catch (IOException e) {
            logger.error("Error during API key validation", e);
            throw new YouTubeApiException("Error validating YouTube API key", e);
        }
    }

    public List<CommentDto> getTopVideoComments(String videoId) {
        logger.info("Fetching top comment for video: {}", videoId);
        try {
            YouTube.CommentThreads.List request = youtube.commentThreads()
                    .list(List.of("snippet"))
                    .setVideoId(videoId)
                    .setOrder("relevance")
                    .setMaxResults(100L)
                    .setKey(apiKey);

            CommentThreadListResponse response = request.execute();

            return response.getItems().stream()
                    .map(this::convertToCommentDto)
                    .max(Comparator.comparingInt(CommentDto::getLikeCount))
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());

        } catch (GoogleJsonResponseException e) {
            if (e.getDetails().getCode() == 403 && e.getDetails().getMessage().contains("commentsDisabled")) {
                logger.warn("Comments are disabled for video: {}", videoId);
                return Collections.emptyList();
            } else {
                logger.error("Error fetching comments for video {}: {}", videoId, e.getDetails().getMessage());
                throw new YouTubeApiException("Error fetching comments from YouTube API", e);
            }
        } catch (IOException e) {
            logger.error("IO error while fetching comments for video {}", videoId, e);
            throw new YouTubeApiException("Error fetching comments", e);
        }
    }

    public List<VideoDto> getPopularVideosByCategory(String categoryId) {
        logger.info("Fetching popular videos for category: {}", categoryId);
        try {
            YouTube.Videos.List request = youtube.videos()
                    .list(List.of("snippet", "statistics"))
                    .setChart("mostPopular")
                    .setRegionCode("KR")
                    .setVideoCategoryId(categoryId)
                    .setMaxResults(50L)
                    .setKey(apiKey);

            VideoListResponse response = request.execute();
            List<String> channelIds = response.getItems().stream()
                    .map(video -> video.getSnippet().getChannelId())
                    .distinct()
                    .collect(Collectors.toList());

            Map<String, String> channelThumbnails = getChannelThumbnails(channelIds);

            List<VideoDto> videos = response.getItems().stream()
                    .map(video -> {
                        VideoDto videoDto = convertToVideoDto(video);
                        ChannelDto channelDto = new ChannelDto();
                        channelDto.setId(video.getSnippet().getChannelId());
                        channelDto.setTitle(video.getSnippet().getChannelTitle());
                        channelDto.setThumbnailUrl(channelThumbnails.get(video.getSnippet().getChannelId()));
                        videoDto.setChannel(channelDto);
                        return videoDto;
                    })
                    .collect(Collectors.toList());

            logger.info("Fetched {} popular videos for category: {}", videos.size(), categoryId);
            return videos;
        } catch (GoogleJsonResponseException e) {
            logger.error("Google API error for category {}: {}", categoryId, e.getDetails().getMessage());
            throw new YouTubeApiException("Error fetching popular videos from YouTube API", e);
        } catch (IOException e) {
            logger.error("IO error while fetching popular videos for category {}", categoryId, e);
            throw new YouTubeApiException("Error fetching popular videos", e);
        }
    }

    private Map<String, String> getChannelThumbnails(List<String> channelIds) {
        Map<String, String> thumbnails = new HashMap<>();
        List<String> missingChannelIds = new ArrayList<>();

        for (String channelId : channelIds) {
            String cachedThumbnail = channelThumbnailCache.getIfPresent(channelId);
            if (cachedThumbnail != null) {
                thumbnails.put(channelId, cachedThumbnail);
            } else {
                missingChannelIds.add(channelId);
            }
        }

        if (!missingChannelIds.isEmpty()) {
            try {
                YouTube.Channels.List request = youtube.channels()
                        .list(List.of("snippet"))
                        .setId(missingChannelIds)
                        .setKey(apiKey);

                ChannelListResponse response = request.execute();
                for (Channel channel : response.getItems()) {
                    String thumbnailUrl = channel.getSnippet().getThumbnails().getDefault().getUrl();
                    thumbnails.put(channel.getId(), thumbnailUrl);
                    channelThumbnailCache.put(channel.getId(), thumbnailUrl);
                }
            } catch (IOException e) {
                logger.error("Error fetching channel thumbnails", e);
            }
        }

        return thumbnails;
    }

    public List<CommentDto> getVideoComments(String videoId) {
        logger.info("Fetching comments for video: {}", videoId);
        try {
            YouTube.CommentThreads.List request = youtube.commentThreads()
                    .list(List.of("snippet", "replies"))
                    .setVideoId(videoId)
                    .setOrder("relevance")
                    .setMaxResults(1000L)
                    .setKey(apiKey);

            logger.debug("API Request: {}", request);

            CommentThreadListResponse response = request.execute();
            logger.debug("API Response: {}", response);

            List<CommentDto> comments = response.getItems().stream()
                    .map(this::convertToCommentDto)
                    .collect(Collectors.toList());

            logger.info("Fetched {} comments for video: {}", comments.size(), videoId);
            return comments;
        } catch (GoogleJsonResponseException e) {
            if (e.getDetails().getCode() == 403 && e.getDetails().getMessage().contains("disabled comments")) {
                logger.warn("Comments are disabled for video: {}", videoId);
                return new ArrayList<>();
            } else {
                logger.error("Error fetching comments for video {}: {}", videoId, e.getDetails().getMessage());
                throw new YouTubeApiException("Error fetching comments from YouTube API", e);
            }
        } catch (IOException e) {
            logger.error("IO error while fetching comments for video {}", videoId, e);
            throw new YouTubeApiException("Error fetching comments", e);
        }
    }

    public VideoDto getVideoInfo(String videoId) {
        logger.info("Fetching info for video: {}", videoId);
        try {
            YouTube.Videos.List request = youtube.videos()
                    .list(List.of("snippet", "statistics"))
                    .setId(List.of(videoId))
                    .setKey(apiKey);

            logger.debug("API Request: {}", request);

            VideoListResponse response = request.execute();
            logger.debug("API Response: {}", response);

            if (response.getItems().isEmpty()) {
                logger.warn("No video found with id: {}", videoId);
                return null;
            }
            VideoDto videoDto = convertToVideoDto(response.getItems().get(0));

            ChannelDto channelDto = new ChannelDto();
            channelDto.setId(response.getItems().get(0).getSnippet().getChannelId());
            channelDto.setTitle(response.getItems().get(0).getSnippet().getChannelTitle());
            videoDto.setChannel(channelDto);

            CategoryDto categoryDto = new CategoryDto();
            String categoryId = response.getItems().get(0).getSnippet().getCategoryId();
            if (categoryId != null) {
                categoryDto.setId(Long.parseLong(categoryId));
                videoDto.setCategory(categoryDto);
            } else {
                logger.warn("No category found for video: {}", videoId);
            }

            logger.info("Fetched info for video: {}", videoId);
            return videoDto;
        } catch (GoogleJsonResponseException e) {
            logger.error("Google API error for video {}: {}", videoId, e.getDetails().getMessage());
            throw new YouTubeApiException("Error fetching video info from YouTube API", e);
        } catch (IOException e) {
            logger.error("IO error while fetching video info {}", videoId, e);
            throw new YouTubeApiException("Error fetching video info", e);
        }
    }

    public ChannelDto getChannelInfo(String channelId) {
        logger.info("Fetching info for channel: {}", channelId);
        try {
            YouTube.Channels.List request = youtube.channels()
                    .list(List.of("snippet", "statistics"))
                    .setId(List.of(channelId))
                    .setKey(apiKey);

            ChannelListResponse response = request.execute();

            if (response.getItems().isEmpty()) {
                logger.warn("No channel found with id: {}", channelId);
                return null;
            }

            Channel channel = response.getItems().get(0);
            logger.info("YouTube API Response - Channel ID: {}, Title: {}, Thumbnail URL: {}",
                    channel.getId(),
                    channel.getSnippet().getTitle(),
                    channel.getSnippet().getThumbnails().getDefault().getUrl());

            ChannelDto channelDto = convertToChannelDto(channel);
            logger.info("Fetched info for channel: {}", channelId);
            return channelDto;
        } catch (GoogleJsonResponseException e) {
            logger.error("Google API error for channel {}: {}", channelId, e.getDetails().getMessage());
            throw new YouTubeApiException("Error fetching channel info from YouTube API", e);
        } catch (IOException e) {
            logger.error("IO error while fetching channel info {}", channelId, e);
            throw new YouTubeApiException("Error fetching channel info", e);
        }
    }

    private VideoDto convertToVideoDto(Video video) {
        VideoDto videoDto = new VideoDto();
        videoDto.setId(video.getId());
        videoDto.setTitle(video.getSnippet().getTitle());
        videoDto.setThumbnailUrl(video.getSnippet().getThumbnails().getDefault().getUrl());

        ChannelDto channelDto = new ChannelDto();
        channelDto.setId(video.getSnippet().getChannelId());
        channelDto.setTitle(video.getSnippet().getChannelTitle());
        channelDto.setThumbnailUrl(video.getSnippet().getThumbnails().getDefault().getUrl());
        videoDto.setChannel(channelDto);

        return videoDto;
    }

    private CommentDto convertToCommentDto(CommentThread commentThread) {
        Comment topLevelComment = commentThread.getSnippet().getTopLevelComment();
        CommentDto commentDto = new CommentDto();
        commentDto.setId(topLevelComment.getId());
        commentDto.setContent(topLevelComment.getSnippet().getTextDisplay());
        commentDto.setLikeCount(topLevelComment.getSnippet().getLikeCount().intValue());
        return commentDto;
    }

    private ChannelDto convertToChannelDto(Channel channel) {
        ChannelDto channelDto = new ChannelDto();
        channelDto.setId(channel.getId());
        channelDto.setTitle(channel.getSnippet().getTitle());
        channelDto.setThumbnailUrl(channel.getSnippet().getThumbnails().getDefault().getUrl());
        logger.info("Converting to ChannelDto - ID: {}, Title: {}, Thumbnail URL: {}",
                channelDto.getId(), channelDto.getTitle(), channelDto.getThumbnailUrl());
        return channelDto;
    }

    public String getCategoryId(Category.CategoryName categoryName) {
        logger.debug("Getting category ID for: {}", categoryName);
        switch (categoryName) {
            case NOW:
                return "0";
            case MUSIC:
                return "10";
            case GAMING:
                return "20";
            default:
                logger.warn("Unknown category: {}", categoryName);
                throw new IllegalArgumentException("Unknown category: " + categoryName);
        }
    }
}