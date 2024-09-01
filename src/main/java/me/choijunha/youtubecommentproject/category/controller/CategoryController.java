package me.choijunha.youtubecommentproject.category.controller;

import me.choijunha.youtubecommentproject.category.dto.CategoryDto;
import me.choijunha.youtubecommentproject.category.entity.Category;
import me.choijunha.youtubecommentproject.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable String name) {
        CategoryDto category = categoryService.getCategoryByName(Category.CategoryName.valueOf(name.toUpperCase()));
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllCategoryNames() {
        List<String> categoryNames = Arrays.stream(Category.CategoryName.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryNames);
    }

    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeCategories() {
        categoryService.initializeCategories();
        return ResponseEntity.ok().build();
    }
}