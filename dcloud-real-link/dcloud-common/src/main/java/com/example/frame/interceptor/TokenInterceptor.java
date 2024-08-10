package com.example.frame.interceptor;

import com.example.frame.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Get token.
        String token = request.getHeader("token");

        // Check if token exists.
        if (!StringUtils.hasText(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // Parse token.
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
//            String userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}