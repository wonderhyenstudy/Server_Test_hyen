package com.busanit501.server_test_hyen._0209_todo.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name="_0209_10_TodoRegController", urlPatterns = "/todo/register_0209")
@Log4j2
public class _0209_10_TodoRegController extends HttpServlet {

    private _0209_2_TodoService todoService = _0209_2_TodoService.INSTANCE;
    private final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("/todo/register, 글작성 폼 임시화면 get으로 요청 처리함. ");
        req.getRequestDispatcher("/WEB-INF/_0206_todo/todoReg.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        System.out.println("todo/register 글쓰기 로직 처리하는 곳입니다.");
        System.out.println("PRG 패턴으로 글쓰기 post 작업 후, 리다이렉트 목록 화면으로 이동하기.");

        _0209_6_TodoDTO todoDTO = _0209_6_TodoDTO.builder()
                .title(req.getParameter("title"))
                .dueDate(LocalDate.parse(req.getParameter("dueDate"),DATEFORMATTER))
                .build();

        try{
            resp.setContentType("text/html;charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            todoService.register(todoDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        resp.sendRedirect("/todo/list_0209");

    }
}
