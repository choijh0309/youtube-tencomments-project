package me.choijunha.youtubecommentproject.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import me.choijunha.youtubecommentproject.video.entity.Video;
import me.choijunha.youtubecommentproject.category.entity.Category;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    private String id;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(name = "like_count")
    private int likeCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }

    public boolean isOlderThan24Hours() {
        return LocalDateTime.now().minusHours(24).isAfter(this.lastUpdatedAt);
    }
}