package com.busanit501.server_test_hyen._0209_todo.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@WebServlet(name="_0209_9_TodoReadController", urlPatterns = "/todo/read_0209")
public class _0209_9_TodoReadController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 현재, 작업하는 파일 어디인지 , 서버 콘솔에 출력
        log.info("/todo/read_0206, get으로 요청 처리함. ");

        try {
            Long tno = Long.parseLong(req.getParameter("tno"));

            _0209_6_TodoDTO todoDTO  = _0209_2_TodoService.INSTANCE.get(tno);

            req.setAttribute("dto", todoDTO);

            Cookie viewTodosCookie = findCookie(req.getCookies(),"viewTodos");
            String todoListStr = viewTodosCookie.getValue();
            boolean exist = false;

            if(todoListStr != null && todoListStr.indexOf(tno+"-") >= 0) {
                exist = true;
            }
            log.info("exist : " + exist);

            if(!exist) {
                todoListStr += tno+"-";
                viewTodosCookie.setValue(todoListStr);
                viewTodosCookie.setMaxAge(60 * 60 * 24);
                viewTodosCookie.setPath("/");
                // 응답 객체에 담아서, 웹브라우저에게 쿠키 전달하기.
                resp.addCookie(viewTodosCookie);
            }
            req.getRequestDispatcher("/WEB-INF/_0209_todo/_3_todoRead.jsp").forward(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ServletException("read error");
        }

    }
    private Cookie findCookie(Cookie[] cookies, String cookieName) {
        // 찾고자 하는 쿠키를 담을 임시 변수 선언,
        Cookie targetCookie = null;

        if(cookies != null && cookies.length > 0) {
            for(Cookie ck: cookies) {
                if(ck.getName().equals(cookieName)) {
                    targetCookie = ck;
                    break;
                }
            }
        }
        if(targetCookie == null) {
            targetCookie = new Cookie(cookieName,"");
            targetCookie.setPath("/");
            targetCookie.setMaxAge(60 * 60 * 24); // 24 시간 유효시간. 임의로
        }
        return targetCookie;
    }


}
