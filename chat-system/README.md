# Chat System

채팅 시스템 - 실시간 1:1 및 그룹 채팅 서비스

## 개요

WhatsApp이나 Slack 같은 실시간 채팅 서비스를 구현합니다. 1:1 채팅과 그룹 채팅을 모두 지원합니다.

## 핵심 설계 포인트

### 1. 메시징 아키텍처
- **WebSocket**: 실시간 양방향 통신
- **STOMP**: 메시징 프로토콜
- **Redis Pub/Sub**: 분산 메시지 브로커

### 2. 데이터 모델
```
// 채팅방
room_id (PK) | name | type (1:1/group) | created_at

// 메시지
message_id (PK) | room_id | sender_id | content | timestamp

// 멤버십
room_id | user_id | last_read_timestamp
```

### 3. 연결 관리
- **Connection Pool**: WebSocket 연결 관리
- **Heartbeat**: 연결 상태 확인
- **재연결**: 자동 재연결 처리

### 4. 보안
- **JWT**: 사용자 인증
- **방 권한**: 채팅방 접근 제어
- **메시지 암호화**: E2E 암호화 (확장)

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| Real-time | WebSocket + STOMP |
| Message Broker | Redis Pub/Sub |
| Auth | JWT + Spring Security |

## 아키텍처

```
[Client A] ←→ [WebSocket Server] ←→ [Client B]
                    ↓
            [Redis Pub/Sub]
                    ↓
            [Message Storage]
```

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/auth/login | 로그인 |
| GET | /api/v1/rooms | 채팅방 목록 |
| POST | /api/v1/rooms | 채팅방 생성 |
| WS | /ws/chat | WebSocket 연결 |
| SUB | /topic/rooms/{id} | 채팅방 구독 |

## 학습 목표

- [ ] WebSocket 기반 실시간 통신
- [ ] Redis Pub/Sub 메시지 브로커
- [ ] JWT 인증 구현
- [ ] 온라인/오프라인 상태 관리
- [ ] 읽음 표시 기능

## 참고 자료

- 책: 채팅 시스템 설계
- [Spring WebSocket 가이드](https://spring.io/guides/gs/messaging-stomp-websocket)
