package me.choijunha.youtubecommentproject.category.mapper;

import me.choijunha.youtubecommentproject.category.dto.CategoryDto;
import me.choijunha.youtubecommentproject.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }

    public Category toEntity(CategoryDto dto) {
        if (dto == null) {
            return null;
        }

        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());

        return category;
    }

    public void updateEntityFromDto(CategoryDto dto, Category category) {
        if (dto == null || category == null) {
            return;
        }

        category.setName(dto.getName());
    }

    public String categoryNameToString(Category.CategoryName categoryName) {
        return categoryName != null ? categoryName.name() : null;
    }

    public Category.CategoryName stringToCategoryName(String categoryName) {
        if (categoryName == null) {
            return null;
        }
        return Category.CategoryName.valueOf(categoryName.toUpperCase());
    }
}