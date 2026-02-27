package lab.systemdesign.urlshortener.domain.vo;

import java.util.regex.Pattern;

/**
 * 원본 URL을 표현하는 값 객체.
 *
 * <p>유효한 URL 형식(http/https)만 허용하며, 최대 2048자까지 지원한다.
 *
 * @param value 원본 URL 문자열
 */
public record OriginalUrl(String value) {

    private static final int MAX_LENGTH = 2048;
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^https?://[\\w\\-]+(\\.[\\w\\-]+)+[/#?]?.*$",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * 원본 URL 값을 검증하고 인스턴스를 생성한다.
     *
     * @param value 원본 URL 문자열
     * @return 생성된 OriginalUrl 인스턴스
     * @throws IllegalArgumentException URL이 null이거나 빈 문자열인 경우
     * @throws IllegalArgumentException URL 형식이 유효하지 않은 경우
     * @throws IllegalArgumentException URL 길이가 2048자를 초과하는 경우
     */
    public static OriginalUrl of(String value) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    /**
     * URL 형식이 유효한지 검사한다.
     *
     * @return URL 형식이 유효하면 true, 그렇지 않으면 false
     */
    public boolean isValidFormat() {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }
}
