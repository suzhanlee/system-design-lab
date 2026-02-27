package lab.systemdesign.urlshortener.repository;

import lab.systemdesign.urlshortener.domain.entity.ShortUrl;
import lab.systemdesign.urlshortener.domain.vo.OriginalUrl;
import lab.systemdesign.urlshortener.domain.vo.ShortCode;

import java.util.Optional;

/**
 * ShortUrl 영속성 인터페이스.
 *
 * <p>Aggregate Root인 ShortUrl의 저장소 인터페이스로,
 * Redis Look-Aside 캐싱 전략(ADR-002)을 적용한다.
 */
public interface ShortUrlRepository {

    /**
     * 단축 코드로 ShortUrl을 조회한다.
     *
     * <p>Redis 캐시를 먼저 확인하고, miss 시 DB에서 조회하여 캐시에 저장한다.
     *
     * @param code 단축 코드
     * @return 조회된 ShortUrl (없으면 empty)
     */
    Optional<ShortUrl> findByShortCode(ShortCode code);

    /**
     * 원본 URL로 ShortUrl을 조회한다.
     *
     * <p>중복 URL 생성 방지를 위해 사용한다.
     *
     * @param url 원본 URL
     * @return 조회된 ShortUrl (없으면 empty)
     */
    Optional<ShortUrl> findByOriginalUrl(OriginalUrl url);

    /**
     * 단축 코드의 존재 여부를 확인한다.
     *
     * <p>코드 충돌 검사에 사용한다.
     *
     * @param code 단축 코드
     * @return 존재하면 true, 없으면 false
     */
    boolean existsByShortCode(ShortCode code);

    /**
     * ShortUrl을 저장한다.
     *
     * <p>저장 후 Redis 캐시에도 저장한다 (@CachePut).
     *
     * @param shortUrl 저장할 ShortUrl
     * @return 저장된 ShortUrl
     */
    ShortUrl save(ShortUrl shortUrl);

    /**
     * ShortUrl을 ID로 조회한다.
     *
     * @param id ShortUrl ID
     * @return 조회된 ShortUrl (없으면 empty)
     */
    Optional<ShortUrl> findById(Long id);
}
