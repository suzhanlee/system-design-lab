# Web Crawler

웹 크롤러 - 인터넷의 웹페이지를 수집하고 색인하는 시스템

## 개요

Google 검색엔진처럼 웹을 순회하며 페이지를 수집하는 분산 크롤러를 구현합니다.

## 핵심 설계 포인트

### 1. 크롤링 아키텍처
- **Seed URLs**: 시작점 URL 관리
- **URL Frontier**: 수집할 URL 큐
- **Worker Nodes**: 병렬 크롤링 워커

### 2. URL 관리
```
url_hash (PK) | url | status | last_crawled | next_crawl | priority
```

### 3. 중복 방지
- **URL 정규화**: URL 표준화
- **콘텐츠 해시**: 중복 페이지 감지
- **Robots.txt**: 크롤링 정책 준수

### 4. 확장성
- **Kafka**: URL 큐 및 크롤링 결과 전달
- **파티셔닝**: 도메인별 크롤링 분산
- **Rate Limiting**: 서버 부하 방지

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| Message Queue | Apache Kafka |
| HTML Parser | Jsoup |
| HTTP Client | OkHttp / WebClient |

## 아키텍처

```
[Seed URLs] → [URL Frontier (Kafka)]
                    ↓
            [Crawler Workers]
                    ↓
            [Content Parser (Jsoup)]
                    ↓
            [Storage / Indexer]
```

## 학습 목표

- [ ] BFS/DFS 크롤링 전략
- [ ] 분산 크롤링 구현
- [ ] URL 정규화 및 중복 제거
- [ ] Rate Limiting 및 Politeness
- [ ] Kafka를 활용한 메시지 처리

## 참고 자료

- 책: 검색엔진 크롤러 설계
- [Jsoup 문서](https://jsoup.org/)
- [Kafka 공식 문서](https://kafka.apache.org/documentation/)
