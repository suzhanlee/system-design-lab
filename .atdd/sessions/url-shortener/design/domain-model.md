# URL Shortener Domain Model

## Overview

URL Shortener 시스템의 도메인 모델을 DDD 관점에서 정의합니다.

---

## Bounded Context

```
┌─────────────────────────────────────────────────────────┐
│                  URL Shortener Context                   │
├─────────────────────────────────────────────────────────┤
│  Aggregates:                                             │
│  - ShortUrl (Root) ⊃ Statistics (Embedded)              │
│  - VisitLog (별도 Aggregate)                             │
│                                                          │
│  Domain Services:                                        │
│  - ShortUrlGenerator                                     │
│                                                          │
│  Domain Events:                                          │
│  - UrlVisitedEvent                                       │
│  - ShortUrlDeletedEvent                                  │
└─────────────────────────────────────────────────────────┘
```

---

## Aggregates

### 1. ShortUrl Aggregate

```
┌─────────────────────────────────────────────────────────┐
│                    ShortUrl Aggregate                    │
│                      (Aggregate Root)                    │
├─────────────────────────────────────────────────────────┤
│  Entity: ShortUrl                                        │
│  - id: Long                                              │
│  - originalUrl: OriginalUrl (VO)                         │
│  - shortCode: ShortCode (VO)                             │
│  - statisticsId: Long                                    │
│  - createdAt: LocalDateTime                              │
│  - deletedAt: LocalDateTime                              │
│                                                          │
│  Behavior:                                               │
│  + create(url, code, statsId): ShortUrl                  │
│  + softDelete(): void                                    │
│  + isDeleted(): boolean                                  │
└─────────────────────────────────────────────────────────┘
                           │
                           │ 1:1 (ID 참조)
                           ▼
┌─────────────────────────────────────────────────────────┐
│                       Statistics                         │
├─────────────────────────────────────────────────────────┤
│  - id: Long                                              │
│  - shortUrlId: Long                                      │
│  - visitCount: Long                                      │
│  - lastVisitedAt: LocalDateTime                          │
│                                                          │
│  Behavior:                                               │
│  + create(shortUrlId): Statistics                        │
│  + increment(): void                                     │
│  + updateLastVisitedAt(time): void                       │
└─────────────────────────────────────────────────────────┘
```

**Invariant Rules:**
- ShortUrl과 Statistics는 반드시 함께 생성/삭제된다
- shortCode는 6~7자 Base62 형식이어야 한다
- deletedAt이 설정되면 isDeleted()는 true를 반환한다

---

### 2. VisitLog Aggregate (별도)

```
┌─────────────────────────────────────────────────────────┐
│                    VisitLog Aggregate                    │
│                      (Aggregate Root)                    │
├─────────────────────────────────────────────────────────┤
│  - id: Long                                              │
│  - shortUrlId: Long                                      │
│  - visitedAt: LocalDateTime                              │
│  - ipAddress: String                                     │
│  - userAgent: String                                     │
│                                                          │
│  Behavior:                                               │
│  + create(shortUrlId, ip, userAgent): VisitLog           │
└─────────────────────────────────────────────────────────┘
```

**특징:**
- ShortUrl 삭제와 독립적으로 생명주기를 가짐
- Historical Data로 보존됨

---

## Value Objects

### OriginalUrl
```
┌─────────────────────────────────────────────────────────┐
│                     OriginalUrl                          │
├─────────────────────────────────────────────────────────┤
│  - value: String                                         │
│                                                          │
│  Validation:                                             │
│  - http/https 프로토콜 필수                               │
│  - 최대 2048자                                            │
│  - URL 형식 검증                                          │
│                                                          │
│  Behavior:                                               │
│  + of(value): OriginalUrl                                │
│  + isValidFormat(): boolean                              │
└─────────────────────────────────────────────────────────┘
```

### ShortCode
```
┌─────────────────────────────────────────────────────────┐
│                      ShortCode                           │
├─────────────────────────────────────────────────────────┤
│  - value: String                                         │
│                                                          │
│  Validation:                                             │
│  - 6~7자 Base62 (a-z, A-Z, 0-9)                          │
│                                                          │
│  Behavior:                                               │
│  + of(value): ShortCode                                  │
│  + isValidFormat(): boolean                              │
└─────────────────────────────────────────────────────────┘
```

