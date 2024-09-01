package me.choijunha.youtubecommentproject.channel.repository;

import me.choijunha.youtubecommentproject.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {

    Optional<Channel> findByTitle(String title);

    boolean existsByTitle(String title);
}