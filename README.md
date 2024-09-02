## TenComments : Real-time YouTube top comments tracker by category
- 배포 링크: http://158.247.196.113:8080/ (도메인 변경 예정)
- 사람들의 현재 생각을 알고자 하는 **개인적 궁금증**에서 시작하였습니다.
- *유튜브*를 선택한 이유: 높은 트래픽과 다양한 의견 수집이 가능하다고 판단하였습니다.

## 주요 기능
1. 인기급상승(TRENDING) 카테고리 활용
   - 매시간 댓글 업데이트로 최신 트렌드 및 의견을 파악하실 수 있습니다.
   - 24시간 지속된 댓글은 삭제되도록 구현해 좀 더 트랜디한 댓글을 볼 수 있습니다.
2. 조회 전용 기능
   - 수정, 삭제 기능 없이 순수한 의견 관찰에 집중할 수 있도록 하였습니다.
3. 데이터 균형
   - 영상당 좋아요 수 최다 댓글 하나만 표시하여 편중을 방지하였습니다.
4. 사용자 편의성
   - 썸네일 클릭으로 영상을 직접 시청하실 수 있습니다.
   - 채널 썸네일 클릭으로 해당 채널을 방문하실 수 있습니다.

## 프로젝트 목표
최신 트렌드와 그에 대한 다양한 의견을 효과적으로 관찰하고 분석하실 수 있는 플랫폼을 제공하고자 합니다.

## 화면 기획
<img width="563" alt="image" src="https://github.com/user-attachments/assets/093e1af8-17b0-4d32-b9d0-3a59f9991c59">

Youtube 의 Trending 카테고리를 클릭하면 Now, Music, Gaming, Movies 총 4개의 카테고리로 분류됩니다.
기존 기획은 4개를 모두 구현하는 것을 목표로 삼았지만, Youtube data api 에서 Movies(30번) 는 지원하지 않아 3개 카테고리로 기획을 변경했습니다.

## DB 및 클래스 구조
<img width="856" alt="image" src="https://github.com/user-attachments/assets/0c42c4a6-2ea5-4e22-8025-a6cc2cbd9031">

<img width="1010" alt="image" src="https://github.com/user-attachments/assets/5a48d3dd-5590-473f-89a6-f5b9d83adfda">

개발 시작 전 최소한의 설계 진행 후, 프로젝트 진행하면서 최종 수정 구조를 완성했습니다.
## git commit 메시지 규약
- feat : 새로운 기능을 추가하였을 때
- fix : 버그를 수정하였을 때
- docs : README 등 문서 내용을 변경하였을 때
- style : 들여쓰기, 세미콜론 등을 변경하였을 때
- refactor : 코드 리팩토링을 했을 때(기능의 변경은 없어야 한다.)
- test : test코드의 작성 및 수정이 이루어졌을 때
- chore : 외부 라이브러리 임포트 등의 작업을 완료했을 때
## 문제 해결 과정
### FetchType.LAZY 프론트 화면에 나오지 않는 이슈
<img width="638" alt="image" src="https://github.com/user-attachments/assets/7e25ed45-b6fa-4c91-b2d9-89dccb47eebd">

위 이미지처럼 Video의 썸네일이 화면은 나오지 않았지만, 썸네일이 나올 자리에 마우스를 클릭하면 영상 재생은 됐습니다. 처음엔 프론트엔드 코드 문제인 줄 알았지만 Comment 엔티티와 Video 간의 관계 설정 문제였습니다.

처음엔 fetch 타입이 LAZY로 설정되어 있어, Comment를 조회할 때 Video 정보를 즉시 가져오지 않았습니다. LAZY 대신 EAGER로 Comment 엔티티의 Video 관계를 변경해 해결했습니다.

### Youtube Data Api 에서 영화 api 지원하지 않는 이슈 

다른 카테고리(NOW, MUSIC, GAMING)은 데이터가 다 들어갔지만 영화 카테고리는 불러오지 못했습니다. 로직을 바꿔도 들어가지 않아 youtube data api reference를 참고하니 영화는 지원하지 않아 카테고리에서 제외했습니다.

또한 영화는 스포일러 등 문제로 영화 제작사 요청으로 댓글을 막는 경우가 많아 카테고리에서 제외하는 게 맞다고 판단했습니다.

### 좋아요 수 로직
처음에 최상단 댓글만 가져오니 좋아요 수가 가장 많지 않은 댓글이 프로젝트에 적용됐습니다.

대신 상위 100개(YouTube API의 제한으로 인해 한 번에 가져올 수 있는 최대 댓글 수)의 댓글을 가져온 후 CommentDto 객체로 변환해 좋아요 수를 기준으로 가장 높은 댓글을 선택하게 로직을 수정했습니다.

### N + 1 문제

기능 구현은 했지만 프로젝트를 실행할 때 시간이 오래 걸려 문제를 분석했는데 N + 1 문제였습니다. 

CommentRepository에 새 메서드(findTopCommentsByCategoryAndLastUpdatedAfterWithFetchJoin)를 추가해 JPQL(Java Persistence Query Language)을 사용하여 최적화된 쿼리를 실행했습니다. CommentService의 getTopCommentsByCategory 메서드에 추가한 메서드를 적용했습니다. 

이 변경으로 인해 데이터베이스 쿼리 수가 크게 줄었습니다.(N + 1 문제 해결) 성능이 향상되고, 필요한 데이터만 한번에 가져와 메모리 사용량이 줄었습니다. 아쉬운 점은 수치로 기록을 하지 못한 점입니다.

### channel.thumbnail null 문제

YoutubeService.java 파일의 getPopularVideosByCategory 메소드에 ChannelDto를 생성할 때 thumbnailUrl을 설정하지 않아 채널 썸네일 URL Null 문제가 발생했습니다.

채널의 썸네일 URL을 설정하고, 채널 정보를 추가해 문제를 해결했습니다.

### Scheduler 24시간 / 1시간 업데이트
  
CommentService에서 24시간 이전 업데이트 댓글을 삭제했습니다.

CommentScheduler에서 @Scheduled(cron = "0 0 * * * *")을 이용해 매시간 댓글을 업데이트하고 삭제했습니다.

## 개선할 점 (확장성)
- 테스트 코드 작성
- 대댓글 기능 추가
- 검색 기능 추가
- 카테고리 늘리기
