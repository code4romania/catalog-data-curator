package ro.code4.curator.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ro.code4.curator.entity.User;
import ro.code4.curator.repository.UserRepository;

import java.util.Arrays;

/**
 * Created on 4/20/17.
 * <p>
 * DaoAuthenticationProvider might be a safer choice (password salting, etc)
 */
@Component
public class SimpleUserRepositoryAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object pass = authentication.getCredentials();
        Object user = authentication.getPrincipal();
        if (user != null && pass != null) {
            User userAcc = userRepository.findByUsername(user.toString());

            if (userAcc != null && userAcc.getPassword().equals(pass))
                return new UsernamePasswordAuthenticationToken(user, pass,
                        Arrays.asList(
                                new SimpleGrantedAuthority("ROLE_USER"),
                                new SimpleGrantedAuthority("ROLE_ACTUATOR")));
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
