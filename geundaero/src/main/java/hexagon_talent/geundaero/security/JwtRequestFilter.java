package hexagon_talent.geundaero.security;

import hexagon_talent.geundaero.exception.AuthErrorException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter  extends OncePerRequestFilter {
    private  final JwtTokenProvider jwtProvider;
    private static final String BEARER_PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (!uri.equals("/users/kakao") &&
                !uri.equals("/users/newToken") &&
                !uri.equals("/users/reissue") &&
                !uri.startsWith("/swagger-ui") &&
                !uri.startsWith("/v3/api-docs") &&
                !uri.startsWith("/actuator") &&
                !uri.startsWith("/favicon") &&
                !uri.startsWith("/oauth2") &&
                !uri.startsWith("/login/oauth2")) {

            log.info("요청 URI: {}", uri);

            String jwt = resolveToken(request);

            if (StringUtils.hasText(jwt)) {
                log.info("resolveToken - jwt token = {}", jwt);
            }

            try {
                if (jwt != null && jwtProvider.validateToken(jwt)) {
                    log.info("토큰 유효함. 인증 객체 설정 진행");
                    Authentication authentication = jwtProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (AuthErrorException e) {
                SecurityContextHolder.clearContext();
                log.error(" AuthErrorException 발생: {}", e.getErrorMsg());
                throw e;
            }
        }

        filterChain.doFilter(request, response);
    }


    private String resolveToken(HttpServletRequest request){
        String token =request.getHeader("Authorization");

        if(StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)){
            return token.substring(7);
        }
        return null ;
    }
}