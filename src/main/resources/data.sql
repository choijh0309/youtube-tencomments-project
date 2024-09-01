
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'FILM_ANIMATION');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'MUSIC');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'SHORT_MOVIES');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'GAMING');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'COMEDY');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'ENTERTAINMENT');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'MOVIES');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'ANIME_ANIMATION');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'ACTION_ADVENTURE');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'CLASSICS');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'DRAMA');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'FAMILY');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'FOREIGN');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'HORROR');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'SCI_FI_FANTASY');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'THRILLER');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'SHORTS');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'SHOWS');
-- INSERT INTO categories (main_category, sub_category) VALUES ('ENTERTAINMENT', 'TRAILERS');

-- INSERT INTO categories (main_category, sub_category) VALUES ('EDUCATION', 'EDUCATION');
-- INSERT INTO categories (main_category, sub_category) VALUES ('EDUCATION', 'SCIENCE_TECHNOLOGY');

-- INSERT INTO categories (main_category, sub_category) VALUES ('LIFESTYLE', 'AUTOS_VEHICLES');
-- INSERT INTO categories (main_category, sub_category) VALUES ('LIFESTYLE', 'PETS_ANIMALS');
-- INSERT INTO categories (main_category, sub_category) VALUES ('LIFESTYLE', 'SPORTS');
-- INSERT INTO categories (main_category, sub_category) VALUES ('LIFESTYLE', 'TRAVEL_EVENTS');
-- INSERT INTO categories (main_category, sub_category) VALUES ('LIFESTYLE', 'VIDEOBLOGGING');
-- INSERT INTO categories (main_category, sub_category) VALUES ('LIFESTYLE', 'PEOPLE_BLOGS');
-- INSERT INTO categories (main_category, sub_category) VALUES ('LIFESTYLE', 'HOWTO_STYLE');

-- INSERT INTO channels (id, name, url, thumbnail_url) VALUES
--                                                         ('UC1234567890', '엔터테인먼트 채널', 'https://www.youtube.com/channel/UC1234567890', 'https://yt3.googleusercontent.com/ytc/AGIKgqMvZNi1q6O4WLL2V8P-5RHk1Oex4oM9iZI-TQ=s88-c-k-c0x00ffffff-no-rj'),
--                                                         ('UC2345678901', '교육 채널', 'https://www.youtube.com/channel/UC2345678901', 'https://yt3.googleusercontent.com/ytc/AGIKgqOBWyLUVe0PjYK81nSG2DgqJhI1q6O4WLL2V8P=s88-c-k-c0x00ffffff-no-rj'),
--                                                         ('UC3456789012', '라이프스타일 채널', 'https://www.youtube.com/channel/UC3456789012', 'https://yt3.googleusercontent.com/ytc/AGIKgqNBWyLUVe0PjYK81nSG2DgqJhI1q6O4WLL2V8P=s88-c-k-c0x00ffffff-no-rj'),
--                                                         ('UC4567890123', 'officialpsy', 'https://www.youtube.com/@officialpsy', 'https://yt3.googleusercontent.com/VXVR9IKCRGRAtjdXcul8EcB2MoT1ZC7d8YMlkzVfB8Iuulf3WK5HA_h6BihPBe-OnpS4Fufrag=s160-c-k-c0x00ffffff-no-rj'),
--                                                         ('UC5678901234', '게임 채널', 'https://www.youtube.com/channel/UC5678901234', 'https://yt3.googleusercontent.com/ytc/AGIKgqOBWyLUVe0PjYK81nSG2DgqJhI1q6O4WLL2V8P=s88-c-k-c0x00ffffff-no-rj');

-- INSERT INTO videos (id, title, thumbnail_url, channel_id, last_updated_at) VALUES
--                                                                                ('dQw4w9WgXcQ', 'Rick Astley - Never Gonna Give You Up (Official Music Video)', 'https://img.youtube.com/vi/dQw4w9WgXcQ/default.jpg', 'UC4567890123', CURRENT_TIMESTAMP),
--                                                                                ('9bZkp7q19f0', 'PSY - GANGNAM STYLE(강남스타일) M/V', 'https://i.ytimg.com/vi/9bZkp7q19f0/hqdefault.jpg?sqp=-oaymwEcCNACELwBSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLAwlIZLORAJKNb7smWcuzGrzFDNPw', 'UC4567890123', CURRENT_TIMESTAMP),
--                                                                                ('uD4izuDMUQA', 'The Black Hole at the Center of Our Galaxy', 'https://img.youtube.com/vi/uD4izuDMUQA/default.jpg', 'UC2345678901', CURRENT_TIMESTAMP),
--                                                                                ('jNQXAC9IVRw', 'Me at the zoo', 'https://img.youtube.com/vi/jNQXAC9IVRw/default.jpg', 'UC3456789012', CURRENT_TIMESTAMP),
--                                                                                ('xC-c7E5PK0Y', '【MINECRAFT】10 Cool Building Tricks and Ideas', 'https://img.youtube.com/vi/xC-c7E5PK0Y/default.jpg', 'UC5678901234', CURRENT_TIMESTAMP);

