# Critique Report: ADR-002 (캐싱 전략)

## 개요
- **ADR**: ADR-002. 캐싱 전략 선택
- **검토 일시**: 2026-02-27
- **전체 위험도**: MEDIUM

---

## 이슈 목록

### [SEC-1] Redis 접근 보안 미명시 x
- **관점**: Security
- **심각도**: MEDIUM
- **설명**: Redis에 대한 인증(AUTH), TLS 암호화, 네트워크 격리 설정이 명시되지 않음
- **영향**: 캐시 데이터 탈취, 무단 접근 가능성
- **제안**:
  - Redis AUTH 설정 (requirepass)
  - VPC 내부망에서만 접근 허용
  - 운영 환경에서는 TLS 활성화 검토

### [SEC-2] 캐시 오염(Cache Poisoning) 가능성 x
- **관점**: Security
- **심각도**: LOW
- **설명**: 악의적인 사용자가 단축 URL을 생성한 후 원본 URL을 변경하는 방식의 공격이 가능할 수 있음 (현재 설계에서는 URL 변경 불가하므로 낮음)
- **영향**: 사용자를 악성 사이트로 리다이렉트
- **제안**:
  - 단축 URL 생성 후 원본 URL 변경 불가 정책 명시
  - (선택) 관리자용 URL 변경 기능은 별도 인증 필요

### [PERF-1] Cache Miss 시 P99 지연 시간 ~ 서버 켜질 때 cache warm-up 전략 정도만 사용
- **관점**: Performance
- **심각도**: MEDIUM
- **설명**: Look-Aside 방식은 Cache Miss 시 DB 조회(~50ms)가 발생. 트래픽 스파이크 시 P99가 100ms 목표를 초과할 수 있음
- **영향**: 일부 사용자 경험 저하
- **제안**:
  - 인기 URL에 대한 선제적 캐시 워밍업
  - P99 모니터링 및 알림 설정

### [PERF-2] 캐시 스탬피드 완화 전략 구체화 필요 x
- **관점**: Performance
- **심각도**: LOW
- **설명**: "랜덤 딜레이 적용"만으로는 충분하지 않을 수 있음
- **영향**: 특정 URL에 대한 갑작스러운 트래픽 증가 시 DB 과부하
- **제안**:
  - Singleflight 패턴 또는 Cache Lock 적용
  - stale-while-revalidate 패턴 검토

### [SCALE-1] Redis 단일 인스턴스 SPOF x
- **관점**: Scalability
- **심각도**: MEDIUM
- **설명**: Redis 단일 인스턴스 사용 시 장애 발생 시 전체 캐시 손실. Circuit Breaker로 DB 폴백하지만 DB 부하 급증
- **영향**: 서비스 성능 급격한 저하
- **제안**:
  - Redis Sentinel 또는 Cluster 도입 검토 (DAU 50만 시점)
  - 로컬 캐시(Caffeine)를 2계층 캐시로 추가 검토

### [SCALE-2] 캐시 메모리 용량 계획 부재 v
- **관점**: Scalability
- **심각도**: LOW
- **설명**: 24시간 TTL 시 최대 캐시 엔트리 수와 필요 메모리 양이 명시되지 않음
- **영향**: 메모리 부족으로 인한 eviction 발생 가능
- **제안**:
  - 일일 활성 URL 수 기반 메모리 용량 산정
  - Redis maxmemory 및 eviction 정책 설정 (allkeys-lru)

### [REL-1] Cache Warm-up 구체적 절차 미정의 - 이건 조회수 기반 warm up 전략 사용 상위 1000개
- **관점**: Reliability
- **심각도**: LOW
- **설명**: Redis 복구 후 Cache Warm-up 절차가 구체적으로 정의되지 않음
- **영향**: 복구 후 초기 캐시 히트율 저하
- **제안**:
  - 인기 URL Top N을 선제적으로 로드하는 스크립트 준비
  - 또는 트래픽 기반 자동 워밍업 허용

---

## 요약

| 관점 | 이슈 수 | HIGH | MEDIUM | LOW |
|------|---------|------|--------|-----|
| Security | 2 | 0 | 1 | 1 |
| Performance | 2 | 0 | 1 | 1 |
| Scalability | 2 | 0 | 1 | 1 |
| Maintainability | 0 | 0 | 0 | 0 |
| Business | 0 | 0 | 0 | 0 |
| Reliability | 1 | 0 | 0 | 1 |
| **Total** | **7** | **0** | **3** | **4** |

---

## 권장 사항

1. **[권장] SEC-1 해결**: Redis 보안 설정 명시 (AUTH, VPC 격리)
2. **[권장] SCALE-1 해결**: Redis 고가용성 아키텍처를 로드맵에 추가 (Sentinel/Cluster)
3. **[고려] PERF-1/PERF-2**: 인기 URL 선제적 워밍업 및 스탬피드 방지 패턴 적용

---

## ADR 수정 제안

### 추가할 섹션: Infrastructure Considerations

```markdown
## Infrastructure Considerations

### Redis 보안
- AUTH 설정 필수 (requirepass)
- VPC 내부망에서만 접근 허용
- 운영 환경 TLS 검토

### 고가용성 (향후)
- DAU 50만 시 Redis Sentinel 도입 검토
- 또는 Redis Cluster (3노드 이상)

### 모니터링 필수 지표
- 캐시 적중률 (목표 95%)
- P99 리다이렉트 응답시간
- Redis 메모리 사용률
```
