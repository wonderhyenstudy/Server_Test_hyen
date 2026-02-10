package com.busanit501.server_test_hyen._0209_todo.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@Log4j2
@WebServlet(name="_0209_13_LoginController", urlPatterns = "/login_0209")
public class _0209_13_LoginController extends HttpServlet {

    _0209_21_MemberService memberService = _0209_21_MemberService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("로그인 화면을 제공하는 컨트롤러입니다.");
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("로그인 처리를 담당하느 doPost 입니다. ");
        String mid = req.getParameter("mid");
        String mpw = req.getParameter("mpw");
        String auto = req.getParameter("auto");boolean rememberMe = auto != null && auto.equals("on");
        try {
            _0209_18_MemberDTO memberDTO = memberService.login(mid, mpw);
            if (rememberMe) {
                String uuid = UUID.randomUUID().toString();
                log.info("생성된 uuid 값 확인: " + uuid);
                memberService.updateUuid(mid, uuid);
                memberDTO.setUuid(uuid);
                Cookie rememberCookie = new Cookie("remember-me", uuid);
                rememberCookie.setMaxAge(60 * 60 * 24 * 7);
                rememberCookie.setPath("/");
                resp.addCookie(rememberCookie);
            }
            HttpSession session = req.getSession();
            session.setAttribute("loginInfo", memberDTO);
            resp.sendRedirect("/todo/list_0209");
        } catch (Exception e) {
            resp.sendRedirect("/login_0209?result=error");
        }
    }
}
