package me.choijunha.youtubecommentproject.video.dto;

import lombok.*;
import me.choijunha.youtubecommentproject.category.dto.CategoryDto;
import me.choijunha.youtubecommentproject.channel.dto.ChannelDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VideoDto {
    private String id;
    private String title;
    private String thumbnailUrl;
    private ChannelDto channel;
    private CategoryDto category;
}