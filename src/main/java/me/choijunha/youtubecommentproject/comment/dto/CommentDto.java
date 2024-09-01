package me.choijunha.youtubecommentproject.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.choijunha.youtubecommentproject.video.dto.VideoDto;
import me.choijunha.youtubecommentproject.category.dto.CategoryDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDto {
    private String id;
    private String content;
    private int likeCount;
    private VideoDto video;
    private CategoryDto category;
    private LocalDateTime lastUpdatedAt;
}