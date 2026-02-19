# News Feed

뉴스 피드 - Facebook/Twitter 스타일의 소셜 피드 시스템

## 개요

사용자가 게시물을 작성하면 팔로워들의 피드에 실시간으로 반영되는 소셜 미디어 피드 시스템입니다.

## 핵심 설계 포인트

### 1. 피드 모델링
- **Push Model**: 게시물을 팔로워 피드에 미리 복사 (쓰기 시 복사)
- **Pull Model**: 피드 요청 시 팔로잉 게시물 조회 (읽기 시 조인)
- **Hybrid**: 팔로워 수에 따라 전략 분기

### 2. 데이터 모델
```
// 사용자
user_id (PK) | username | created_at

// 게시물
post_id (PK) | user_id | content | created_at

// 팔로우
follower_id | followee_id | created_at

// 피드 (캐시)
user_id | post_id | created_at
```

### 3. 확장성
- **Kafka**: 게시물 이벤트 스트리밍
- **Redis**: 피드 캐싱
- **샤딩**: 사용자/게시물 분산

### 4. 성능 최적화
- **페이지네이션**: 커서 기반
- **캐시 워밍**: 인기 게시물 미리 로드
- **비동기 처리**: 팬아웃 비동기화

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| ORM | Spring Data JPA |
| Cache | Redis |
| Message Queue | Apache Kafka |

## 아키텍처 (Fan-out)

```
[Post Created] → [Kafka Topic]
                      ↓
              [Fan-out Workers]
              ↓              ↓
        [Redis Feed A] [Redis Feed B]
```

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/posts | 게시물 작성 |
| GET | /api/v1/feed | 내 피드 조회 |
| POST | /api/v1/follow/{userId} | 팔로우 |
| DELETE | /api/v1/follow/{userId} | 언팔로우 |
| GET | /api/v1/users/{userId}/posts | 사용자 게시물 |

## 학습 목표

- [ ] Push vs Pull vs Hybrid 피드 모델
- [ ] Fan-out 패턴 구현
- [ ] Kafka 이벤트 스트리밍
- [ ] Redis Sorted Set 활용
- [ ] 커서 기반 페이지네이션

## 참고 자료

- 책: 뉴스 피드 시스템 설계
- [Redis Sorted Sets](https://redis.io/docs/data-types/sorted-sets/)
