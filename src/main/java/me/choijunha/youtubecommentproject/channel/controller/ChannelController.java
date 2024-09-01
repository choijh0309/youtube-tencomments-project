package me.choijunha.youtubecommentproject.channel.controller;

import me.choijunha.youtubecommentproject.channel.dto.ChannelDto;
import me.choijunha.youtubecommentproject.channel.service.ChannelService;
import me.choijunha.youtubecommentproject.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelDto> getChannelById(@PathVariable String id) {
        try {
            ChannelDto channel = channelService.getChannelById(id);
            return ResponseEntity.ok(channel);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> getAllChannels() {
        List<ChannelDto> channels = channelService.getAllChannels();
        return ResponseEntity.ok(channels);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ChannelDto> createOrUpdateChannel(@PathVariable String id) {
        try {
            ChannelDto channel = channelService.createOrUpdateChannel(id);
            return ResponseEntity.ok(channel);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable String id) {
        channelService.deleteChannel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{title}")
    public ResponseEntity<Boolean> channelExists(@PathVariable String title) {
        boolean exists = channelService.channelExists(title);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/refresh/{id}")
    public ResponseEntity<ChannelDto> refreshChannelFromYoutube(@PathVariable String id) {
        try {
            ChannelDto refreshedChannel = channelService.refreshChannelFromYoutube(id);
            return ResponseEntity.ok(refreshedChannel);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}