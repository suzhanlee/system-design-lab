# URL Shortener Traceability Matrix

## Overview

요구사항 → 설계 요소 → 테스트 시나리오 간의 추적성을 관리합니다.

---

## Requirements Traceability

### Functional Requirements

| ID | Requirement | Domain Element | Test Scenario |
|----|-------------|----------------|---------------|
| FR-01 | URL 단축 기능 | ShortUrl.create(), ShortUrlGenerator | `단축_URL_생성_성공` |
| FR-02 | 중복 URL 처리 | ShortUrlRepository.findByOriginalUrl() | `중복_URL_단축_시_기존_코드_반환` |
| FR-03 | 단축 코드 충돌 처리 | ShortUrlGenerator.regenerate() | `코드_충돌_시_재생성` |
| FR-04 | 리다이렉트 기능 | ShortUrlRepository.findByShortCode() | `단축_URL_조회_성공` |
| FR-05 | Soft Delete | ShortUrl.softDelete() | `단축_URL_삭제_성공` |
| FR-06 | 접속 통계 | Statistics.increment() | `방문_통계_증가` |
| FR-07 | 접속 로그 | VisitLog.create() | `접속_로그_기록` |
| FR-08 | 삭제된 URL 접근 차단 | ShortUrl.isDeleted() | `삭제된_URL_접근_시_404` |

---

### Non-Functional Requirements

| ID | Requirement | Design Decision | ADR |
|----|-------------|-----------------|-----|
| NFR-01 | 고유성 보장 (6~7자) | Base62 인코딩 (56억~3.5조 조합) | ADR-001 |
| NFR-02 | 빠른 조회 | Redis Look-Aside Cache (24h TTL) | ADR-002 |
| NFR-03 | 통계 정합성 | Event-driven 동기 처리 | ADR-003 |
| NFR-04 | 데이터 보존 | VisitLog 별도 Aggregate | - |

---

## Design Elements Traceability

### Entities

| Entity | Attributes | Methods | Related VO | Related Event |
|--------|------------|---------|------------|---------------|
| ShortUrl | id, originalUrl, shortCode, statisticsId, createdAt, deletedAt | create(), softDelete(), isDeleted() | OriginalUrl, ShortCode | ShortUrlDeletedEvent |
| Statistics | id, shortUrlId, visitCount, lastVisitedAt | create(), increment(), updateLastVisitedAt() | - | UrlVisitedEvent |
| VisitLog | id, shortUrlId, visitedAt, ipAddress, userAgent | create() | - | - |

### Value Objects

| VO | Validation | Used By |
|----|------------|---------|
| OriginalUrl | http/https, 2048자 제한 | ShortUrl, UrlFormatSpecification |
| ShortCode | 6~7자 Base62 | ShortUrl, ShortUrlGenerator |

### Domain Services

| Service | Method | Input | Output | Repository Used |
|---------|--------|-------|--------|-----------------|
| ShortUrlGenerator | generate() | OriginalUrl | ShortCode | - |
| ShortUrlGenerator | regenerate() | OriginalUrl, int | ShortCode | - |

### Specifications

| Specification | Method | Validates |
|---------------|--------|-----------|
| UrlFormatSpecification | isSatisfiedBy() | OriginalUrl 형식 |
| UrlFormatSpecification | validate() | OriginalUrl 형식 (throws) |

---

## Event Flow Traceability

### UrlVisitedEvent

```
Trigger: RedirectService.redirect()
     │
     ▼
Event: UrlVisitedEvent.of(shortUrlId, statisticsId, ip, userAgent)
     │
     ├──▶ Handler: StatisticsEventHandler
     │         │
     │         ▼
     │    Statistics.increment()
     │
     └──▶ Handler: VisitLogEventHandler
              │
              ▼
         VisitLogRepository.save()
```

### ShortUrlDeletedEvent

```
Trigger: ShortUrl.softDelete()
     │
     ▼
Event: ShortUrlDeletedEvent.of(shortUrlId, statisticsId)
     │
     ▼
Handler: StatisticsDeletionHandler
     │
     ▼
StatisticsRepository.deleteById()
```

---

## Test Scenario Coverage

| Scenario | Domain Elements Tested | Status |
|----------|------------------------|--------|
| 단축_URL_생성_성공 | ShortUrl.create(), ShortCode.of() | TODO |
| 중복_URL_단축_시_기존_코드_반환 | ShortUrlRepository.findByOriginalUrl() | TODO |
| 잘못된_URL_형식_예외 | OriginalUrl.of(), UrlFormatSpecification | TODO |
| 코드_충돌_시_재생성 | ShortUrlGenerator.regenerate() | TODO |
| 리다이렉트_성공 | ShortUrlRepository.findByShortCode() | TODO |
| 방문_통계_증가 | Statistics.increment(), UrlVisitedEvent | TODO |
| 접속_로그_기록 | VisitLog.create() | TODO |
| 단축_URL_삭제_성공 | ShortUrl.softDelete(), ShortUrlDeletedEvent | TODO |
| 삭제된_URL_접근_시_404 | ShortUrl.isDeleted() | TODO |

---

## ADR References

| ADR | Decision | Impact |
|-----|----------|--------|
| ADR-001 | Hash + Base62 + Sequence | ShortUrlGenerator 구현 |
| ADR-002 | Redis Look-Aside + TTL 24h | ShortUrlRepository 캐싱 |
| ADR-003 | Event-driven + 동기 처리 | Statistics 업데이트 방식 |

---

## Implementation Checklist

- [ ] **Phase 1: Domain Layer**
  - [ ] OriginalUrl VO
  - [ ] ShortCode VO
  - [ ] ShortUrl Entity
  - [ ] Statistics Entity
  - [ ] VisitLog Entity
  - [ ] ShortUrlGenerator Service
  - [ ] UrlVisitedEvent
  - [ ] ShortUrlDeletedEvent
  - [ ] UrlFormatSpecification
  - [ ] InvalidUrlException

- [ ] **Phase 2: Repository Interfaces**
  - [ ] ShortUrlRepository
  - [ ] StatisticsRepository
  - [ ] VisitLogRepository

- [ ] **Phase 3: TDD Implementation**
  - [ ] Unit Tests for VOs
  - [ ] Unit Tests for Entities
  - [ ] Unit Tests for Services
  - [ ] Integration Tests for Events
