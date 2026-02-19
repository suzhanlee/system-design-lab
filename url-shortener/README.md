# URL Shortener

URL 단축 서비스 - 긴 URL을 짧은 URL로 변환하는 시스템

## 개요

Twitter나 Bitly 같은 URL 단축 서비스를 구현합니다. 사용자가 긴 URL을 입력하면 고유한 단축 URL을 생성하고, 단축 URL로 접속하면 원본 URL로 리다이렉트합니다.

## 핵심 설계 포인트

### 1. 단축 URL 생성 전략
- **해시 + 충돌 해결**: 긴 URL을 해싱하여 단축 키 생성
- **Base62 인코딩**: 0-9, a-z, A-Z 조합으로 짧은 키 생성
- **미리 생성**: 오프라인에서 키를 미리 생성해두고 할당

### 2. 데이터 모델
```
short_key (PK) | original_url | created_at | expiration_date
```

### 3. 캐시 전략
- **Redis**: 자주 접속하는 URL 캐싱
- **LRU**: Least Recently Used 정책으로 캐시 관리

### 4. 확장성
- **샤딩**: short_key 범위별로 DB 분산
- **CDN**: 리다이렉트 응답 캐싱

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| ORM | Spring Data JPA |
| Cache | Redis (Lettuce) |
| Encoding | Base62 |
| Database | H2 (개발), PostgreSQL (운영) |

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/urls | 단축 URL 생성 |
| GET | /{shortKey} | 원본 URL로 리다이렉트 |
| GET | /api/v1/urls/{shortKey}/stats | 접속 통계 조회 |

## 학습 목표

- [ ] 해시 함수와 충돌 처리 이해
- [ ] Base62 인코딩 구현
- [ ] Redis 캐시 전략 학습
- [ ] URL 리다이렉션 처리
- [ ] 분산 환경에서의 고유 키 생성

## 참고 자료

- 책 1장: URL 단축기 설계
- [Base62 인코딩](https://en.wikipedia.org/wiki/Base62)
