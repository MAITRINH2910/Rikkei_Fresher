package com.example.project.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        boolean hasUserRole = false;
        boolean hasAdminRole = false;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority: authorities){
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                hasAdminRole = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_USER")){
                hasUserRole = true;
                break;
            }
        }
        if (hasAdminRole){
            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/admin");
        } else if (hasUserRole){
            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/");
        } else {
            throw new IllegalStateException();
        }
    }
}