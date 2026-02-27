package lab.systemdesign.urlshortener.domain.specification;

import lab.systemdesign.urlshortener.domain.vo.OriginalUrl;
import lab.systemdesign.urlshortener.exception.InvalidUrlException;

/**
 * URL 형식 검증 Specification.
 *
 * <p>원본 URL이 시스템에서 허용하는 형식인지 검증하는 비즈니스 규칙을 캡슐화한다.
 */
public class UrlFormatSpecification {

    /**
     * URL이 형식 규칙을 만족하는지 확인한다.
     *
     * @param url 검증할 원본 URL
     * @return 규칙을 만족하면 true, 그렇지 않으면 false
     */
    public boolean isSatisfiedBy(OriginalUrl url) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    /**
     * URL 형식을 검증하고, 유효하지 않으면 예외를 발생시킨다.
     *
     * @param url 검증할 원본 URL
     * @throws InvalidUrlException URL 형식이 유효하지 않은 경우
     */
    public void validate(OriginalUrl url) throws InvalidUrlException {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }
}
