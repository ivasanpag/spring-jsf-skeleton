package com.ijsp.provider;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author ijsp
 * @since
 */
@Component
@Log4j2
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        String ip = ((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress();
        if (authenticateToThirdParty(username, password, ip)) {
            log.info("[" + ip + "] Successful login for user " + username);
            return new UsernamePasswordAuthenticationToken(username, password,
                    new ArrayList<GrantedAuthority>(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
        }
        throw new BadCredentialsException("Bad credentials");
    }

    private boolean authenticateToThirdParty(String username, String password, String ip) {
        //TODO
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
