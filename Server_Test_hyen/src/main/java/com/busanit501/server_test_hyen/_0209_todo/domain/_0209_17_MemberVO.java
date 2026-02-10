package com.busanit501.jsp_server_project1._0209_todo.domain;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class _0209_17_MemberVO {
    // 데이터베이스의 컬럼과 동일하게 작업.
    private String mid;
    private String mpw;
    private String mname;
    // 자동로그인 기능 추가 0209, 순서2
    private String uuid;
}
