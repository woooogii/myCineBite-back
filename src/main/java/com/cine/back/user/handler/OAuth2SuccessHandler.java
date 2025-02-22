package com.cine.back.user.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cine.back.user.dto.oauth2.CustomOAuth2User;
import com.cine.back.user.entity.RefreshEntity;
import com.cine.back.user.provider.JwtProvider;
import com.cine.back.user.repository.RefreshRepository;

import java.util.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final JwtProvider jwtProvider;
    private final RefreshRepository refreshRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String userId = oAuth2User.getUsername();
        String userNick = oAuth2User.getName();
        String userRole = auth.getAuthority();
        String refresh = jwtProvider.create("refresh", userId, userNick, userRole, 24 * 60 * 60 * 1000L);
        addRefreshEntity(userId, refresh, 86400000L);

        response.addCookie(createCookie("refresh", refresh));
        response.sendRedirect("http://localhost:3000/getAccess");
    }
    
    // Refresh 토큰 저장
    private void addRefreshEntity(String userId, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUserId(userId);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());
        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);// 1시간
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
