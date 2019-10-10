package com.example.project.config;

import com.example.project.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

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
//        if(e.getClass() == UsernameNotFoundException.class) {
//            message = "cannot find a user";
//        } else if(e.getClass() == BadCredentialsException.class) {
//            message = "check your password";
//        }
//        httpServletRequest.getRequestDispatcher(String.format("/error?message=%s", message)).forward(httpServletRequest, httpServletResponse);

    }
}

