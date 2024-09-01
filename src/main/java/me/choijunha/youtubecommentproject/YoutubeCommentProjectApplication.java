package me.choijunha.youtubecommentproject;

import me.choijunha.youtubecommentproject.category.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YoutubeCommentProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoutubeCommentProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(CategoryService categoryService) {
        return args -> {
            categoryService.initializeCategories();
        };
    }
}