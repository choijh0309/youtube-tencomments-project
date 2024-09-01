package me.choijunha.youtubecommentproject.video.mapper;

import me.choijunha.youtubecommentproject.category.mapper.CategoryMapper;
import me.choijunha.youtubecommentproject.channel.mapper.ChannelMapper;
import me.choijunha.youtubecommentproject.video.dto.VideoDto;
import me.choijunha.youtubecommentproject.video.entity.Video;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoMapper {

    private final ChannelMapper channelMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public VideoMapper(ChannelMapper channelMapper, CategoryMapper categoryMapper) {
        this.channelMapper = channelMapper;
        this.categoryMapper = categoryMapper;
    }

    public VideoDto toDto(Video video) {
        if (video == null) {
            return null;
        }

        VideoDto videoDto = new VideoDto();
        videoDto.setId(video.getId());
        videoDto.setTitle(Hibernate.isInitialized(video) ? video.getTitle() : "");
        videoDto.setThumbnailUrl(Hibernate.isInitialized(video) ? video.getThumbnailUrl() : "");

        if (video.getChannel() != null && Hibernate.isInitialized(video.getChannel())) {
            videoDto.setChannel(channelMapper.toDto(video.getChannel()));
        }

        if (video.getCategory() != null && Hibernate.isInitialized(video.getCategory())) {
            videoDto.setCategory(categoryMapper.toDto(video.getCategory()));
        }

        return videoDto;
    }

    public Video toEntity(VideoDto dto) {
        if (dto == null) {
            return null;
        }

        Video video = new Video();
        video.setId(dto.getId());
        video.setTitle(dto.getTitle());
        video.setThumbnailUrl(dto.getThumbnailUrl());
        video.setChannel(channelMapper.toEntity(dto.getChannel()));
        video.setCategory(categoryMapper.toEntity(dto.getCategory()));

        return video;
    }

    public void updateEntityFromDto(VideoDto dto, Video video) {
        if (dto == null || video == null) {
            return;
        }

        video.setTitle(dto.getTitle());
        video.setThumbnailUrl(dto.getThumbnailUrl());
        if (dto.getChannel() != null) {
            video.setChannel(channelMapper.toEntity(dto.getChannel()));
        }
        if (dto.getCategory() != null) {
            video.setCategory(categoryMapper.toEntity(dto.getCategory()));
        }
    }
}