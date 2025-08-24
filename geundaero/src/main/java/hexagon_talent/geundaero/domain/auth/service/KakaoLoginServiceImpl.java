package hexagon_talent.geundaero.domain.auth.service;

import hexagon_talent.geundaero.common.AuthErrorStatus;
import hexagon_talent.geundaero.domain.auth.dao.UserDAO;
import hexagon_talent.geundaero.domain.auth.dto.request.KakaoProfileDTO;
import hexagon_talent.geundaero.domain.auth.dto.response.KakaoLoginResponseDTO;
import hexagon_talent.geundaero.domain.auth.entity.RefreshToken;
import hexagon_talent.geundaero.domain.auth.entity.User;
import hexagon_talent.geundaero.exception.AuthErrorException;
import hexagon_talent.geundaero.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoLoginServiceImpl implements KakaoLoginService{

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtTokenProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDAO userDAO;
    @Value("${kakao.admin-key}")
    private String adminKey;

    @Override
    public void unlink(Long kakaoId) {
        String url = "https://kapi.kakao.com/v1/user/unlink";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", String.valueOf(kakaoId));

        ResponseEntity<String> res =
                restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);

        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Kakao unlink failed: http=" + res.getStatusCode());
        }
    }

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    @Override
    public KakaoProfileDTO getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoProfileDTO> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                KakaoProfileDTO.class
        );

        return response.getBody();
    }

    @Override
    public KakaoLoginResponseDTO createTokens(User user, String type) {
        String accessToken = delegateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        return new KakaoLoginResponseDTO (type, accessToken, refreshToken);
    }

    @Override
    public String delegateAccessToken(Long id, String email) {
        return jwtProvider.generateAccessToken(id, email);
    }

    @Override
    public KakaoLoginResponseDTO reissueTokens(String refreshToken) throws AuthErrorException {

        RefreshToken tokenEntity = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new AuthErrorException(AuthErrorStatus.INVALID_TOKEN));

        User user = userDAO.findById(tokenEntity.getUserId());
        if (user == null) {
            throw new AuthErrorException(AuthErrorStatus.USER_NOT_FOUND);
        }

        refreshTokenService.delete(refreshToken);

        String newAccessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtProvider.generateRefreshToken(user.getId());

        return new KakaoLoginResponseDTO("Login", newAccessToken, newRefreshToken);
    }

}