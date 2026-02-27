package lab.systemdesign.urlshortener.domain.event;

import java.time.LocalDateTime;

/**
 * URL 방문 이벤트.
 *
 * <p>리다이렉트 요청 시 발행되며, Statistics의 visitCount를 증가시킨다.
 * Event-driven 아키텍처로 방문 로그 기록과 통계 업데이트를 비동기로 처리한다.
 *
 * @param shortUrlId 단축 URL ID
 * @param statisticsId 통계 ID
 * @param visitedAt 방문 시간
 * @param ipAddress 클라이언트 IP 주소 (optional)
 * @param userAgent 브라우저/기기 정보 (optional)
 */
public record UrlVisitedEvent(
        Long shortUrlId,
        Long statisticsId,
        LocalDateTime visitedAt,
        String ipAddress,
        String userAgent
) {

    /**
     * 방문 이벤트를 생성한다.
     *
     * @param shortUrlId 단축 URL ID
     * @param statisticsId 통계 ID
     * @param ipAddress 클라이언트 IP 주소
     * @param userAgent 브라우저/기기 정보
     * @return 생성된 이벤트 인스턴스
     */
    public static UrlVisitedEvent of(Long shortUrlId, Long statisticsId, String ipAddress, String userAgent) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }
}
