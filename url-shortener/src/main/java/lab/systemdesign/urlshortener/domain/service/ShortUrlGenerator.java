package lab.systemdesign.urlshortener.domain.service;

import lab.systemdesign.urlshortener.domain.vo.OriginalUrl;
import lab.systemdesign.urlshortener.domain.vo.ShortCode;

/**
 * 단축 URL 코드 생성 도메인 서비스.
 *
 * <p>SHA-256 해시 + Base62 인코딩 알고리즘(ADR-001)을 사용하여
 * 6~7자의 단축 코드를 생성한다.
 */
public class ShortUrlGenerator {

    /**
     * 원본 URL로부터 단축 코드를 생성한다.
     *
     * <p>SHA-256 해시를 생성하고, 앞 8바이트를 Base62로 인코딩하여
     * 6~7자의 단축 코드를 생성한다.
     *
     * @param originalUrl 원본 URL
     * @return 생성된 단축 코드
     */
    public ShortCode generate(OriginalUrl originalUrl) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    /**
     * 충돌 발생 시 단축 코드를 재생성한다.
     *
     * <p>원본 URL에 시도 횟수(attempt)를 추가하여 새로운 해시를 생성한다.
     * 최대 3회까지 재시도한다.
     *
     * @param originalUrl 원본 URL
     * @param attempt 재시도 횟수 (1부터 시작)
     * @return 재생성된 단축 코드
     * @throws IllegalStateException 최대 재시도 횟수 초과 시
     */
    public ShortCode regenerate(OriginalUrl originalUrl, int attempt) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }
}
