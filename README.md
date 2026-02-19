# System Design Lab

"가상면접 사례로 배우는 대규모 시스템 설계 기초" 책의 12개 시스템을 직접 구현하며 학습하기 위한 멀티 모듈 프로젝트입니다.

## 🎯 학습 목표

- 대규모 시스템 설계의 핵심 개념 이해
- 분산 시스템 아키텍처 패턴 학습
- 실제 구현을 통한 이론적 지식 검증
- 다양한 기술 스택 경험

## 🛠 기술 스택

| Category | Technology |
|----------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.2 |
| ORM | Spring Data JPA |
| Build Tool | Gradle Kotlin DSL |
| Database | H2 (기본), PostgreSQL, Redis, Elasticsearch |

## 📦 모듈 구성

### 1단계: 입문 (Entry Level)

| Module | Description | Key Technologies |
|--------|-------------|------------------|
| [url-shortener](./url-shortener) | URL 단축 서비스 | Redis, Base62 |
| [pastebin](./pastebin) | 텍스트 저장/공유 서비스 | Redis |

### 2단계: 중급 (Intermediate Level)

| Module | Description | Key Technologies |
|--------|-------------|------------------|
| [web-crawler](./web-crawler) | 웹 크롤러 | Kafka, Jsoup |
| [news-alert](./news-alert) | 뉴스 알림 서비스 | WebSocket, Quartz |
| [chat-system](./chat-system) | 채팅 시스템 | WebSocket, Redis, JWT |

### 3단계: 상급 (Advanced Level)

| Module | Description | Key Technologies |
|--------|-------------|------------------|
| [news-feed](./news-feed) | 뉴스 피드 | Redis, Kafka |
| [email-service](./email-service) | 이메일 서비스 | Kafka, Elasticsearch, Mail |
| [ecommerce](./ecommerce) | 이커머스 플랫폼 | Redisson, Kafka, Security |

### 4단계: 심화 (Expert Level)

| Module | Description | Key Technologies |
|--------|-------------|------------------|
| [youtube](./youtube) | 동영상 스트리밍 | S3, Kafka, JavaCV |
| [google-drive](./google-drive) | 클라우드 스토리지 | S3, WebSocket |
| [search-engine](./search-engine) | 검색 엔진 | Elasticsearch, Kafka |
| [maps](./maps) | 지도 서비스 | Redis, PostGIS |

## 🚀 시작하기

### 사전 요구사항

- JDK 21+
- Gradle 8.x

### 빌드

```bash
# 전체 프로젝트 빌드
./gradlew build

# 특정 모듈 빌드
./gradlew :url-shortener:build
```

### 모듈 목록 확인

```bash
./gradlew projects
```

## 📚 학습 로드맵

자세한 학습 로드맵은 [LEARNING_ROADMAP.md](./LEARNING_ROADMAP.md)를 참조하세요.

## 📖 참고 자료

- [가상면접 사례로 배우는 대규모 시스템 설계 기초](https://www.yes24.com/Product/Goods/102819435)
- [System Design Primer](https://github.com/donnemartin/system-design-primer)

## 📝 라이선스

이 프로젝트는 학습 목적으로만 사용됩니다.
