# 시나리오 요약

## Feature 목록
1. URL 단축 - 4개 시나리오 (1 Happy + 3 Exception)
2. 리다이렉트 - 3개 시나리오 (1 Happy + 2 Exception)
3. 통계 조회 - 2개 시나리오 (1 Happy + 1 Exception)

## 시나리오 통계
- 총 Feature 수: 1 (단일 Feature 파일)
- 총 Scenario 수: 9
- Happy Path: 3
- Exception Path: 6

## 시나리오 상세

### URL 단축
| 시나리오 | 유형 | 상태코드 |
|----------|------|----------|
| 정상적인 URL 단축 요청 | Happy | 200 |
| 유효하지 않은 URL 형식으로 단축 요청 | Exception | 400 |
| 필수값이 누락된 URL 단축 요청 | Exception | 400 |
| URL 길이가 제한을 초과한 단축 요청 | Exception | 400 |

### 리다이렉트
| 시나리오 | 유형 | 상태코드 |
|----------|------|----------|
| 정상적인 리다이렉트 요청 | Happy | 301 |
| 존재하지 않는 단축 URL로 리다이렉트 요청 | Exception | 404 |
| 동일한 원본 URL로 재요청 시 새로운 단축 URL 생성 | Exception | 200 |

### 통계 조회
| 시나리오 | 유형 | 상태코드 |
|----------|------|----------|
| 정상적인 통계 조회 요청 | Happy | 200 |
| 존재하지 않는 단축 URL 통계 조회 | Exception | 404 |

## 커버리지

| 우선순위 | 커버리지 | 상태 |
|----------|----------|------|
| Must Have | 100% (4/4) | ✅ |
| Should Have | 100% (2/2) | ✅ |
| Could Have | 0% | ⚠️ (의도적 제외) |

## 파일 위치
- Feature 파일: `url-shortener/src/test/resources/features/url-shortener.feature`

## 다음 단계
`/adr` 실행 (설계 의사결정 문서화)
