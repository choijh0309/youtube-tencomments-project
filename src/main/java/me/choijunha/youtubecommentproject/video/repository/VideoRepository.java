package me.choijunha.youtubecommentproject.video.repository;

import me.choijunha.youtubecommentproject.category.entity.Category;
import me.choijunha.youtubecommentproject.channel.entity.Channel;
import me.choijunha.youtubecommentproject.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

    List<Video> findByChannel(Channel channel);

    List<Video> findByCategory(Category category);

    @Query("SELECT v FROM Video v WHERE v.category = :category ORDER BY v.id DESC")
    List<Video> findRecentVideosByCategory(@Param("category") Category category);

    List<Video> findByTitleContaining(String keyword);

    @Query("SELECT v FROM Video v JOIN v.comments c GROUP BY v ORDER BY COUNT(c) DESC")
    List<Video> findTopVideosByCommentCount();

    @Query("SELECT v FROM Video v LEFT JOIN FETCH v.channel LEFT JOIN FETCH v.category WHERE v.id = :id")
    Optional<Video> findByIdWithChannelAndCategory(@Param("id") String id);
}