package hexagon_talent.geundaero.security;

import hexagon_talent.geundaero.common.AuthErrorStatus;
import hexagon_talent.geundaero.exception.AuthErrorException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtProvider;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final AntPathMatcher PATH = new AntPathMatcher();

    private static final String[] WHITELIST = {
            "/api/auth/kakao",
            "/api/auth/refresh",
            "/api/auth/reissue",
            "/login/oauth2/**",
            "/oauth2/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/**",
            "/favicon.ico"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        for (String pat : WHITELIST) {
            if (PATH.match(pat, uri)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        log.info("요청 URI: {}", uri);

        String jwt = resolveToken(request);
        if (StringUtils.hasText(jwt)) {
            log.info("resolveToken - jwt token(length={}): ****{}", jwt.length(), jwt.substring(Math.max(0, jwt.length()-10)));
        }

        try {
            if (jwt != null && jwtProvider.validateToken(jwt)) {
                Authentication authentication = jwtProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("토큰 유효함. 인증 객체 설정 완료");
            }
            filterChain.doFilter(request, response);

        } catch (UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
            throw new AuthErrorException(AuthErrorStatus.USER_NOT_FOUND);

        } catch (AuthErrorException e) {
            SecurityContextHolder.clearContext();
            log.warn("AuthErrorException: {}", e.getErrorStatus().getMsg());
            throw e;
        }
    }

    private String resolveToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
