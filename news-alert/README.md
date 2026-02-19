# News Alert

뉴스 알림 서비스 - 키워드 기반 실시간 뉴스 알림 시스템

## 개요

사용자가 등록한 키워드와 관련된 뉴스가 발행되면 실시간으로 알림을 보내는 시스템입니다.

## 핵심 설계 포인트

### 1. 알림 구독 모델
```
subscription_id (PK) | user_id | keywords | channels | created_at
```

### 2. 실시간 푸시
- **WebSocket**: 실시간 양방향 통신
- **Server-Sent Events (SSE)**: 단방향 실시간 업데이트
- **폴링**: 폴백 메커니즘

### 3. 스케줄링
- **Quartz**: 뉴스 수집 스케줄링
- **Cron Expression**: 유연한 스케줄 설정
- **분산 락**: 중복 실행 방지

### 4. 알림 채널
- **WebSocket**: 웹 브라우저
- **Email**: 이메일 알림
- **Push**: 모바일 푸시

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| Real-time | WebSocket |
| Scheduling | Quartz |
| Database | H2 (개발) |

## 아키텍처

```
[News Sources] → [Collector (Scheduled)]
                        ↓
                [Keyword Matcher]
                        ↓
            [Notification Dispatcher]
                   ↓         ↓
            [WebSocket]   [Email/Push]
```

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/subscriptions | 알림 구독 생성 |
| GET | /api/v1/subscriptions | 내 구독 목록 |
| DELETE | /api/v1/subscriptions/{id} | 구독 취소 |
| GET | /ws/alerts | WebSocket 연결 |

## 학습 목표

- [ ] WebSocket 실시간 통신
- [ ] 키워드 매칭 알고리즘
- [ ] Quartz 스케줄링
- [ ] 알림 전송 신뢰성 보장
- [ ] 다중 채널 알림

## 참고 자료

- 책: 뉴스 알림 시스템 설계
- [Spring WebSocket](https://docs.spring.io/spring-framework/reference/web/websocket.html)
