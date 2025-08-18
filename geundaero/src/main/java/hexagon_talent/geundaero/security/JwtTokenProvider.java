package hexagon_talent.geundaero.security;

import hexagon_talent.geundaero.common.AuthErrorStatus;
import hexagon_talent.geundaero.domain.auth.dao.UserDAO;
import hexagon_talent.geundaero.domain.auth.entity.CustomUserDetail;
import hexagon_talent.geundaero.domain.auth.entity.RefreshToken;
import hexagon_talent.geundaero.domain.auth.entity.User;
import hexagon_talent.geundaero.domain.auth.service.CustomUserDetailService;
import hexagon_talent.geundaero.domain.auth.service.RefreshTokenService;
import hexagon_talent.geundaero.exception.AuthErrorException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor

public class JwtTokenProvider {


    @Value("${jwt.access.token.expiration.seconds}")
    private Long accessTokenValidationTime;

    @Value("${jwt.token.secret.key}")
    private String secretKeyString;
    private Key secretKey;
    private final RefreshTokenService refreshTokenService;
    private final UserDAO userDAO;
    private final CustomUserDetailService userDetailService;

    @PostConstruct
    public void initializeSecretKey(){
        log.info("Loaded JWT Secret Key: {}", secretKeyString);
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyString));
    }


    public String generateAccessToken(Long id, String subject){
        Instant now = Instant.now();


        return Jwts.builder()
                .claim("id", id)
                .setSubject(subject)
                .setExpiration(Date.from(now.plusSeconds(accessTokenValidationTime)))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long id){
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), id);
        refreshTokenService.save(refreshToken);

        return refreshToken.getRefreshToken();
    }


    public String reAccessToken(String token) throws AuthErrorException {
        RefreshToken refreshToken = refreshTokenService.findByToken(token)
                .orElseThrow(()-> new AuthErrorException(AuthErrorStatus.INVALID_TOKEN));

        Long userId = refreshToken.getUserId();

        User user = userDAO.findById(userId);

        return generateAccessToken(user.getId(), user.getEmail());
    }


    public boolean validateToken(String token) throws AuthErrorException{
        try{
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.warn("잘못된 토큰: {}", e.getMessage());
            throw new AuthErrorException(AuthErrorStatus.INVALID_TOKEN);
        } catch(ExpiredJwtException e){
            log.warn("토큰 만료: {}", e.getMessage());
            throw new AuthErrorException(AuthErrorStatus.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e){
            log.error("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e){
            log.error("잘못된 JWT 토큰입니다.");
        }
        return false;
    }


    public Authentication getAuthentication(String accessToken) throws ExpiredJwtException {
        Claims claims = getTokenBody(accessToken);
        CustomUserDetail userDetail = userDetailService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetail, "", userDetail.getAuthorities());
    }


    public Claims getTokenBody(String jwtToken){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }



}