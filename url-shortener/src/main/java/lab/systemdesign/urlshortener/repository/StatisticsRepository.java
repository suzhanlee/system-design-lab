package lab.systemdesign.urlshortener.repository;

import lab.systemdesign.urlshortener.domain.entity.Statistics;

import java.util.Optional;

/**
 * Statistics 영속성 인터페이스.
 *
 * <p>접속 통계 엔티티의 저장소 인터페이스로,
 * ShortUrl과 1:1 관계를 가진다.
 */
public interface StatisticsRepository {

    /**
     * ShortUrl ID로 Statistics를 조회한다.
     *
     * @param shortUrlId ShortUrl ID
     * @return 조회된 Statistics (없으면 empty)
     */
    Optional<Statistics> findByShortUrlId(Long shortUrlId);

    /**
     * ID로 Statistics를 조회한다.
     *
     * @param id Statistics ID
     * @return 조회된 Statistics (없으면 empty)
     */
    Optional<Statistics> findById(Long id);

    /**
     * Statistics를 저장한다.
     *
     * @param statistics 저장할 Statistics
     * @return 저장된 Statistics
     */
    Statistics save(Statistics statistics);

    /**
     * Statistics를 hard delete 한다.
     *
     * <p>ShortUrlDeletedEvent 핸들러에서 호출된다.
     *
     * @param id 삭제할 Statistics ID
     */
    void deleteById(Long id);
}
