package com.busanit501.server_test_hyen._0209_todo.filter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@WebFilter(urlPatterns = "/*") // 톰캣서버 하위의 모든 경로.
public class _0209_15_UTF8Filter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        log.info("필터2, 업무: 서버에 접근하는 부분 UTF-8 인코딩 설정. ");
        // req 객체를 구체적인 : HttpServletRequest 로 형변환 후
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        // resp 객체를 구체적인 : HttpServletResponse 로 형변환 후
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // UTF-8 설정.
        req.setCharacterEncoding("UTF-8");

        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // 필터2 검사후, 나머지 원래 진행 절차 그대로 하기.
        filterChain.doFilter(servletRequest,servletResponse);


    }
}
