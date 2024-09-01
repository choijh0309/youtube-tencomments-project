package me.choijunha.youtubecommentproject.category.service;

import jakarta.annotation.PostConstruct;
import me.choijunha.youtubecommentproject.category.dto.CategoryDto;
import me.choijunha.youtubecommentproject.category.entity.Category;
import me.choijunha.youtubecommentproject.category.mapper.CategoryMapper;
import me.choijunha.youtubecommentproject.category.repository.CategoryRepository;
import me.choijunha.youtubecommentproject.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    public CategoryDto getCategoryByName(Category.CategoryName name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
        return categoryMapper.toDto(category);
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }


    @PostConstruct
    public void initializeCategories() {
        if (categoryRepository.count() == 0) {
            for (Category.CategoryName name : Category.CategoryName.values()) {
                Category category = new Category(name);
                categoryRepository.save(category);
            }
        }
    }

}