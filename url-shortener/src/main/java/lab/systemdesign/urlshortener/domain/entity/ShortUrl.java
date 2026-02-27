package lab.systemdesign.urlshortener.domain.entity;

import lab.systemdesign.urlshortener.domain.vo.OriginalUrl;
import lab.systemdesign.urlshortener.domain.vo.ShortCode;

import java.time.LocalDateTime;

/**
 * 단축 URL Aggregate Root.
 *
 * <p>원본 URL과 단축 코드를 매핑하는 핵심 엔티티로, Statistics와 1:1 관계를 가진다.
 * Soft Delete를 지원하며, 삭제 시 ShortUrlDeletedEvent가 발행된다.
 */
public class ShortUrl {

    private Long id;
    private OriginalUrl originalUrl;
    private ShortCode shortCode;
    private Long statisticsId;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    /**
     * 새로운 단축 URL을 생성한다.
     *
     * @param originalUrl 원본 URL
     * @param shortCode 단축 코드
     * @param statisticsId 연결될 통계 ID
     * @return 생성된 ShortUrl 인스턴스
     */
    public static ShortUrl create(OriginalUrl originalUrl, ShortCode shortCode, Long statisticsId) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    /**
     * 단축 URL을 soft delete 처리한다.
     *
     * <p>deletedAt 필드에 현재 시간을 설정하고,
     * ShortUrlDeletedEvent가 발행되어 Statistics도 함께 hard delete된다.
     */
    public void softDelete() {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    /**
     * 삭제된 상태인지 확인한다.
     *
     * @return 삭제된 상태이면 true, 그렇지 않으면 false
     */
    public boolean isDeleted() {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    // Getters
    public Long getId() {
        return id;
    }

    public OriginalUrl getOriginalUrl() {
        return originalUrl;
    }

    public ShortCode getShortCode() {
        return shortCode;
    }

    public Long getStatisticsId() {
        return statisticsId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    // Protected constructor for JPA
    protected ShortUrl() {
    }
}
