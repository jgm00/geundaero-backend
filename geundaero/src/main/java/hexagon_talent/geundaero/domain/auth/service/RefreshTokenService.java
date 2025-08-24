package hexagon_talent.geundaero.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import hexagon_talent.geundaero.domain.auth.entity.RefreshToken;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
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
    public void deleteAllByUserId(Long userId) {
        String uid = String.valueOf(userId);


        final String pattern = KEY_PREFIX + "*";
        final HashSet<String> toDelete = new HashSet<>();

        redisTemplate.execute((RedisConnection conn) -> {
            ScanOptions opts = ScanOptions.scanOptions().match(pattern).count(1000).build();
            try (Cursor<byte[]> cursor = conn.scan(opts)) {
                while (cursor.hasNext()) {
                    String key = new String(cursor.next(), StandardCharsets.UTF_8);
                    String val = redisTemplate.opsForValue().get(key);
                    if (uid.equals(val)) {
                        toDelete.add(key);
                    }
                }
            } catch (Exception ignore) {}
            return null;
        });

        if (!toDelete.isEmpty()) {
            redisTemplate.delete(toDelete);
        }
    }
}