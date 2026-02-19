# Search Engine

검색 엔진 - 전문 검색 및 자동완성 시스템

## 개요

Google 검색 같은 검색 엔진을 구현합니다. 전문 검색, 자동완성, 랭킹 알고리즘을 포함합니다.

## 핵심 설계 포인트

### 1. 인덱싱
- **역색인 (Inverted Index)**: 단어 → 문서 매핑
- **Kafka**: 문서 변경 이벤트 수집
- **Bulk Indexing**: 대량 문서 색인

### 2. 검색 쿼리 처리
```
[Query] → [Analyzer] → [Tokenizer] → [Search] → [Rank] → [Results]
```

### 3. 데이터 모델
```
// 문서 (Elasticsearch)
{
  "id": "doc_001",
  "title": "...",
  "content": "...",
  "url": "...",
  "page_rank": 0.85,
  "created_at": "2024-01-01"
}
```

### 4. 랭킹 알고리즘
- **TF-IDF**: 단어 빈도 기반
- **BM25**: 개선된 랭킹 함수
- **PageRank**: 링크 기반 중요도

### 5. 자동완성
- **Trie**: 접두사 검색
- **N-gram**: 부분 매칭
- **Suggester**: 추천어 제안

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| Search Engine | Elasticsearch |
| Message Queue | Apache Kafka |

## 아키텍처

```
[Web Crawler] → [Kafka] → [Indexer]
                            ↓
                    [Elasticsearch]
                            ↓
                    [Search API] → [Client]
```

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/search | 검색 |
| GET | /api/v1/suggest | 자동완성 |
| POST | /api/v1/documents | 문서 색인 |
| DELETE | /api/v1/documents/{id} | 문서 삭제 |

## 학습 목표

- [ ] 역색인 구조 이해
- [ ] Elasticsearch 쿼리 DSL
- [ ] 분석기 및 토크나이저
- [ ] 랭킹 알고리즘
- [ ] 자동완성 구현

## 참고 자료

- 책: 검색 엔진 시스템 설계
- [Elasticsearch 공식 문서](https://www.elastic.co/guide/index.html)
