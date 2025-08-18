package hexagon_talent.geundaero.domain.auth.service;


import hexagon_talent.geundaero.domain.auth.dto.request.KakaoProfileDTO;
import hexagon_talent.geundaero.domain.auth.dto.response.UserSimpleInfoDTO;
import hexagon_talent.geundaero.domain.auth.entity.User;

public interface UserService {

    User insertUser(KakaoProfileDTO user);
    User upsertUser(KakaoProfileDTO kakaoProfile);
    boolean existingUser(Long kakaoId);

    User findById(Long id);
    User findByEmail(String email);
    User findByKakaoId(Long kakaoId);
    UserSimpleInfoDTO findByIdUserSimpleDTO(Long id);

}