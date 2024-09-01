package me.choijunha.youtubecommentproject.channel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChannelDto {
    private String id;
    private String title;
    private String thumbnailUrl;
}