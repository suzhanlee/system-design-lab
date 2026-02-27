package lab.systemdesign.urlshortener.domain.entity;

import java.time.LocalDateTime;

/**
 * 접속 로그 엔티티.
 *
 * <p>별도의 Aggregate로, ShortUrl 삭제와 무관하게 보존된다.
 * 각 접속에 대한 상세 정보(IP, UserAgent)를 기록한다.
 */
public class VisitLog {

    private Long id;
    private Long shortUrlId;
    private LocalDateTime visitedAt;
    private String ipAddress;
    private String userAgent;

    /**
     * 새로운 접속 로그를 생성한다.
     *
     * @param shortUrlId 단축 URL ID
     * @param ipAddress 클라이언트 IP 주소
     * @param userAgent 브라우저/기기 정보
     * @return 생성된 VisitLog 인스턴스
     */
    public static VisitLog create(Long shortUrlId, String ipAddress, String userAgent) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getShortUrlId() {
        return shortUrlId;
    }

    public LocalDateTime getVisitedAt() {
        return visitedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    // Protected constructor for JPA
    protected VisitLog() {
    }
}
