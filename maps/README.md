# Maps Service

지도 서비스 - 위치 기반 검색 및 내비게이션 시스템

## 개요

Google Maps 같은 지도 서비스를 구현합니다. 장소 검색, 경로 탐색, 실시간 교통 정보를 포함합니다.

## 핵심 설계 포인트

### 1. 지리 데이터 모델링
```
// 장소
place_id (PK) | name | location (Point) | category | address | rating

// 도로
road_id (PK) | geometry (LineString) | name | length | speed_limit
```

### 2. 공간 인덱싱
- **PostGIS**: 공간 데이터 처리
- **GiST Index**: 지리 인덱스
- **GeoHash**: 위치 해싱

### 3. 위치 기반 검색
- **반경 검색**: 주변 장소 찾기
- **지오코딩**: 주소 ↔ 좌표 변환
- **카테고리 필터**: 음식점, 카페 등

### 4. 경로 탐색
- **Dijkstra**: 최단 경로
- **A* Algorithm**: 휴리스틱 경로 탐색
- **Real-time Traffic**: 실시간 교통 반영

### 5. 캐싱 전략
- **Redis Geo**: 위치 데이터 캐시
- **Tile Cache**: 지도 타일 캐싱
- **Route Cache**: 자주 찾는 경로

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| Spatial DB | PostgreSQL + PostGIS |
| ORM | Hibernate Spatial |
| Cache | Redis (GEO) |

## 아키텍처

```
[Client] → [Geocoding Service]
       → [Places API] → [PostGIS]
       → [Routing Service] → [Graph DB]
       → [Tile Server] → [Map Tiles]
```

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/places/nearby | 주변 장소 검색 |
| GET | /api/v1/places/{id} | 장소 상세 |
| GET | /api/v1/geocode | 지오코딩 |
| GET | /api/v1/directions | 경로 탐색 |
| GET | /api/v1/route/optimize | 경로 최적화 |

## 학습 목표

- [ ] 공간 데이터 타입 (Point, Polygon)
- [ ] PostGIS 공간 쿼리
- [ ] 지오코딩 구현
- [ ] A* 경로 탐색 알고리즘
- [ ] Redis GEO 활용

## 참고 자료

- 책: 지도 서비스 시스템 설계
- [PostGIS 문서](https://postgis.net/documentation/)
- [Redis GEO Commands](https://redis.io/commands/geoadd)
