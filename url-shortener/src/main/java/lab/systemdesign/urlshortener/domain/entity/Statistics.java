package lab.systemdesign.urlshortener.domain.entity;

import java.time.LocalDateTime;

/**
 * 접속 통계 엔티티.
 *
 * <p>ShortUrl과 1:1 관계를 가지며, 방문 횟수와 마지막 방문 시간을 추적한다.
 * ShortUrl 삭제 시 ShortUrlDeletedEvent에 의해 hard delete된다.
 */
public class Statistics {

    private Long id;
    private Long shortUrlId;
    private Long visitCount;
    private LocalDateTime lastVisitedAt;

    /**
     * 새로운 통계 엔티티를 생성한다.
     *
     * @param shortUrlId 연결될 ShortUrl ID
     * @return 생성된 Statistics 인스턴스
     */
    public static Statistics create(Long shortUrlId) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    /**
     * 방문 횟수를 1 증가시키고 마지막 방문 시간을 갱신한다.
     *
     * <p>UrlVisitedEvent 핸들러에서 호출된다.
     */
    public void increment() {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    /**
     * 마지막 방문 시간을 갱신한다.
     *
     * @param time 방문 시간
     */
    public void updateLastVisitedAt(LocalDateTime time) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getShortUrlId() {
        return shortUrlId;
    }

    public Long getVisitCount() {
        return visitCount;
    }

    public LocalDateTime getLastVisitedAt() {
        return lastVisitedAt;
    }

    // Protected constructor for JPA
    protected Statistics() {
    }
}
