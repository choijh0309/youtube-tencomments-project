package me.choijunha.youtubecommentproject.comment.mapper;

import me.choijunha.youtubecommentproject.category.mapper.CategoryMapper;
import me.choijunha.youtubecommentproject.comment.dto.CommentDto;
import me.choijunha.youtubecommentproject.comment.entity.Comment;
import me.choijunha.youtubecommentproject.video.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final VideoMapper videoMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CommentMapper(VideoMapper videoMapper, CategoryMapper categoryMapper) {
        this.videoMapper = videoMapper;
        this.categoryMapper = categoryMapper;
    }

    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setLikeCount(comment.getLikeCount());
        dto.setLastUpdatedAt(comment.getLastUpdatedAt());

        if (comment.getVideo() != null) {
            dto.setVideo(videoMapper.toDto(comment.getVideo()));
        }

        if (comment.getCategory() != null) {
            dto.setCategory(categoryMapper.toDto(comment.getCategory()));
        }

        return dto;
    }

    public Comment toEntity(CommentDto dto) {
        if (dto == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setContent(dto.getContent());
        comment.setLikeCount(dto.getLikeCount());
        comment.setLastUpdatedAt(dto.getLastUpdatedAt());

        if (dto.getVideo() != null) {
            comment.setVideo(videoMapper.toEntity(dto.getVideo()));
        }

        if (dto.getCategory() != null) {
            comment.setCategory(categoryMapper.toEntity(dto.getCategory()));
        }

        return comment;
    }

    public void updateEntityFromDto(CommentDto dto, Comment comment) {
        if (dto == null || comment == null) {
            return;
        }

        comment.setContent(dto.getContent());
        comment.setLikeCount(dto.getLikeCount());

        if (dto.getVideo() != null) {
            comment.setVideo(videoMapper.toEntity(dto.getVideo()));
        }

        if (dto.getCategory() != null) {
            comment.setCategory(categoryMapper.toEntity(dto.getCategory()));
        }
    }
}