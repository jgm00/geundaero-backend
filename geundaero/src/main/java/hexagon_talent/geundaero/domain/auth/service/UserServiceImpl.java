package hexagon_talent.geundaero.domain.auth.service;

import hexagon_talent.geundaero.domain.auth.dao.UserDAO;
import hexagon_talent.geundaero.domain.auth.dto.request.KakaoProfileDTO;
import hexagon_talent.geundaero.domain.auth.dto.response.UserSimpleInfoDTO;
import hexagon_talent.geundaero.domain.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserDAO userDAO;
    private final RefreshTokenService refreshTokenService;
    private final KakaoLoginService kakaoLoginService;

    @Override
    @Transactional
    public User upsertUser(KakaoProfileDTO kakaoProfile) {
        userDAO.upsertUser(kakaoProfile);
        return userDAO.findByKakaoId(kakaoProfile.getId());
    }

    @Override
    @Transactional
    public User insertUser(KakaoProfileDTO kakaoProfile) {
        userDAO.insertUser(kakaoProfile);
        return userDAO.findByKakaoId(kakaoProfile.getId());
    }

    @Override
    public boolean existingUser(Long kakaoId) {
        return userDAO.existingUser(kakaoId);
    }

    @Override
    public User findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public User findByKakaoId(Long kakaoId) {
        return userDAO.findByKakaoId(kakaoId);
    }

    @Override
    public UserSimpleInfoDTO findByIdUserSimpleDTO(Long id) {
        return new UserSimpleInfoDTO(findById(id));
    }

    @Transactional
    @Override
    public void withdrawHard(Long userId, Long kakaoId) {
        refreshTokenService.deleteAllByUserId(userId);

        try {
            if (kakaoId != null) {
                kakaoLoginService.unlink(kakaoId);
            }
        } catch (Exception ignore) {
        }
        userDAO.deleteById(userId);
    }
}
