package lab.systemdesign.urlshortener.repository;

import lab.systemdesign.urlshortener.domain.entity.VisitLog;

import java.util.List;

/**
 * VisitLog 영속성 인터페이스.
 *
 * <p>접속 로그 엔티티의 저장소 인터페이스로,
 * ShortUrl 삭제와 무관하게 보존된다.
 */
public interface VisitLogRepository {

    /**
     * VisitLog를 저장한다.
     *
     * @param visitLog 저장할 VisitLog
     * @return 저장된 VisitLog
     */
    VisitLog save(VisitLog visitLog);

    /**
     * ShortUrl ID로 VisitLog 목록을 조회한다.
     *
     * @param shortUrlId ShortUrl ID
     * @return 조회된 VisitLog 목록
     */
    List<VisitLog> findByShortUrlId(Long shortUrlId);

    /**
     * ShortUrl ID로 VisitLog 개수를 조회한다.
     *
     * @param shortUrlId ShortUrl ID
     * @return VisitLog 개수
     */
    long countByShortUrlId(Long shortUrlId);
}
