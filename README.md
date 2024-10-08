## UI
<img width="400" alt="프로젝트캡쳐3" src="https://github.com/user-attachments/assets/5a2bb88c-66cb-42e6-8405-e5fffc98d799">
<img width="400" alt="프로젝트캡쳐4" src="https://github.com/user-attachments/assets/dc3184ce-0101-428c-95a9-fbd3540eafa1">


## TenComments : Real-time YouTube top comments tracker by category
- 이 프로젝트는 사람들의 현재 생각을 알고자 하는 **개인적 궁금증**에서 시작하였습니다.
- *유튜브*를 선택한 이유: 높은 트래픽과 다양한 의견 수집이 가능하다고 판단하였습니다.

## 배포링크
[http://158.247.196.113:8080/ ](http://158.247.196.113:8080/)

## 주요 기능
1. 인기급상승(TRENDING) 카테고리 활용
   - 매시간 댓글 업데이트로 최신 트렌드 및 의견을 파악하실 수 있습니다.
   - 24시간 지속된 댓글은 삭제되도록 구현해 좀 더 트랜디한 댓글을 볼 수 있습니다.
2. 조회 전용 기능
   - 수정, 삭제 기능 없이 댓글 관찰에 집중할 수 있도록 하였습니다.
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

이 변경으로 인해 데이터베이스 쿼리 수가 크게 줄었습니다.(N + 1 문제 해결) 성능이 향상되고, 필요한 데이터만 한번에 가져와 메모리 사용량이 줄었습니다. 프로젝트 실행 속도가 약 30% 개선됐습니다.

### channel.thumbnail null 문제

YoutubeService.java 파일의 getPopularVideosByCategory 메소드에 ChannelDto를 생성할 때 thumbnailUrl을 설정하지 않아 채널 썸네일 URL Null 문제가 발생했습니다.

채널의 썸네일 URL을 설정하고, 채널 정보를 추가해 문제를 해결했습니다.

### Scheduler 24시간 / 1시간 업데이트
  
CommentService에서 24시간 이전 업데이트 댓글을 삭제했습니다.

CommentScheduler에서 @Scheduled(cron = "0 0 * * * *")을 이용해 매시간 댓글을 업데이트하고 삭제했습니다.

## 개선할 점 
소프트웨어에서 가장 중요한 점은 **업데이트**와 **확장** 이라고 생각합니다. 소프트웨어는 기능이 작동된다고 끝이 아닙니다.

서두에서 언급했듯이 이 프로젝트의 목표는 사람의 생각을 알고 분석하는 것입니다. 저는 프로젝트를 구상할 때 업데이트와 확장에 중점을 두고 제한된 시간에 우선 핵심 기능을 구현했습니다. 핵심 기능을 구현했으니 이용자가 더 개인적인 관심을 찾아볼 수 있게 기능을 추가할 예정입니다.

### 대댓글 기능 추가
본 댓글 외에도 대댓글을 보면 사람들의 공감을 많이 얻는 댓글을 볼 수 있습니다. 그 중 몇개는 본 댓글보다 더 많은 공감을 얻습니다. 대댓글 기능 구현을 통해 댓글 간 소통을 분석할 수 있습니다. 

### 검색 기능 추가
사람들이 개인적으로 찾는 동영상에 대한 댓글을 가져오기 위해 검색 기능을 업데이트 할 예정입니다.

### 카테고리 늘리기
유튜브는 약 40여개 카테고리를 제공합니다. 그 중 api를 제공하지 하지 않는 카테고리를 제외하고 사람들이 관심이 많은 카테고리를 업데이트할 예정입니다.

### api 할당량 최소화
Youtube data api 하루 할당량은 10,000인데 V3DataCommentThreadService.List 를 호출하는 로직을 개선했지만 아직 가장 많은 할당량을 차지합니다. 이 부분 로직을 리팩토링 할 계획입니다.

### 테스트 코드 작성 및 API 문서화
## git commit 메시지 규약
- feat : 새로운 기능을 추가하였을 때
- fix : 버그를 수정하였을 때
- docs : README 등 문서 내용을 변경하였을 때
- style : 들여쓰기, 세미콜론 등을 변경하였을 때
- refactor : 코드 리팩토링을 했을 때(기능의 변경은 없어야 한다.)
- test : test코드의 작성 및 수정이 이루어졌을 때
- chore : 외부 라이브러리 임포트 등의 작업을 완료했을 때
## 프로젝트 기간
2024.7.20 ~ 2024.9.1(최초 배포일)

2024.9.2 ~ 업데이트 및 확장 중입니다.


