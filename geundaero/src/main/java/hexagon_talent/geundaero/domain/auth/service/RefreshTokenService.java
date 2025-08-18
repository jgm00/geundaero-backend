package hexagon_talent.geundaero.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import hexagon_talent.geundaero.domain.auth.entity.RefreshToken;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "jwtToken:";

    @Value("${jwt.refresh.token.expiration.seconds}")
    private long refreshTokenExpirationSeconds;


    public void save(RefreshToken token) {
        String key = KEY_PREFIX + token.getRefreshToken();
        redisTemplate.opsForValue().set(key, token.getUserId().toString(), Duration.ofSeconds(refreshTokenExpirationSeconds));
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        String key = KEY_PREFIX + refreshToken;
        String userIdStr = redisTemplate.opsForValue().get(key);
        if (userIdStr != null) {
            return Optional.of(new RefreshToken(refreshToken, Long.valueOf(userIdStr)));
        } else {
            return Optional.empty();
        }
    }

    public void delete(String refreshToken) {
        String key = KEY_PREFIX + refreshToken;
        redisTemplate.delete(key);
    }

    public void deleteByUserId(Long userId) {
        String pattern = KEY_PREFIX + "*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null) {
            for (String key : keys) {
                String value = redisTemplate.opsForValue().get(key);
                if (userId.toString().equals(value)) {
                    redisTemplate.delete(key);
                    break;
                }
            }
        }
    }

}