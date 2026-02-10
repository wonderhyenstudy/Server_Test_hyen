package com.busanit501.server_test_hyen._0209_todo.controller;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "_0209_1_TodoListController", urlPatterns = "/todo/list_0209")
@Log4j2 // 로그를 중요도에 따라서, 기록을 하는 방법을 다르게한다.

public class _0209_1_TodoListController extends HttpServlet {

    private _0209_2_TodoService todoService = _0209_2_TodoService.INSTANCE;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("===전체 목록 조회=====");
        try {

            ServletContext context = getServletContext();

            String adminEmail = context.getInitParameter("adminEmail");

            context.setAttribute("globalMessage", "안녕하세요, 모두와 공유하는 메시지입니다.");

            req.setAttribute("adminEmailDirect", adminEmail);


            List<_0209_6_TodoDTO> dtoList = todoService.ListAll();
            req.setAttribute("dtoList", dtoList);
            // 결과 화면으로 안내.
            req.getRequestDispatcher("/WEB-INF/_0209_todo/list.jsp")
                    .forward(req,resp);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServletException("list error");
        }


    }
}
