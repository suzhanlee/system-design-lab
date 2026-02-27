package lab.systemdesign.urlshortener.domain.vo;

import java.util.regex.Pattern;

/**
 * 단축 URL 코드를 표현하는 값 객체.
 *
 * <p>6~7자의 Base62 문자열(a-z, A-Z, 0-9)로 구성된다.
 *
 * @param value 단축 코드 문자열
 */
public record ShortCode(String value) {

    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 7;
    private static final Pattern BASE62_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,7}$");

    /**
     * 단축 코드 값을 검증하고 인스턴스를 생성한다.
     *
     * @param value 단축 코드 문자열
     * @return 생성된 ShortCode 인스턴스
     * @throws IllegalArgumentException 코드가 null이거나 빈 문자열인 경우
     * @throws IllegalArgumentException 코드 형식이 유효하지 않은 경우 (6~7자 Base62)
     */
    public static ShortCode of(String value) {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    /**
     * 단축 코드 형식이 유효한지 검사한다.
     *
     * @return 코드 형식이 유효하면 true, 그렇지 않으면 false
     */
    public boolean isValidFormat() {
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }
}
