package hexagon_talent.geundaero.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {
    EXTERNAL_API_CALL_FAIL("E9001", "외부 API 호출에 실패했습니다."),
    EXTERNAL_API_NO_RESPONSE("E9002", "외부 API 응답이 없습니다."),
    EXTERNAL_API_BAD_RESPONSE("E9003", "외부 API 응답 파싱에 실패했습니다."),
    EXTERNAL_API_ERROR("E9004", "외부 API가 오류를 반환했습니다.");

    private final String code;
    private final String msg;
}