---

## Domain Services

### ShortUrlGenerator

```
┌─────────────────────────────────────────────────────────┐
│                   ShortUrlGenerator                      │
│                  (Domain Service)                        │
├─────────────────────────────────────────────────────────┤
│  Algorithm: SHA-256 Hash → Base62 Encoding              │
│                                                          │
│  Behavior:                                               │
│  + generate(url): ShortCode                              │
│  + regenerate(url, attempt): ShortCode                   │
│                                                          │
│  Rules:                                                  │
│  - 해시 충돌 시 최대 3회 재시도                            │
│  - attempt 값을 suffix로 추가하여 재해시                  │
└─────────────────────────────────────────────────────────┘
```

---

## Domain Events

### UrlVisitedEvent
```
┌─────────────────────────────────────────────────────────┐
│                    UrlVisitedEvent                       │
├─────────────────────────────────────────────────────────┤
│  - shortUrlId: Long                                      │
│  - statisticsId: Long                                    │
│  - visitedAt: LocalDateTime                              │
│  - ipAddress: String                                     │
│  - userAgent: String                                     │
│                                                          │
│  Handler: StatisticsEventHandler                         │
│  → Statistics.increment()                                │
└─────────────────────────────────────────────────────────┘
```

### ShortUrlDeletedEvent
```
┌─────────────────────────────────────────────────────────┐
│                  ShortUrlDeletedEvent                    │
├─────────────────────────────────────────────────────────┤
│  - shortUrlId: Long                                      │
│  - statisticsId: Long                                    │
│                                                          │
│  Handler: StatisticsDeletionHandler                      │
│  → StatisticsRepository.deleteById()                     │
└─────────────────────────────────────────────────────────┘
```

---

## Event Flow

```
┌──────────┐     ┌──────────────────┐     ┌─────────────┐
│  Client  │────▶│  RedirectService │────▶│  ShortUrl   │
└──────────┘     └──────────────────┘     └─────────────┘
                          │
                          │ publish
                          ▼
               ┌──────────────────────┐
               │   UrlVisitedEvent    │
               └──────────────────────┘
                    /           \
                   /             \
                  ▼               ▼
        ┌──────────────┐  ┌──────────────┐
        │  Statistics  │  │   VisitLog   │
        │   Handler    │  │   Handler    │
        └──────────────┘  └──────────────┘
              │                  │
              ▼                  ▼
        ┌──────────────┐  ┌──────────────┐
        │ increment()  │  │   save()     │
        └──────────────┘  └──────────────┘
```

---

## Specifications

### UrlFormatSpecification
```
┌─────────────────────────────────────────────────────────┐
│               UrlFormatSpecification                     │
├─────────────────────────────────────────────────────────┤
│  Business Rules:                                         │
│  - http/https 프로토콜만 허용                             │
│  - 유효한 URL 형식이어야 함                               │
│  - 최대 길이 2048자                                       │
│                                                          │
│  Behavior:                                               │
│  + isSatisfiedBy(url): boolean                           │
│  + validate(url): void // throws InvalidUrlException     │
└─────────────────────────────────────────────────────────┘
```

---

## Package Structure

```
lab.systemdesign.urlshortener/
├── domain/
│   ├── entity/
│   │   ├── ShortUrl.java          # Aggregate Root
│   │   ├── Statistics.java        # 1:1 Entity
│   │   └── VisitLog.java          # 별도 Aggregate
│   ├── vo/
│   │   ├── OriginalUrl.java       # 원본 URL VO
│   │   └── ShortCode.java         # 단축 코드 VO
│   ├── service/
│   │   └── ShortUrlGenerator.java # Domain Service
│   ├── event/
│   │   ├── UrlVisitedEvent.java   # 방문 이벤트
│   │   └── ShortUrlDeletedEvent.java # 삭제 이벤트
│   └── specification/
│       └── UrlFormatSpecification.java
├── repository/
│   ├── ShortUrlRepository.java
│   ├── StatisticsRepository.java
│   └── VisitLogRepository.java
└── exception/
    └── InvalidUrlException.java
```
