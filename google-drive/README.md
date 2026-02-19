# Google Drive Clone

클라우드 스토리지 - 파일 저장, 동기화, 공유 서비스

## 개요

Google Drive 같은 클라우드 스토리지 서비스를 구현합니다. 파일 업로드/다운로드, 폴더 구조, 실시간 동기화를 지원합니다.

## 핵심 설계 포인트

### 1. 파일 시스템 모델링
```
// 파일/폴더 메타데이터
file_id (PK) | parent_id | name | type (file/folder) | size | s3_key | owner_id | created_at

// 공유
share_id (PK) | file_id | shared_with | permission | created_at
```

### 2. 업로드 전략
- **Multipart Upload**: 대용량 파일 분할 업로드
- **Chunked Upload**: 재개 가능한 업로드
- **Deduplication**: 중복 파일 감지

### 3. 동기화
- **WebSocket**: 실시간 변경 알림
- **Delta Sync**: 변경분만 동기화
- **Conflict Resolution**: 동시 수정 충돌 해결

### 4. 스토리지 최적화
- **S3**: 객체 스토리지
- **Cold Storage**: 자주 안 쓰는 파일 보관
- **Compression**: 압축 저장

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| ORM | Spring Data JPA |
| Storage | S3 (LocalStack/MinIO) |
| Real-time | WebSocket |

## 아키텍처

```
[Client] ←→ [API Server] ←→ [S3 Storage]
       ↑              ↓
    [WebSocket]   [Database]
                    ↓
            [Metadata Store]
```

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/files | 파일 업로드 |
| GET | /api/v1/files/{id} | 파일 다운로드 |
| DELETE | /api/v1/files/{id} | 파일 삭제 |
| POST | /api/v1/folders | 폴더 생성 |
| GET | /api/v1/files?parentId= | 목록 조회 |
| POST | /api/v1/files/{id}/share | 파일 공유 |

## 학습 목표

- [ ] S3 객체 스토리지 활용
- [ ] Multipart 파일 업로드
- [ ] 트리 구조 파일 시스템
- [ ] WebSocket 실시간 동기화
- [ ] 파일 공유 및 권한 관리

## 참고 자료

- 책: 클라우드 스토리지 시스템 설계
- [AWS S3 SDK](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)
