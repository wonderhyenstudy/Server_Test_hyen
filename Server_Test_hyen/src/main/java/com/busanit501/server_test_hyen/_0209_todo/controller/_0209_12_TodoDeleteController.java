package com.busanit501.server_test_hyen._0209_todo.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@WebServlet(name="_0209_12_TodoDelectController", urlPatterns = "/todo/delete_0209")
public class _0209_12_TodoDelectController extends HttpServlet {

    private _0209_2_TodoService todoService = _0209_2_TodoService.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 삭제를 할 때, 어떤 tno를 삭제할지 알고 있음. 그래서, tno 번호도 알고 있음.
        String tnoStr = req.getParameter("tno");
        Long tno = Long.parseLong(tnoStr);

        try {
            todoService.remove(tno);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServletException("삭제 오류");
        }
        resp.sendRedirect("/todo/list_0209");
    }


}
