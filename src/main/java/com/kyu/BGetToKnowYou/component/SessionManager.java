package com.kyu.BGetToKnowYou.component;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public String createSession(Object value, HttpServletResponse response) {
        //세션 생성
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //쿠키 생성 후 저장
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
//        cookie.setMaxAge(60*60);
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return sessionId;
    }

    public Object getSession(HttpServletRequest request) {
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if (cookie == null) {
            return null;
        }
        return sessionStore.get(cookie.getValue());
    }

    public boolean expire(HttpServletRequest request) {
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if (cookie != null) {
            sessionStore.remove(cookie.getValue());
            return true;
        }
        return false;
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}
