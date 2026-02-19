# YouTube Clone

동영상 스트리밍 - 동영상 업로드, 트랜스코딩, 스트리밍 서비스

## 개요

YouTube 같은 동영상 플랫폼을 구현합니다. 비디오 업로드, 트랜스코딩, 적응형 스트리밍을 지원합니다.

## 핵심 설계 포인트

### 1. 비디오 업로드 파이프라인
```
[Upload] → [S3 Raw] → [Transcoder] → [S3 Encoded] → [CDN]
```

### 2. 트랜스코딩
- **HLS/DASH**: 적응형 비트레이트 스트리밍
- **다중 해상도**: 360p, 480p, 720p, 1080p
- **JavaCV/FFmpeg**: 비디오 처리

### 3. 데이터 모델
```
// 비디오
video_id (PK) | title | description | uploader_id | status | duration | created_at

// 비디오 파일
file_id (PK) | video_id | resolution | format | s3_key | size
```

### 4. 스트리밍 최적화
- **CDN**: 전 세계 엣지 캐싱
- **Chunked Transfer**: 분할 전송
- **Pre-sign URL**: 보안 URL 생성

## 기술 스택

| Component | Technology |
|-----------|------------|
| Web Framework | Spring Boot 3.2 |
| Storage | S3 (LocalStack/MinIO) |
| Video Processing | JavaCV (FFmpeg) |
| Message Queue | Apache Kafka |

## 아키텍처

```
[Client] → [Upload API] → [S3 Raw Bucket]
                                  ↓
                          [Kafka Topic]
                                  ↓
                          [Transcoder Worker]
                                  ↓
                          [S3 Encoded Bucket]
                                  ↓
                              [CDN]
```

## API 명세

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/videos | 비디오 업로드 (Presigned URL) |
| GET | /api/v1/videos/{id} | 비디오 메타데이터 |
| GET | /api/v1/videos/{id}/stream | 스트리밍 URL |
| GET | /api/v1/videos | 비디오 목록 |

## 학습 목표

- [ ] 대용량 파일 업로드 (Multipart)
- [ ] S3 Presigned URL
- [ ] 비디오 트랜스코딩
- [ ] HLS/DASH 스트리밍
- [ ] 비동기 처리 파이프라인

## 참고 자료

- 책: 비디오 스트리밍 시스템 설계
- [JavaCV 문서](https://github.com/bytedeco/javacv)
- [HLS 스트리밍](https://developer.apple.com/streaming/)
