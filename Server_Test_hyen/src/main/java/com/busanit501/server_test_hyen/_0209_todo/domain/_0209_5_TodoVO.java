package com.busanit501.jsp_server_project1._0209_todo.domain;

import lombok.*;

import java.time.LocalDate;

@Getter // getter 직접 생성안하고, 어노테이션이용하면, 메모리상에 다 만들어져 있다.
@Builder // 객체를 생성시, A a = new A();, 계속 객체를 생성하면서, 이어서 작업합니다.
@ToString // ToString 직접 생성안하고, 어노테이션이용하면, 메모리상에 다 만들어져 있다.
// 해당 객체를 sout 으로 출력하면, 우리가 지정한 데이터 값으로 출력이 됩니다.
// 생성자를 추가.
@NoArgsConstructor // 기본 생성자를 생성함.
@AllArgsConstructor // 모든 멤버를 매개변수로 가지는 생성자를 생성함.
public class _0209_5_TodoVO {
    private Long tno;
    private String title;
    private LocalDate dueDate;
    private  boolean finished;

}
