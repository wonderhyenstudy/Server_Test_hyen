package com.busanit501.server_test_hyen._0209_todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// @Getter, @Setter, @ToString, @EqualsAndHashCode,
// @RequiredArgsConstructor를 모두 포함하는 복합 어노테이션입니다
@Data
@Builder // 객체 생성을 체인 기법을 이용해서, 편하게 작업하기 위해서 사용함.
@NoArgsConstructor // 기본 생성자를 생성함.
@AllArgsConstructor // 모든 멤버를 매개변수로 가지는 생성자를 생성함.
public class _0209_6_TodoDTO {
    private Long tno;
    private String title;
    private LocalDate dueDate;
    private  boolean finished;
}
