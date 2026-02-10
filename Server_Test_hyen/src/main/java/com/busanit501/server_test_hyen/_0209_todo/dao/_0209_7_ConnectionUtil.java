package com.busanit501.jsp_server_project1._0209_todo.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;

@Log4j2
public enum _0209_7_ConnectionUtil {

    INSTANCE;

    private HikariDataSource ds; // DB 연결하는 도구.

    // 생성자
    _0209_7_ConnectionUtil() {
        // HikariConfig 클래스 이용해서, 옵션 설정.
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl("jdbc:mariadb://localhost:3306/webdb");
        config.setUsername("webuser");
        config.setPassword("webuser");

        // PreparedStatement 캐시 설정
        // 데이터 베이스에서 사용하는 sql 문장을 반복적으로 사용하는 것을 캐시 (저장해서) 사용하겠다.
        // - **개념**: PreparedStatement 캐싱 기능을 활성화하는 기본 옵션입니다.
        config.addDataSourceProperty("cachePrepStmts", "true");
        // 각 연결(Connection)당 캐시할 수 있는 PreparedStatement의 최대 개수입니다.
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        //- **개념**: 캐시할 수 있는 SQL 문의 최대 길이(문자 수)입니다.
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        //HikariDataSource 클래스에, 위의 설정 클래스를 담아주기
        ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws  Exception {
        return ds.getConnection();
    }

    // 닫는 메서드 추가.
    public void closePool() {
        if (ds != null && !ds.isClosed()) {
            ds.close(); // 풀 전체 종료
            log.info("HikariCP 커넥션 풀을 종료했습니다.");
        }
    }

}
