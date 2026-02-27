package lab.systemdesign.urlshortener.exception;

/**
 * 유효하지 않은 URL 형식에 대한 예외.
 *
 * <p>원본 URL이 http/https 프로토콜을 따르지 않거나,
 * URL 형식이 올바르지 않은 경우 발생한다.
 */
public class InvalidUrlException extends RuntimeException {

    /**
     * 지정된 메시지로 예외를 생성한다.
     *
     * @param message 예외 메시지
     */
    public InvalidUrlException(String message) {
        super(message);
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }

    /**
     * 지정된 메시지와 원인으로 예외를 생성한다.
     *
     * @param message 예외 메시지
     * @param cause 원인 예외
     */
    public InvalidUrlException(String message, Throwable cause) {
        super(message, cause);
        throw new UnsupportedOperationException("TODO: TDD에서 구현");
    }
}
