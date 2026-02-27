# Edge Case 식별 워크시트

## 목적
예외 케이스를 최소 5개 이상 식별하여 테스트 커버리지를 높입니다.

---

## 작성 체크리스트

- [ ] 입력 검증: 2개 이상 (필수값 누락, 형식 오류)
- [ ] 비즈니스 규칙: 1개 이상 (중복, 상태 위반)
- [ ] 권한/인증: 1개 이상
- [ ] 경계값: 1개 이상 (최소/최대)
- [ ] 총 5개 이상 작성 완료

---

## Feature 1: URL 단축 - Edge Cases

### Edge Case 1-1: 유효하지 않은 URL 형식
```
When: POST /api/v1/shorten with invalid URL
When: wwww.google.com 으로 요청이 온다.
Then: 400 status code and "Invalid URL" message"
```

### Edge Case 1-2: 필수값 누락
```
When: POST /api/v1/shorten without originalUrl
When: shorten api 로 empty/null url 이 들어온다.
Then: 400 status code and "Original URL is required" message
```

### Edge Case 1-3: URL 길이 경계값
```
When: www.(a*200).com 이 들어온다. (200자 넘어가는 url 경계)
Then: 400 status code and "Original URL is too long" message
```

---

## Feature 2: 리다이렉트 - Edge Cases

### Edge Case 2-1: 존재하지 않는 단축 URL
```
Given: db에 shorten url 값이 www.short.com 이 존재한다.
When: www.short2.com 으로 요청을 보낸다.
Then: 400 status code and "Short URL not found" message
```

### Edge Case 2-2: (추가 Edge Case)
```
Given: db에 shorten url 값이 www.short.com 이 존재한다.
When: long url을 바꾸는 요청을 한다.
Then: 같은 short url이 나와 새로운 short url 을 만들어 반환한다. + status 200
```

---

## Feature 3: 통계 조회 - Edge Cases

### Edge Case 3-1: 존재하지 않는 단축 URL 통계 조회
```
Given: db 에 www.short.com 이라는 url이 존재한다.
When: GET /api/v1/stats/www.short2.com
Then: 400 status code and "Short URL not found" message
```

---

## 예외 케이스 카테고리 참고

| 카테고리 | 예시 |
|----------|------|
| 입력 검증 실패 | 필수값 누락, 잘못된 형식, 길이 제한 초과 |
| 비즈니스 규칙 위반 | 중복 데이터, 상태 위반, 권한 부족 |
| 외부 의존성 실패 | API 타임아웃, DB 연결 실패 |
| 동시성 문제 | 동시 수정, 레이스 컨디션 |
| 경계값 | 최소/최대값, 빈 컬렉션, null |

---

## 요구사항 기반 예외 케이스 힌트

| 요구사항 | 가능한 예외 |
|----------|-------------|
| URL 유효성 검증 | 잘못된 형식, 빈 값, null |
| 단축 URL 생성 | URL 형식 오류 |
| 리다이렉트 | 존재하지 않는 코드, 만료된 URL |
| 통계 조회 | 존재하지 않는 코드 |

---

## 작성 완료 체크

최소 5개 이상의 Edge Case를 식별했는지 확인하세요.

| 카테고리 | 개수 |
|----------|------|
| 입력 검증 | 0 |
| 비즈니스 규칙 | 0 |
| 권한/인증 | 0 |
| 경계값 | 0 |
| **합계** | **0** |

식별 완료 후 **"완료"** 또는 **"다음"**이라고 입력해주세요.
Phase C (Generation)로 진행합니다.
