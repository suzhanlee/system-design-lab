# URL Shortener ERD (Entity Relationship Diagram)

## Overview

URL Shortener 시스템의 물리적 데이터 모델을 정의합니다.

---

## Entity Relationship Diagram

```
┌─────────────────────────┐         ┌─────────────────────────┐
│       short_url         │         │       statistics        │
├─────────────────────────┤         ├─────────────────────────┤
│ id (PK)                 │◄───────┐│ id (PK)                 │
│ original_url (VARCHAR)  │        ││ short_url_id (FK, UQ)   │──┐
│ short_code (VARCHAR, UQ)│        │└─────────────────────────┘  │
│ statistics_id (FK)      │────────┼──────────────────────────────┘
│ created_at (TIMESTAMP)  │        │
│ deleted_at (TIMESTAMP)  │        │ 1:1 관계 (ID 참조)
└─────────────────────────┘
                                      ┌─────────────────────────┐
                                      │       visit_log         │
                                      ├─────────────────────────┤
                                      │ id (PK)                 │
                                      │ short_url_id (FK)       │
                                      │ visited_at (TIMESTAMP)  │
                                      │ ip_address (VARCHAR)    │
                                      │ user_agent (VARCHAR)    │
                                      └─────────────────────────┘
                                        별도 Aggregate (보존)
```

---

## Entity Details

### 1. short_url

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 고유 식별자 |
| original_url | VARCHAR(2048) | NOT NULL | 원본 URL |
| short_code | VARCHAR(7) | NOT NULL, UNIQUE | 단축 코드 (Base62, 6~7자) |
| statistics_id | BIGINT | FK, NULLABLE | 통계 ID (1:1 관계) |
| created_at | TIMESTAMP | NOT NULL | 생성 일시 |
| deleted_at | TIMESTAMP | NULLABLE | 삭제 일시 (soft delete) |

**Indexes:**
- `idx_short_url_code` ON (short_code) - 조회용
- `idx_short_url_original` ON (original_url) - 중복 체크용

---

### 2. statistics

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 고유 식별자 |
| short_url_id | BIGINT | FK, NOT NULL, UNIQUE | ShortUrl ID (1:1) |
| visit_count | BIGINT | NOT NULL, DEFAULT 0 | 총 방문 횟수 |
| last_visited_at | TIMESTAMP | NULLABLE | 마지막 방문 일시 |

**Indexes:**
- `uk_statistics_short_url_id` ON (short_url_id) - 1:1 관계 보장

---

### 3. visit_log

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 고유 식별자 |
| short_url_id | BIGINT | FK, NOT NULL | ShortUrl ID |
| visited_at | TIMESTAMP | NOT NULL | 방문 일시 |
| ip_address | VARCHAR(45) | NULLABLE | 클라이언트 IP (IPv6 지원) |
| user_agent | VARCHAR(512) | NULLABLE | 브라우저/기기 정보 |

**Indexes:**
- `idx_visit_log_short_url_id` ON (short_url_id) - 조회용
- `idx_visit_log_visited_at` ON (visited_at) - 시간 범위 조회용

---

## Relationship Rules

### ShortUrl : Statistics (1:1)
- ShortUrl 생성 시 Statistics도 함께 생성
- ShortUrl soft delete 시 → ShortUrlDeletedEvent 발행 → Statistics hard delete
- ID 참조 방식 (양방향 FK)

### ShortUrl : VisitLog (1:N)
- VisitLog는 별도 Aggregate
- ShortUrl 삭제와 무관하게 보존됨 (Historical Data)

---

## Redis Cache Schema

```
Key: shortUrl:{shortCode}
Value: {
  "id": 1,
  "originalUrl": "https://example.com/...",
  "shortCode": "abc123",
  "statisticsId": 1,
  "createdAt": "2026-02-27T10:00:00"
}
TTL: 24 hours
```

---

## Design Decisions

1. **ID 참조 방식**: ShortUrl과 Statistics 간 양방향 FK로 1:1 관계 보장
2. **Soft Delete**: ShortUrl만 soft delete, Statistics는 hard delete
3. **VisitLog 보존**: 방문 이력은 분석용으로 보존
4. **인덱스 전략**: 조회 빈도가 높은 short_code, visited_at에 인덱스 적용
