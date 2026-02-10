package com.busanit501.jsp_server_project1._0209_todo.controller;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@Log4j2
@WebServlet(name="_0209_16_LogoutController", urlPatterns = "/logout_0209")
public class _0209_16_LogoutController extends HttpServlet {
    // 로그 아웃 처리.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("로그아웃 처리를 담당하느 doPost 입니다. ");

        // 임시 로그아웃 효과,
        HttpSession session = req.getSession();
        // 세션의 loginInfo 라는 키를 삭제하면 됩니다.
        session.removeAttribute("loginInfo");
        // 적용하기.
        session.invalidate();

        // 정상 로그아웃한다면, 쿠키의 remember-me 삭제도 같이 해야, 정상 로그아웃.
        // 2. remember-me 쿠키 삭제 로직 추가
        // 기존에 만든 findCookie 메서드를 활용하거나 직접 생성하여 전송합니다.
        Cookie rememberCookie = findCookie(req.getCookies(), "remember-me");

        // 쿠키의 수명을 0으로 설정 (삭제 효과)
        rememberCookie.setMaxAge(0);
        rememberCookie.setPath("/");

        // 응답 헤더에 추가하여 브라우저의 쿠키를 갱신(삭제)
        resp.addCookie(rememberCookie);

        // 리다이렉트
        resp.sendRedirect("/login_0209");

    }

    private Cookie findCookie(Cookie[] cookies, String cookieName) {
        Cookie targetCookie = null;

        if(cookies != null && cookies.length > 0) {
            for(Cookie ck: cookies) {
                if(ck.getName().equals(cookieName)) {
                    targetCookie = ck;
                    break;
                }
            }
        }

        // 찾고자 하는 쿠키가 없는 경우에도 삭제 처리를 위해 빈 값을 가진 쿠키 생성
        if(targetCookie == null) {
            targetCookie = new Cookie(cookieName, "");
            targetCookie.setPath("/");
        }
        return targetCookie;
    }
}
