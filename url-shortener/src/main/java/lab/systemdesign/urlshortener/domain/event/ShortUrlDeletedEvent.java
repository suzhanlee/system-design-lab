package lab.systemdesign.urlshortener.domain.event;

/**
 * 단축 URL 삭제 이벤트.
 *
 * <p>ShortUrl이 soft delete될 때 발행되며, 연관된 Statistics를 hard delete한다.
 *
 * @param shortUrlId 삭제된 단축 URL ID
 * @param statisticsId 삭제할 통계 ID
 */
public record ShortUrlDeletedEvent(
        Long shortUrlId,
        Long statisticsId
) {

    /**
     * 삭제 이벤트를 생성한다.
     *
     * @param shortUrlId 삭제된 단축 URL ID
     * @param statisticsId 삭제할 통계 ID
     * @return 생성된 이벤트 인스턴스
     */
    public static ShortUrlDeletedEvent of(Long shortUrlId, Long statisticsId) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }
}
