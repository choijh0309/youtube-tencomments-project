## TenComments : Real-time YouTube top comments tracker by category
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
- FetchType.LAZY 프론트 화면에 나오지 않는 이슈
- 성능 개선위해 캐싱 고민
- 영화 api 지원 X
- 좋아요 수 로직
- N + 1 문제
- channel.thumbnail null 문제
- YouTube api 할당량 문제
- Scheduler 24시간 / 1시간 업데이트
- 카테고리 초기화 이슈
## 개선할 점 (확장성)
- 테스트 코드 작성
- 대댓글 기능 추가
- 검색 기능 추가
- 카테고리 늘리기
