package hexagon_talent.geundaero.domain.auth.service;

import hexagon_talent.geundaero.domain.auth.dto.request.KakaoProfileDTO;
import hexagon_talent.geundaero.domain.auth.dto.response.KakaoLoginResponseDTO;
import hexagon_talent.geundaero.domain.auth.entity.User;


public interface KakaoLoginService {
    KakaoProfileDTO getUserInfo(String accessToken);
    KakaoLoginResponseDTO createTokens(User user, String type);
    String delegateAccessToken(Long id, String email);
    KakaoLoginResponseDTO reissueTokens(String refreshToken);
}
