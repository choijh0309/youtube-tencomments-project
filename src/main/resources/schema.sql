DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS videos;
DROP TABLE IF EXISTS channels;
DROP TABLE IF EXISTS categories;

CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE channels (
                          id VARCHAR(255) PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          thumbnail_url VARCHAR(255)
);

CREATE TABLE videos (
                        id VARCHAR(255) PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        thumbnail_url VARCHAR(255),
                        channel_id VARCHAR(255) NOT NULL,
                        category_id BIGINT NOT NULL,
                        FOREIGN KEY (channel_id) REFERENCES channels(id),
                        FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE comments (
                          id VARCHAR(255) PRIMARY KEY,
                          content TEXT NOT NULL,
                          like_count INTEGER,
                          video_id VARCHAR(255) NOT NULL,
                          category_id BIGINT NOT NULL,
                          last_updated_at TIMESTAMP NOT NULL,
                          FOREIGN KEY (video_id) REFERENCES videos(id),
                          FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX idx_comment_like_count ON comments(like_count);
CREATE INDEX idx_comment_last_updated_at ON comments(last_updated_at);
CREATE INDEX idx_comment_video ON comments(video_id);
CREATE INDEX idx_video_category ON videos(category_id);
CREATE INDEX idx_video_channel ON videos(channel_id);