# Email Service

이메일 서비스 - 대량 이메일 발송 및 관리 시스템

## 개요

마케팅 이메일, 트랜잭셔널 이메일 등 대량으로 이메일을 발송하고 추적하는 시스템입니다.

## 핵심 설계 포인트

### 1. 이메일 발송 파이프라인
- **Kafka**: 이메일 발송 요청 큐잉
- **Worker Pool**: 병렬 이메일 발송
- **Retry**: 실패 시 재시도 정책

### 2. 데이터 모델
```
// 이메일
email_id (PK) | subject | body | sender | recipients | status | created_at

// 발송 로그
log_id (PK) | email_id | recipient | status | sent_at | error_message
```

### 3. 검색 및 분석
- **Elasticsearch**: 이메일 검색 및 분석
- **메트릭**: 발송율, 오픈율, 클릭율

### 4. 신뢰성
- **Idempotency**: 중복 발송 방지
- **Dead Letter Queue**: 처리 실패 이메일 격리
- **Throttling**: 발송 속도 제어

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| Mail | Spring Mail (SMTP) |
| Message Queue | Apache Kafka |
| Search | Elasticsearch |

## 아키텍처

```
[Email Request] → [Kafka Topic]
                        ↓
                [Email Workers]
                        ↓
                [SMTP Server]
                        ↓
            [Elasticsearch] ← [Logs]
```

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/emails | 이메일 발송 요청 |
| GET | /api/v1/emails/{id}/status | 발송 상태 조회 |
| GET | /api/v1/emails/search | 이메일 검색 |
| GET | /api/v1/emails/stats | 발송 통계 |

## 학습 목표

- [ ] 비동기 이메일 발송
- [ ] Kafka 기반 메시지 큐
- [ ] Elasticsearch 검색 구현
- [ ] 재시도 및 에러 처리
- [ ] 이메일 템플릿 엔진

## 참고 자료

- 책: 이메일 서비스 설계
- [Spring Mail 문서](https://docs.spring.io/spring-framework/reference/integration/email.html)
