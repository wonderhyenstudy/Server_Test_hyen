package com.busanit501.server_test_hyen._0209_todo.listener;

import package com.busanit501.server_test_hyen._0209_todo.dao._0209_7_ConnectionUtil;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Log4j2
@WebListener // 톰캣에게 "나를 감시자로 등록해줘"라고 알림
public class _0209_8_TodoContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("웹 애플리케이션 시작! (여기서 DB 미리 연결 등을 할 수도 있음)");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("웹 애플리케이션 종료 감지! 자원을 해제합니다.");
        // ★ 여기서 ConnectionUtil의 풀 종료 기능을 호출
        _0209_7_ConnectionUtil.INSTANCE.closePool();
    }
}
