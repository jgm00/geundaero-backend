package hexagon_talent.geundaero.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import hexagon_talent.geundaero.common.ApiResponse;
import hexagon_talent.geundaero.domain.auth.dto.request.KakaoLoginRequestDTO;
import hexagon_talent.geundaero.domain.auth.dto.request.KakaoProfileDTO;
import hexagon_talent.geundaero.domain.auth.dto.response.KakaoLoginResponseDTO;
import hexagon_talent.geundaero.domain.auth.entity.CustomUserDetail;
import hexagon_talent.geundaero.domain.auth.entity.User;
import hexagon_talent.geundaero.domain.auth.service.KakaoLoginService;
import hexagon_talent.geundaero.domain.auth.service.RefreshTokenService;
import hexagon_talent.geundaero.domain.auth.service.UserService;
import hexagon_talent.geundaero.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserControllerDocs {
    private final UserService userService;
    private final KakaoLoginService kakaoLoginService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtProvider;
    private final OAuth2AuthorizedClientService authorizedClientService;


    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<String>> kakaoLogin(
            @RequestBody KakaoLoginRequestDTO requestDTO, HttpServletResponse response) {

        String kakaoAccessToken = requestDTO.getAccessToken();

        KakaoProfileDTO profile = kakaoLoginService.getUserInfo(kakaoAccessToken);

        User user = userService.upsertUser(profile);

        String loginType = user.getCreatedAt().equals(user.getUpdatedAt()) ? "signUp" : "Login";
        KakaoLoginResponseDTO tokenDTO = kakaoLoginService.createTokens(user, loginType);

        return ResponseEntity.ok(ApiResponse.success("tokenDTO", tokenDTO));
    }



    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@AuthenticationPrincipal CustomUserDetail userDetails) {
        Long userId = userDetails.getId();

        refreshTokenService.deleteByUserId(userId);

        return ResponseEntity.ok(ApiResponse.success("message", "로그아웃 되었습니다."));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<KakaoLoginResponseDTO>> reissue(@RequestParam String refreshToken,
                                                         HttpServletResponse response) {
        KakaoLoginResponseDTO newTokens = kakaoLoginService.reissueTokens(refreshToken);

        return ResponseEntity.ok(ApiResponse.success("tokenDTO", newTokens));
    }


    @GetMapping("/my")
    public ResponseEntity<?> findMyInfo(
            @AuthenticationPrincipal Object principal
    ) {
        Long userId = null;

        if (principal instanceof CustomUserDetail cud) {
            userId = cud.getId();
        }
        else if (principal instanceof OAuth2User o2) {
            Object kakaoIdObj = o2.getAttributes().get("id");
            if (kakaoIdObj != null) {
                long kakaoId = Long.parseLong(String.valueOf(kakaoIdObj));
                User u = userService.findByKakaoId(kakaoId);
                if (u != null) userId = u.getId();
            }
        }

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("사용자를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(@AuthenticationPrincipal CustomUserDetail principal) {
        if (principal == null || principal.getUser() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("사용자를 찾을 수 없습니다.");
        }
        Long userId  = principal.getUser().getId();
        Long kakaoId = principal.getUser().getKakaoId();

        userService.withdrawHard(userId, kakaoId);
        return ResponseEntity.ok(ApiResponse.success("message", "회원 탈퇴 되었습니다."));
    }
}