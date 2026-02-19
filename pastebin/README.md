# Pastebin

텍스트 저장 및 공유 서비스 - 코드나 텍스트를 저장하고 공유하는 시스템

## 개요

Pastebin은 사용자가 텍스트나 코드를 업로드하고, 고유한 URL을 통해 다른 사람과 공유할 수 있는 서비스입니다.

## 핵심 설계 포인트

### 1. 키 생성 전략
- **UUID vs 커스텀 키**: 읽기 쉬운 키 생성
- **충돌 방지**: 분산 환경에서의 고유성 보장

### 2. 데이터 저장 전략
```
id (PK) | content | content_type | created_at | expiration | visibility
```

### 3. 만료 정책
- **TTL (Time To Live)**: Redis 활용한 자동 만료
- **저장소 계층화**: 자주 접속하는 Paste는 캐시

### 4. 접근 제어
- **Public**: 누구나 접근 가능
- **Private**: 생성자만 접근
- **Unlisted**: URL을 아는 사람만 접근

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| ORM | Spring Data JPA |
| Cache | Redis |
| Database | H2 (개발), PostgreSQL (운영) |

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/pastes | 새 Paste 생성 |
| GET | /api/v1/pastes/{id} | Paste 조회 |
| DELETE | /api/v1/pastes/{id} | Paste 삭제 |
| GET | /api/v1/pastes | 내 Paste 목록 |

## 학습 목표

- [ ] 대용량 텍스트 저장 전략
- [ ] TTL 기반 데이터 만료
- [ ] 접근 제어 구현
- [ ] 캐시 일관성 유지
- [ ] 스토리지 비용 최적화

## 참고 자료

- Pastebin 시스템 설계 (책 참조)
- [Pastebin.com](https://pastebin.com)
