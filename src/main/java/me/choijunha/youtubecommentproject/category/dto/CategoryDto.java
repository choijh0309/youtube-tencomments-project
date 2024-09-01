package me.choijunha.youtubecommentproject.category.dto;

import lombok.*;
import me.choijunha.youtubecommentproject.category.entity.Category;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDto {
    private Long id;
    private Category.CategoryName name;

    public CategoryDto(Category.CategoryName name) {
        this.name = name;
    }
}