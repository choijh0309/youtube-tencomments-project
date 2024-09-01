package me.choijunha.youtubecommentproject.channel.mapper;

import me.choijunha.youtubecommentproject.channel.dto.ChannelDto;
import me.choijunha.youtubecommentproject.channel.entity.Channel;
import me.choijunha.youtubecommentproject.channel.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper {

    private static final Logger logger = LoggerFactory.getLogger(ChannelMapper.class);

    public ChannelDto toDto(Channel channel) {
        if (channel == null) {
            return null;
        }

        return new ChannelDto(
                channel.getId(),
                channel.getTitle(),
                channel.getThumbnailUrl()
        );
    }

    public Channel toEntity(ChannelDto dto) {
        if (dto == null) {
            return null;
        }
        Channel channel = new Channel();
        channel.setId(dto.getId());
        channel.setTitle(dto.getTitle());
        channel.setThumbnailUrl(dto.getThumbnailUrl());
        logger.info("Converting DTO to Entity - ID: {}, Title: {}, Thumbnail URL: {}",
                dto.getId(), dto.getTitle(), dto.getThumbnailUrl());
        return channel;
    }

    public void updateEntityFromDto(ChannelDto dto, Channel channel) {
        if (dto == null || channel == null) {
            return;
        }

        channel.setTitle(dto.getTitle());
        channel.setThumbnailUrl(dto.getThumbnailUrl());
    }
}