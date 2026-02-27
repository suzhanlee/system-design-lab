# 정제된 요구사항

## 프로젝트명
URL Shortener (URL 단축 서비스)

## 비즈니스 목표
- 긴 URL을 짧고 직관적인 URL로 변환하는 서비스 제공
- 사용자가 긴 URL을 입력하면 고유한 단축 URL을 생성하고, 접속 시 원본 URL로 라우팅

---

## 기능 요구사항

### Must have
- [ ] **URL 단축**: 긴 URL을 입력받아 15자 이내의 고유한 단축 URL 생성
- [ ] **리다이렉트**: 단축 URL 접속 시 원본 URL로 301 영구 리다이렉트
- [ ] **충돌 처리**: 단축 URL 중복 발생 시 자동 재생성
- [ ] **URL 유효성 검증**: 입력받은 URL이 유효한 형식인지 검증

### Should have
- [ ] **접속 통계**: 단축 URL별 접속 횟수, 접속 일시 등 통계 제공
- [ ] **통계 조회 API**: 생성된 단축 URL의 통계 조회 기능

### Could have
- [ ] 커스텀 단축 URL: 사용자가 원하는 키워드로 단축 URL 생성
- [ ] QR 코드 생성: 단축 URL에 대한 QR 코드 자동 생성

### Won't have (이번 버전)
- [ ] 만료 기간: 단축 URL의 유효 기간 설정 (불필요)
- [ ] 사용자 계정: 회원가입/로그인 기능
- [ ] URL 수정/삭제: 생성된 단축 URL 관리 기능

---

## API 명세 (추가)

### URL 단축
```
POST /api/v1/shorten
Request:
{
  "originalUrl": "https://example.com/very/long/url/..."
}

Response:
{
  "shortUrl": "https://sho.rt/abc123",
  "originalUrl": "https://example.com/very/long/url/...",
  "createdAt": "2026-02-27T00:00:00Z"
}
```

### 리다이렉트
```
GET /{shortCode}
Response: 301 Redirect to originalUrl
```

### 통계 조회
```
GET /api/v1/stats/{shortCode}
Response:
{
  "shortUrl": "https://sho.rt/abc123",
  "originalUrl": "...",
  "visitCount": 1234,
  "createdAt": "2026-02-27T00:00:00Z",
  "lastVisitedAt": "2026-02-27T12:00:00Z"
}
```

---

## 예외 케이스 (추가)

| 시나리오 | HTTP Status | 응답 |
|----------|-------------|------|
| 유효하지 않은 URL 형식 | 400 Bad Request | `{ "error": "Invalid URL format" }` |
| 존재하지 않는 단축 URL | 404 Not Found | `{ "error": "Short URL not found" }` |
| URL 단축 서버 오류 | 500 Internal Server Error | `{ "error": "Internal server error" }` |
| 네트워크 타임아웃 | 504 Gateway Timeout | `{ "error": "Request timeout" }` |

---

## 비기능 요구사항

### 성능
- DAU 10만 규모 지원 (중간 규모 트래픽)
- 단축 URL 생성 응답 시간: 500ms 이내
- 리다이렉트 응답 시간: 100ms 이내
- **(추가) 캐싱 전략**: Redis 도입으로 리다이렉트 성능 확보

### 확장성
- 향후 트래픽 증가에 대비한 수평 확장 가능 구조

### 가용성
- 99.9% 서비스 가용성 목표

---

## 기술적 제약
- Java / Spring Boot / JPA 사용
- 데이터베이스: RDBMS (MySQL 권장)
- **(추가) 캐싱**: Redis (권장)
- RESTful API 설계

---

## ADR 필요 항목 (검증 결과)

| 항목 | 결정 필요 사항 | 우선순위 |
|------|----------------|----------|
| 단축 URL 생성 알고리즘 | Base62 vs Hash vs Sequence | 높음 |
| 캐싱 전략 | Redis 도입 여부 및 캐싱 정책 | 높음 |
| 접속 통계 저장 방식 | Redis vs DB vs 로그 분석 | 중간 |

---

## 작성 정보
- 원본 작성일: 2026-02-27
- 정제 일시: 2026-02-27
- 버전: 1.1 (검증 후 개선)
