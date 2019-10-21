package com.example.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Check Status User
 * If Status is Un Active -> block User
 * Authentication Account By Filtering Before Forwarding To Controller
 *
 */
@Component
public class CustomFilter extends GenericFilterBean {

    @Qualifier("userDetailsServiceImpl")  /* Distinguish @Bean because there are more bean with name "UserDetailsService"*/
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof UserDetails && authentication.getCredentials() == null) {
            // get username of user logged saved by getPrincipal()
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            if (!username.isEmpty()) {
                UserDetails userDetails = null;
                try {
                    userDetails = userDetailsService.loadUserByUsername(username);
                    if (!userDetails.isEnabled()) {
                        new SecurityContextLogoutHandler().logout(req, res, authentication);
                    }
                } catch (Exception e) {
                }

                if (userDetails == null) {
                    new SecurityContextLogoutHandler().logout(req, res, authentication);
                }
            }
        }
        chain.doFilter(req, res);
    }

    private CustomFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private CustomFilter() {
    }

}