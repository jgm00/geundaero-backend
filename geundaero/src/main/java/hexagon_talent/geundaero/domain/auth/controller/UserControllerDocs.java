package hexagon_talent.geundaero.domain.auth.controller;


import hexagon_talent.geundaero.common.ApiResponse;
import hexagon_talent.geundaero.domain.auth.dto.request.KakaoLoginRequestDTO;
import hexagon_talent.geundaero.domain.auth.dto.response.KakaoLoginResponseDTO;
import hexagon_talent.geundaero.domain.auth.entity.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserControllerDocs {
    @Operation(
            summary = "카카오 로그인 & 회원가입",
            description =
                    "카카오 액세스 토큰을 이용해 사용자를 로그인하거나, 최초 로그인 시 회원가입을 진행합니다.\n\n" +
                            "1️⃣ **동작:**\n" +
                            "- DB에 해당 카카오 사용자가 존재하면 → 로그인 처리 후 **HttpOnly 쿠키**에 토큰 저장\n" +
                            "- 존재하지 않으면 → 회원가입 처리 후 **HttpOnly 쿠키**에 토큰 저장\n\n" +
                            "2️⃣ **요청 Body:**\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"accessToken\": \"카카오에서 발급받은 액세스 토큰\"\n" +
                            "}\n" +
                            "```\n\n" +
                            "3️⃣ **응답 예시:**\n\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"code\": \"0\",\n" +
                            "  \"message\": \"정상 처리 되었습니다.\",\n" +
                            "  \"data\": {\n" +
                            "    \"tokenDTO\": {\n" +
                            "      \"type\": \"Login\",\n" +
                            "      \"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJpZCI6Miwic3ViIjoidmxmNTIzMUBuYXZlci5jb20iLCJleHAiOjE3NTU1MjQ4NzF9...\",\n" +
                            "      \"refreshToken\": \"e3247b5f-7894-4fde-8024-1ab96ef5303e\"\n" +
                            "    }\n" +
                            "  }\n" +
                            "}\n" +
                            "```\n\n" +
                            "※ 회원가입 시에는 `type`이 \"signUp\"으로 반환됩니다."
    )
    public ResponseEntity<ApiResponse<String>> kakaoLogin(@RequestBody KakaoLoginRequestDTO requestDTO,
                                                          HttpServletResponse response);

    @Operation(
            summary = "로그아웃",
            description =
                    "로그아웃 처리: 서버에서 해당 사용자의 Refresh Token을 무효화합니다.\n\n" +
                            "1️⃣ **동작:**\n" +
                            "- 요청한 사용자의 Refresh Token을 Redis에서 삭제\n\n" +
                            "2️⃣ **요청 헤더:**\n" +
                            "- `Authorization`: Bearer {accessToken}\n\n" +
                            "3️⃣ **응답 예시:**\n\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"code\": \"0\",\n" +
                            "  \"message\": \"정상 처리 되었습니다.\",\n" +
                            "  \"data\": {\n" +
                            "    \"message\": \"로그아웃 되었습니다.\"\n" +
                            "  }\n" +
                            "}\n" +
                            "```"
    )
    ResponseEntity<ApiResponse<String>> logout(
            @AuthenticationPrincipal CustomUserDetail userDetails
    );

    @Operation(
            summary = "Access Token 재발급",
            description =
                    "Refresh Token을 이용해 새로운 Access Token과 Refresh Token을 발급받습니다.\n\n" +
                            "1️⃣ **동작:**\n" +
                            "- 전달된 Refresh Token을 검증 후, 새로운 Access Token과 Refresh Token을 발급\n\n" +
                            "2️⃣ **요청 파라미터:**\n" +
                            "- `refreshToken`: 기존 발급받은 Refresh Token\n\n" +
                            "3️⃣ **응답 예시:**\n\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"code\": \"0\",\n" +
                            "  \"message\": \"정상 처리 되었습니다.\",\n" +
                            "  \"data\": {\n" +
                            "    \"tokenDTO\": {\n" +
                            "      \"type\": \"Login\",\n" +
                            "      \"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoidmxmNTIzMUBuYXZlci5jb20iLCJleHAiOjE3NTU1MjM4NTd9.H-qYBmDICb6dCWEc6znLfuMHFsQ8fScQKr7owg-JVt0\",\n" +
                            "      \"refreshToken\": \"e61a9f01-19de-47e2-b90f-429086d5b78c\"\n" +
                            "    }\n" +
                            "  }\n" +
                            "}\n" +
                            "```"
    )
    ResponseEntity<ApiResponse<KakaoLoginResponseDTO>> reissue(
            @RequestParam String refreshToken, HttpServletResponse response
    );


    @Operation(
            summary = "내 정보 조회",
            description =
                    "현재 로그인한 사용자의 간단한 정보를 조회합니다.\n\n" +
                            "1️⃣ **동작:**\n" +
                            "- 요청한 사용자의 기본 정보(id, name, email, profileImage, kakaoId, createdAt, updatedAt)를 반환\n\n" +
                            "2️⃣ **요청 헤더:**\n" +
                            "- `Authorization`: Bearer {accessToken}\n\n" +
                            "3️⃣ **응답 예시:**\n\n" +
                            "```json\n" +
                            "{\n" +
                            "    \"id\": 1,\n" +
                            "    \"name\": \"제갈민\",\n" +
                            "    \"email\": \"vlf5231@naver.com\",\n" +
                            "    \"profileImage\": \"http://k.kakaocdn.net/dn/cSHHQ0/btsOQJnCkGs/yQftKlYHpYrbuMD1j4Sggk/img_640x640.jpg\",\n" +
                            "    \"kakaoId\": 4402836222,\n" +
                            "    \"createdAt\": \"2025-08-18T21:10:25\",\n" +
                            "    \"updatedAt\": \"2025-08-18T21:10:25\"\n" +
                            "}\n" +
                            "```"
    )
    public ResponseEntity<?> findMyInfo(
            @AuthenticationPrincipal Object principal
    );

    @Operation(
            summary = "회원 탈퇴",
            description =
                    "회원 탈퇴 처리: 서버에서 해당 사용자의 Refresh Token을 삭제하고, 카카오 Unlink 수행합니다.\n\n" +
                            "1️⃣ **동작:**\n" +
                            "- 요청한 사용자의 Refresh Token을 Redis에서 삭제 후 카카오 Unlink 수행\n\n" +
                            "2️⃣ **요청 헤더:**\n" +
                            "- `Authorization`: Bearer {accessToken}\n\n" +
                            "3️⃣ **응답 예시:**\n\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"code\": \"0\",\n" +
                            "  \"message\": \"정상 처리 되었습니다.\",\n" +
                            "  \"data\": {\n" +
                            "    \"message\": \"회원 탈퇴 되었습니다.\"\n" +
                            "  }\n" +
                            "}\n" +
                            "```"
    )
    ResponseEntity<?> deleteMe(@AuthenticationPrincipal CustomUserDetail principal);

}