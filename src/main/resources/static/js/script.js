document.addEventListener('DOMContentLoaded', function() {
    const categoryButtons = document.querySelectorAll('.category-buttons button');
    const categoryTitle = document.getElementById('categoryTitle');
    const commentList = document.getElementById('commentList');
    let currentCategory = 'now';

    const categoryMap = {
        'now': 1,
        'music': 2,
        'gaming': 3
    };

    categoryButtons.forEach(button => {
        button.addEventListener('click', function() {
            currentCategory = this.dataset.category;
            loadComments(currentCategory);
            updateCategoryTitle(currentCategory);
            updateActiveButton(this);
        });
    });

    loadComments(currentCategory);
    updateCategoryTitle(currentCategory);

    function loadComments(category) {
        showLoading();
        updateCategoryTitle(category);

        fetch(`/api/comments/top/${categoryMap[category]}`)
            .then(handleResponse)
            .then(comments => {
                if (comments.length === 0) {
                    showNoComments();
                } else {
                    displayComments(comments);
                }
            })
            .catch(handleError);
    }

    function handleResponse(response) {
        if (!response.ok) {
            throw new Error('서버 응답이 올바르지 않습니다');
        }
        return response.json();
    }

    function showLoading() {
        commentList.innerHTML = '<p>댓글을 불러오는 중...</p>';
    }

    function updateCategoryTitle(category) {
        categoryTitle.textContent = `${capitalize(category)} 인기 댓글`;
    }

    function showNoComments() {
        commentList.innerHTML = '<p>이 카테고리에 댓글이 없습니다.</p>';
    }

    function handleError(error) {
        console.error('댓글 로딩 중 오류 발생:', error);
        commentList.innerHTML = '<p>댓글을 불러오는 데 실패했습니다. 다시 시도해 주세요.</p>';
    }

    function displayComments(comments) {
        commentList.innerHTML = '';
        comments.forEach((comment, index) => {
            const commentCard = createCommentCard(comment, index + 1);
            commentList.appendChild(commentCard);
        });
    }

    function createCommentCard(comment, rank) {
        const card = document.createElement('div');
        card.className = 'col-12 mb-4';

        card.innerHTML = `
            <div class="card">
                <div class="card-body">
                    <div class="row align-items-center">
                        <div class="col-md-1 col-3 text-center">
                            <h5 class="comment-rank">${rank}위</h5>
                            <p class="comment-likes">
                                <span class="likes-count">${comment.likeCount}</span><br>
                                <span class="likes-text">좋아요</span>
                            </p>
                        </div>
                        <div class="col-md-2 col-9 text-center">
                            <img src="${comment.video.thumbnailUrl}" alt="${comment.video.title}" class="img-fluid video-thumbnail">
                        </div>
                        <div class="col-md-7 col-12">
                            <h5 class="video-title">${comment.video.title}</h5>
                            <p class="comment-content">${comment.content}</p>
                        </div>
                        <div class="col-md-2 col-12">
                            <div class="channel-info">
                                <a href="https://www.youtube.com/channel/${comment.video.channel.id}" target="_blank">
                                    <img src="${comment.video.channel.thumbnailUrl}" alt="${comment.video.channel.title}" class="channel-image">
                                </a>
                                <span class="channel-name">${comment.video.channel.title}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;

        const thumbnailImg = card.querySelector('.video-thumbnail');
        thumbnailImg.style.opacity = '0';
        thumbnailImg.onload = function() {
            this.style.opacity = '1';
        };
        thumbnailImg.onerror = function() {
            this.onerror = null;
            console.log('Thumbnail load failed for:', comment.video.id);
        };

        thumbnailImg.addEventListener('click', (e) => {
            e.preventDefault();
            openVideoPlayer(comment.video.id);
        });

        return card;
    }

    function openVideoPlayer(videoId) {
        const videoPlayer = document.createElement('div');
        videoPlayer.className = 'video-player';
        videoPlayer.innerHTML = `
            <div class="video-player-content">
                <iframe src="https://www.youtube.com/embed/${videoId}" frameborder="0" allowfullscreen></iframe>
                <button class="close-video">닫기</button>
            </div>
        `;

        document.body.appendChild(videoPlayer);

        const closeButton = videoPlayer.querySelector('.close-video');
        closeButton.addEventListener('click', () => {
            document.body.removeChild(videoPlayer);
        });

        videoPlayer.addEventListener('click', (e) => {
            if (e.target === videoPlayer) {
                document.body.removeChild(videoPlayer);
            }
        });
    }

    function updateActiveButton(clickedButton) {
        categoryButtons.forEach(button => button.classList.remove('active'));
        clickedButton.classList.add('active');
    }

    function capitalize(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }

    setInterval(() => {
        loadComments(currentCategory);
    }, 300000);
});