-- INSERT INTO comments (id, content, like_count, video_id, category_id, last_updated_at) VALUES

-- (RANDOM_UUID(), '이 노래 정말 좋아요! 클래식한 명곡이에요.', 1520, 'dQw4w9WgXcQ', 2, CURRENT_TIMESTAMP - INTERVAL '2' DAY),
-- (RANDOM_UUID(), '강남스타일은 여전히 최고의 뮤직비디오예요!', 3750, '9bZkp7q19f0', 2, CURRENT_TIMESTAMP - INTERVAL '1' DAY),
-- (RANDOM_UUID(), '이 건축 트릭들 정말 유용해 보여요. 다음 건축할 때 써봐야겠어요!', 367, 'xC-c7E5PK0Y', 4, CURRENT_TIMESTAMP - INTERVAL '3' HOUR),
-- (RANDOM_UUID(), '이 영화의 특수효과가 정말 대단해요. 어떻게 만들었는지 궁금해요.', 2103, 'dQw4w9WgXcQ', 1, CURRENT_TIMESTAMP - INTERVAL '5' HOUR),
-- (RANDOM_UUID(), '이 코미디 시리즈 너무 재밌어요. 매 에피소드마다 웃음이 끊이질 않아요.', 1876, '9bZkp7q19f0', 5, CURRENT_TIMESTAMP - INTERVAL '8' HOUR),
-- (RANDOM_UUID(), '이 게임 스트리머의 실력이 놀라워요. 어떻게 그렇게 빨리 반응하는지!', 954, 'xC-c7E5PK0Y', 4, CURRENT_TIMESTAMP - INTERVAL '10' HOUR),
-- (RANDOM_UUID(), '이 애니메이션의 그래픽이 너무 아름다워요. 제작 과정이 궁금해요.', 3201, 'dQw4w9WgXcQ', 8, CURRENT_TIMESTAMP - INTERVAL '15' HOUR),
-- (RANDOM_UUID(), '이 쇼의 플롯 트위스트에 완전히 놀랐어요. 다음 시즌이 기대돼요!', 5678, '9bZkp7q19f0', 18, CURRENT_TIMESTAMP - INTERVAL '20' HOUR),
-- (RANDOM_UUID(), '이 공포 영화 예고편만 봐도 무서워요. 극장에서 볼 용기가 있을지 모르겠어요.', 789, 'xC-c7E5PK0Y', 14, CURRENT_TIMESTAMP - INTERVAL '1' DAY),
-- (RANDOM_UUID(), '이 뮤지컬 넘버가 계속 머릿속에서 맴돌아요. 노래가 너무 중독적이에요.', 4321, 'dQw4w9WgXcQ', 2, CURRENT_TIMESTAMP - INTERVAL '2' DAY),

