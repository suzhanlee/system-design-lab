Feature: URL 단축 서비스
  긴 URL을 짧고 직관적인 URL로 변환하고, 접속 시 원본 URL로 리다이렉트하는 서비스

  Background:
    Given URL 단축 서비스가 실행 중이다

  # ============================================
  # Feature 1: URL 단축 (Happy Path)
  # ============================================
  Scenario: 정상적인 URL 단축 요청
    Given URL 단축 요청을 위한 데이터
      | originalUrl |
      | https://www.google.com/search?q=very+long+search+query+string+that+needs+to+be+shortened |
    When POST /api/v1/shorten 요청을 보낸다
      | field        | value                                                                  |
      | originalUrl  | https://www.google.com/search?q=very+long+search+query+string+that+needs+to+be+shortened |
    Then 상태 코드 200을 받는다
    And 응답 본문에 다음 필드가 포함된다
      | field        | condition              |
      | shortUrl     | 15자 이하              |
      | originalUrl  | 요청한 URL과 일치      |
      | createdAt    | 존재                   |

  # ============================================
  # Feature 1: URL 단축 (Edge Cases)
  # ============================================
  Scenario: 유효하지 않은 URL 형식으로 단축 요청
    Given URL 단축 요청을 위한 데이터
      | originalUrl |
      | wwww.google.com |
    When POST /api/v1/shorten 요청을 보낸다
      | field        | value            |
      | originalUrl  | wwww.google.com  |
    Then 상태 코드 400을 받는다
    And 응답 본문에 다음 에러 메시지가 포함된다
      | field  | value          |
      | error  | Invalid URL    |

  Scenario: 필수값이 누락된 URL 단축 요청
    Given URL 단축 요청을 위한 데이터
      | originalUrl |
      |             |
    When POST /api/v1/shorten 요청을 보낸다
      | field        | value |
      | originalUrl  |       |
    Then 상태 코드 400을 받는다
    And 응답 본문에 다음 에러 메시지가 포함된다
      | field  | value                  |
      | error  | Original URL is required |

  Scenario: URL 길이가 제한을 초과한 단축 요청
    Given URL 단축 요청을 위한 데이터
      | originalUrl |
      | https://www.example.com/aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.com |
    When POST /api/v1/shorten 요청을 보낸다
      | field        | value                                                                                                                                                 |
      | originalUrl  | https://www.example.com/aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.com |
    Then 상태 코드 400을 받는다
    And 응답 본문에 다음 에러 메시지가 포함된다
      | field  | value                  |
      | error  | Original URL is too long |

  # ============================================
  # Feature 2: 리다이렉트 (Happy Path)
  # ============================================
  Scenario: 정상적인 리다이렉트 요청
    Given DB에 단축 URL 매핑이 존재한다
      | shortCode | originalUrl                                                      |
      | abc123    | https://www.google.com/search?q=very+long+search+query           |
    When GET /abc123 요청을 보낸다
    Then 상태 코드 301을 받는다
    And Location 헤더가 다음과 같다
      | header   | value                                                  |
      | Location | https://www.google.com/search?q=very+long+search+query |

  # ============================================
  # Feature 2: 리다이렉트 (Edge Cases)
  # ============================================
  Scenario: 존재하지 않는 단축 URL로 리다이렉트 요청
    Given DB에 단축 URL 매핑이 존재한다
      | shortCode | originalUrl                            |
      | abc123    | https://www.example.com                |
    When GET /notexist 요청을 보낸다
    Then 상태 코드 404을 받는다
    And 응답 본문에 다음 에러 메시지가 포함된다
      | field  | value              |
      | error  | Short URL not found |

  Scenario: 동일한 원본 URL로 재요청 시 새로운 단축 URL 생성
    Given DB에 단축 URL 매핑이 존재한다
      | shortCode | originalUrl                            |
      | abc123    | https://www.example.com                |
    And URL 단축 요청을 위한 데이터
      | originalUrl                            |
      | https://www.example.com/updated-page   |
    When POST /api/v1/shorten 요청을 보낸다
      | field        | value                              |
      | originalUrl  | https://www.example.com/updated-page |
    Then 상태 코드 200을 받는다
    And 응답 본문에 다음 필드가 포함된다
      | field       | condition                    |
      | shortUrl    | 기존 abc123과 다른 새로운 값 |

  # ============================================
  # Feature 3: 통계 조회 (Happy Path)
  # ============================================
  Scenario: 정상적인 통계 조회 요청
    Given DB에 단축 URL 통계가 존재한다
      | shortCode | originalUrl                                      | visitCount | lastVisitedAt              |
      | abc123    | https://www.google.com/search?q=very+long+query  | 42         | 2026-02-27T12:00:00Z       |
    When GET /api/v1/stats/abc123 요청을 보낸다
    Then 상태 코드 200을 받는다
    And 응답 본문에 다음 필드가 포함된다
      | field         | value                                           |
      | shortUrl      | https://sho.rt/abc123                           |
      | originalUrl   | https://www.google.com/search?q=very+long+query |
      | visitCount    | 42                                              |
      | createdAt     | 존재                                            |
      | lastVisitedAt | 2026-02-27T12:00:00Z                            |

  # ============================================
  # Feature 3: 통계 조회 (Edge Cases)
  # ============================================
  Scenario: 존재하지 않는 단축 URL 통계 조회
    Given DB에 단축 URL 통계가 존재한다
      | shortCode | originalUrl               | visitCount |
      | abc123    | https://www.example.com   | 10         |
    When GET /api/v1/stats/notexist 요청을 보낸다
    Then 상태 코드 404을 받는다
    And 응답 본문에 다음 에러 메시지가 포함된다
      | field  | value              |
      | error  | Short URL not found |
