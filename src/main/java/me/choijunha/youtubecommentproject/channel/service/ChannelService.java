package me.choijunha.youtubecommentproject.channel.service;

import me.choijunha.youtubecommentproject.channel.dto.ChannelDto;
import me.choijunha.youtubecommentproject.channel.entity.Channel;
import me.choijunha.youtubecommentproject.channel.mapper.ChannelMapper;
import me.choijunha.youtubecommentproject.channel.repository.ChannelRepository;
import me.choijunha.youtubecommentproject.exception.ResourceNotFoundException;
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
public class ChannelService {

    private static final Logger logger = LoggerFactory.getLogger(ChannelService.class);

    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;
    private final YoutubeService youtubeService;

    @Autowired
    public ChannelService(ChannelRepository channelRepository, ChannelMapper channelMapper, YoutubeService youtubeService) {
        this.channelRepository = channelRepository;
        this.channelMapper = channelMapper;
        this.youtubeService = youtubeService;
    }

    @Transactional
    public ChannelDto createOrUpdateChannel(String channelId) throws IOException {
        try {
            ChannelDto youtubeChannelDto = youtubeService.getChannelInfo(channelId);
            logger.info("Received ChannelDto - ID: {}, Title: {}, Thumbnail URL: {}",
                    youtubeChannelDto.getId(), youtubeChannelDto.getTitle(), youtubeChannelDto.getThumbnailUrl());
            if (youtubeChannelDto == null) {
                throw new ResourceNotFoundException("Channel not found on YouTube: " + channelId);
            }

            logger.info("Retrieved channel info from YouTube. Channel ID: {}, Thumbnail URL: {}",
                    youtubeChannelDto.getId(), youtubeChannelDto.getThumbnailUrl());

            Channel channel = channelRepository.findById(channelId)
                    .map(existingChannel -> {
                        channelMapper.updateEntityFromDto(youtubeChannelDto, existingChannel);
                        logger.info("Updated existing channel. Channel ID: {}, New Thumbnail URL: {}",
                                existingChannel.getId(), existingChannel.getThumbnailUrl());
                        return existingChannel;
                    })
                    .orElseGet(() -> {
                        Channel newChannel = channelMapper.toEntity(youtubeChannelDto);
                        logger.info("Created new channel. Channel ID: {}, Thumbnail URL: {}",
                                newChannel.getId(), newChannel.getThumbnailUrl());
                        return newChannel;
                    });

            Channel savedChannel = channelRepository.save(channel);
            logger.info("Saved channel to database. Channel ID: {}, Thumbnail URL: {}",
                    savedChannel.getId(), savedChannel.getThumbnailUrl());

            return channelMapper.toDto(savedChannel);
        } catch (Exception e) {
            logger.error("Error while creating or updating channel. Channel ID: {}", channelId, e);
            throw e;
        }
    }

    public ChannelDto getChannelById(String id) {
        return channelRepository.findById(id)
                .map(channelMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found with id: " + id));
    }

    public List<ChannelDto> getAllChannels() {
        return channelRepository.findAll().stream()
                .map(channelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteChannel(String id) {
        channelRepository.deleteById(id);
    }

    public boolean channelExists(String title) {
        return channelRepository.existsByTitle(title);
    }

    @Transactional
    public ChannelDto refreshChannelFromYoutube(String channelId) throws IOException {
        return createOrUpdateChannel(channelId);
    }
}