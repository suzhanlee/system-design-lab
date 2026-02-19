# E-commerce

이커머스 플랫폼 - 상품 주문 및 결제 시스템

## 개요

Amazon 같은 이커머스 플랫폼의 핵심 기능인 상품 조회, 장바구니, 주문, 결제를 구현합니다.

## 핵심 설계 포인트

### 1. 상품 및 재고 관리
```
// 상품
product_id (PK) | name | price | stock | created_at

// 재고 이력
log_id (PK) | product_id | quantity_change | reason | created_at
```

### 2. 주문 처리
- **주문 생성**: 트랜잭션으로 원자성 보장
- **분산 락 (Redisson)**: 동시 주문 시 재고 정합성
- **이벤트 소싱**: 주문 상태 변경 이력

### 3. 결제 연동
- **PG 연동**: 결제 게이트웨이 API
- **Webhook**: 결제 결과 수신
- **보상 트랜잭션**: 결제 실패 시 롤백

### 4. 확장성
- **Kafka**: 주문 이벤트 발행
- **CQRS**: 읽기/쓰기 분리
- **Saga**: 분산 트랜잭션 패턴

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| ORM | Spring Data JPA |
| Distributed Lock | Redisson |
| Message Queue | Apache Kafka |
| Security | Spring Security |

## 아키텍처

```
[User] → [Order Service] → [Kafka] → [Inventory Service]
              ↓                           ↓
        [Payment Service]          [Notification Service]
```

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/products | 상품 목록 |
| GET | /api/v1/products/{id} | 상품 상세 |
| POST | /api/v1/cart | 장바구니 추가 |
| POST | /api/v1/orders | 주문 생성 |
| POST | /api/v1/orders/{id}/pay | 결제 |

## 학습 목표

- [ ] 분산 락으로 동시성 제어
- [ ] Saga 패턴 구현
- [ ] 주문 상태 머신
- [ ] 결제 연동 및 보상 트랜잭션
- [ ] 이벤트 기반 아키텍처

## 참고 자료

- 책: 이커머스 시스템 설계
- [Redisson 분산 락](https://redisson.org/glossary/distributed-lock.html)
