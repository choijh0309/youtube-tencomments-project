## TenComments : Real-time YouTube top comments tracker by category
## 화면 기획
<img width="563" alt="image" src="https://github.com/user-attachments/assets/093e1af8-17b0-4d32-b9d0-3a59f9991c59">

Youtube 의 Trending 카테고리를 클릭하면 Now, Music, Gaming, Movies 총 4개의 카테고리로 분류됩니다.
기존 기획은 4개를 모두 구현하는 것을 목표로 삼았지만, Youtube data api 에서 Movies(30번) 는 지원하지 않아 3개 카테고리로 기획을 변경했습니다.

## DB 및 클래스 구조
<img width="753" alt="image" src="https://github.com/user-attachments/assets/8081424e-9c95-42cd-94ac-dea6b268a7b1">

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
## 리팩터링
## 개선할 점 (확장성)
- 테스트 코드 작성
- 대댓글 기능 추가
- 검색 기능 추가
- 카테고리 늘리기