-- (RANDOM_UUID(), '블랙홀에 대한 설명이 정말 이해하기 쉬웠어요. 감사합니다!', 892, 'uD4izuDMUQA', 21, CURRENT_TIMESTAMP - INTERVAL '12' HOUR),
-- (RANDOM_UUID(), '이 수학 개념 설명 덕분에 드디어 이해가 갔어요. 정말 감사합니다!', 1234, 'uD4izuDMUQA', 20, CURRENT_TIMESTAMP - INTERVAL '4' HOUR),
-- (RANDOM_UUID(), '역사에 대한 새로운 시각을 얻었어요. 이런 교육 콘텐츠가 더 많아졌으면 좋겠어요.', 2345, 'uD4izuDMUQA', 20, CURRENT_TIMESTAMP - INTERVAL '6' HOUR),
-- (RANDOM_UUID(), '화학 실험 영상이 너무 재미있어요. 집에서도 안전하게 할 수 있는 실험이 있나요?', 876, 'uD4izuDMUQA', 21, CURRENT_TIMESTAMP - INTERVAL '9' HOUR),
-- (RANDOM_UUID(), '언어 학습 팁 정말 유용해요. 덕분에 외국어 공부에 자신감이 생겼어요.', 1654, 'uD4izuDMUQA', 20, CURRENT_TIMESTAMP - INTERVAL '11' HOUR),
-- (RANDOM_UUID(), '이 철학 강의 시리즈 너무 좋아요. 삶에 대해 깊이 생각하게 됐어요.', 3210, 'uD4izuDMUQA', 20, CURRENT_TIMESTAMP - INTERVAL '14' HOUR),
-- (RANDOM_UUID(), '코딩을 이렇게 쉽게 설명해주다니! 비전공자도 이해할 수 있어요.', 2109, 'uD4izuDMUQA', 21, CURRENT_TIMESTAMP - INTERVAL '18' HOUR),
-- (RANDOM_UUID(), '환경 문제에 대해 자세히 알게 됐어요. 작은 것부터 실천해야겠어요.', 1807, 'uD4izuDMUQA', 21, CURRENT_TIMESTAMP - INTERVAL '22' HOUR),
-- (RANDOM_UUID(), '이 경제학 개념 설명 영상 덕분에 뉴스를 더 잘 이해할 수 있게 됐어요.', 1506, 'uD4izuDMUQA', 20, CURRENT_TIMESTAMP - INTERVAL '1' DAY),
-- (RANDOM_UUID(), '우주에 대한 이 다큐멘터리는 정말 눈을 뗄 수가 없었어요. 더 많이 알고 싶어졌어요.', 4029, 'uD4izuDMUQA', 21, CURRENT_TIMESTAMP - INTERVAL '2' DAY),

-- (RANDOM_UUID(), 'YouTube의 첫 번째 동영상이라니, 역사적인 순간이네요.', 10483, 'jNQXAC9IVRw', 26, CURRENT_TIMESTAMP - INTERVAL '6' HOUR),
-- (RANDOM_UUID(), '이 요리 레시피 영상 덕분에 처음으로 완벽한 저녁을 만들었어요!', 3456, 'jNQXAC9IVRw', 28, CURRENT_TIMESTAMP - INTERVAL '2' HOUR),
-- (RANDOM_UUID(), '홈트레이닝 동작을 잘 보여줘서 따라하기 쉬웠어요. 건강해지는 것 같아요.', 2345, 'jNQXAC9IVRw', 24, CURRENT_TIMESTAMP - INTERVAL '7' HOUR),
-- (RANDOM_UUID(), '여행 팁 영상 너무 유용해요. 다음 여행 계획에 꼭 참고하겠습니다.', 1876, 'jNQXAC9IVRw', 25, CURRENT_TIMESTAMP - INTERVAL '13' HOUR),
-- (RANDOM_UUID(), '반려동물 훈련법 영상 덕분에 우리 강아지가 말을 잘 듣네요!', 3210, 'jNQXAC9IVRw', 23, CURRENT_TIMESTAMP - INTERVAL '16' HOUR),
-- (RANDOM_UUID(), '메이크업 튜토리얼 영상 감사해요. 오늘 파티에서 칭찬 받았어요!', 4567, 'jNQXAC9IVRw', 28, CURRENT_TIMESTAMP - INTERVAL '19' HOUR),
-- (RANDOM_UUID(), '차량 관리 팁 덕분에 제 차의 수명이 연장된 것 같아요. 감사합니다!', 1234, 'jNQXAC9IVRw', 22, CURRENT_TIMESTAMP - INTERVAL '21' HOUR),
-- (RANDOM_UUID(), '정원 가꾸기 영상을 보고 용기를 내어 시작했어요. 이제 제 작은 정원이 생겼어요!', 2109, 'jNQXAC9IVRw', 26, CURRENT_TIMESTAMP - INTERVAL '1' DAY),
-- (RANDOM_UUID(), '이 패션 하울 영상 덕분에 새로운 스타일에 도전하게 됐어요.', 3698, 'jNQXAC9IVRw', 28, CURRENT_TIMESTAMP - INTERVAL '2' DAY),
-- (RANDOM_UUID(), '명상 가이드 영상 정말 도움 돼요. 매일 조금씩 마음의 평화를 찾고 있어요.', 1506, 'jNQXAC9IVRw', 26, CURRENT_TIMESTAMP - INTERVAL '3' DAY);