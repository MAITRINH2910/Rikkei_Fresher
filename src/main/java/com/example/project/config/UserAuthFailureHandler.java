package com.example.project.config;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserAuthFailureHandler implements AuthenticationFailureHandler {

    private String redirectPathLoginError="/login?error=true";

    private String redirectPathForDisabledUser = "/401";

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ServletContext ctx = httpServletRequest.getSession().getServletContext();
        String contextPath = ctx.getContextPath();

        if(e.getClass() == DisabledException.class){
            httpServletResponse.sendRedirect(contextPath + redirectPathForDisabledUser);
        } else {
            httpServletResponse.sendRedirect(contextPath + redirectPathLoginError);
        }
    }
}

