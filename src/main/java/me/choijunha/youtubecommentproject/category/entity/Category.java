package me.choijunha.youtubecommentproject.category.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.choijunha.youtubecommentproject.comment.entity.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private CategoryName name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Category(CategoryName name) {
        this.name = name;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setCategory(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setCategory(null);
    }

    public enum CategoryName {
        NOW, MUSIC, GAMING;
    }
}