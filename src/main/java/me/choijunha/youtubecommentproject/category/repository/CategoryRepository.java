package me.choijunha.youtubecommentproject.category.repository;

import me.choijunha.youtubecommentproject.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(Category.CategoryName name);

    boolean existsByName(Category.CategoryName name);
}