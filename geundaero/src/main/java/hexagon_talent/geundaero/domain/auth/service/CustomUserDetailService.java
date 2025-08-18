package hexagon_talent.geundaero.domain.auth.service;

import hexagon_talent.geundaero.domain.auth.dao.UserDAO;
import hexagon_talent.geundaero.domain.auth.entity.CustomUserDetail;
import hexagon_talent.geundaero.domain.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class CustomUserDetailService implements UserDetailsService {
    private final UserDAO userDAO;

    @Override
    public CustomUserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
        User findUser = userDAO.findByEmail(email);
        if (findUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new CustomUserDetail(findUser);
    }

}