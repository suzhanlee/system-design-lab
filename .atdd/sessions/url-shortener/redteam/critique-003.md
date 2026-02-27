# Critique Report: ADR-003 (접속 통계 저장 방식)

## 개요
- **ADR**: ADR-003. 접속 통계 저장 방식 선택
- **검토 일시**: 2026-02-27
- **전체 위험도**: MEDIUM

---

## 이슈 목록

### [SEC-1] 조회수 조작(Injection) 가능성 - v ip 기반 중복 카운트 방지 사용
- **관점**: Security
- **심각도**: LOW
- **설명**: 사용자가 자신의 URL 조회수를 인위적으로 높일 수 있음 (봇, 스크립트 활용)
- **영향**: 통계 신뢰도 저하, 비즈니스 의사결정 오류
- **제안**:
  - IP 기반 중복 카운트 방지 (같은 IP는 N분간 1회만 카운트)
  - User-Agent 필터링 (봇 탐지)
  - Rate Limiting 적용

### [PERF-1] 대량 Bulk UPDATE 성능 이슈 - jdbc batch update 사용
- **관점**: Performance
- **심각도**: MEDIUM
- **설명**: 1시간마다 전체 조회수를 DB로 동기화할 때, URL 수가 100만 개 이상이면 UPDATE 쿼리가 장시간 실행되어 DB 부하 유발
- **영향**: 동기화 시간 동안 DB 성능 저하, Lock 대기
- **제안**:
  - 변경된 키만 스캔 (Redis SCAN + dirty flag)
  - Batch UPDATE 사용 (JDBC Batch)
  - 동기화 시간을 트래픽이 적은 시간대로 지정

### [PERF-2] Redis 전체 스캔 부하 - scan 방식 사용
- **관점**: Performance
- **심각도**: MEDIUM
- **설명**: `Redis 전체 스캔 → DB Bulk UPDATE` 방식은 `KEYS *` 명령어 사용 시 Redis 블로킹 발생 가능
- **영향**: 스캔 중 Redis 응답 지연
- **제안**:
  - SCAN 명령어 사용 (커서 기반)
  - 또는 변경된 키만 별도 Set으로 관리

### [SCALE-1] 동기화 스케줄러 SPOF - x 실패 시 fail log 정도 수집으로 끝
- **관점**: Scalability
- **심각도**: HIGH
- **설명**: 동기화 스케줄러가 단일 인스턴스에서 실행되며, 장애 시 1시간치 데이터 영구 유실
- **영향**: 통계 데이터 손실
- **제안**:
  - Spring Scheduler에 클러스터 모드 적용 (ShedLock)
  - 또는 스케줄러 장애 감지 및 알림
  - 최소 2대 인스턴스 운영

### [SCALE-2] Redis 조회수 키 무한 증가 - v 자동 삭제 ㄱ
- **관점**: Scalability
- **심각도**: MEDIUM
- **설명**: 새로운 URL이 계속 생성되면 조회수 키(`url:shortCode:views`)가 무한히 증가. 7일 TTL이 있지만 활성 URL은 계속 유지됨
- **영향**: Redis 메모리 지속적 증가
- **제안**:
  - 조회수 키에도 TTL 설정 (예: 30일 후 자동 삭제, 조회 시 복구)
  - 메모리 사용량 모니터링 및 알림

### [MAINT-1] 통계 정확도 검증 방법 부재 - v
- **관점**: Maintainability
- **심각도**: LOW
- **설명**: Redis와 DB 간 통계 정합성을 검증하는 방법이 없음
- **영향**: 데이터 불일치 발견 지연
- **제안**:
  - 일일 정합성 검사 배치 (Redis vs DB 비교)
  - 불일치 시 로그 및 알림

### [BIZ-1] "실제 조회수와 다를 수 있음" 안내의 UX 영향 - 동기화 주기 10분으로 단축
- **관점**: Business
- **심각도**: LOW
- **설명**: 사용자에게 "조회수는 실제와 다를 수 있습니다" 안내는 서비스 신뢰도 저하 가능성
- **영향**: 사용자 만족도 저하
- **제안**:
  - 대안: 실시간 조회수 대신 "최근 1시간 전 기준" 명시
  - 또는 동기화 주기를 30분으로 단축하여 지연 최소화

### [REL-1] Circuit Breaker 상태 복구 후 데이터 동기화 - redis cache 날리고 warm-up 전략 사용
- **관점**: Reliability
- **심각도**: MEDIUM
- **설명**: Redis 장애로 DB 폴백 중 발생한 조회수 증가가 Redis 복구 후 어떻게 처리되는지 불명확
- **영향**: 장애 복구 후 데이터 불일치
- **제안**:
  - 폴백 중 DB에 기록된 조회수를 Redis로 역동기화하는 로직 필요
  - 또는 복구 후 Redis 카운터를 DB 값으로 초기화

---

## 요약

| 관점 | 이슈 수 | HIGH | MEDIUM | LOW |
|------|---------|------|--------|-----|
| Security | 1 | 0 | 0 | 1 |
| Performance | 2 | 0 | 2 | 0 |
| Scalability | 2 | 1 | 1 | 0 |
| Maintainability | 1 | 0 | 0 | 1 |
| Business | 1 | 0 | 0 | 1 |
| Reliability | 1 | 0 | 1 | 0 |
| **Total** | **8** | **1** | **4** | **3** |

---

## 권장 사항

1. **[긴급] SCALE-1 해결**: 스케줄러 고가용성 확보 (ShedLock 또는 클러스터 모드)
2. **[권장] PERF-1/PERF-2 해결**: SCAN 기반 동기화 및 Batch UPDATE 적용
3. **[고려] SEC-1**: IP 기반 중복 방지로 조회수 조작 방지

---

## ADR 수정 제안

### 수정할 섹션: 구체적 구현 방식

```markdown
### 구체적 구현 방식 (수정)

1. 리다이렉트 발생 → Redis INCR (url:shortCode:views)
2. **변경 키 추적**: 변경된 shortCode를 별도 Set에 추가 (url:dirty)
3. 조회수 조회 → Redis GET (있으면 반환, 없으면 DB 조회)
4. **1시간마다 스케줄러 실행**:
   - SCAN으로 `url:dirty` Set 순회
   - 변경된 키만 Batch UPDATE
   - 완료 후 `url:dirty` Set 비우기
5. Redis 장애 시 → Circuit Breaker → DB 직접 UPDATE
6. Redis 복구 시:
   - DB에서 최근 활성 URL 조회수 로드
   - Redis 카운터 초기화
   - `url:dirty` Set 초기화
```

### 추가할 섹션: Scheduler High Availability

```markdown
## Scheduler High Availability

### 문제
단일 스케줄러 장애 시 최대 1시간치 데이터 유실

### 해결책
1. **ShedLock 적용**: 분산 환경에서 하나의 인스턴스만 스케줄러 실행
2. **장애 감지**: 스케줄러 실행 로그 모니터링, 미실행 시 알림
3. **수동 복구**: 관리자용 수동 동기화 API 제공
```
