package com.busanit501.jsp_server_project1._0209_todo.dao;

import com.busanit501.jsp_server_project1._0209_todo.domain._0209_17_MemberVO;
import lombok.Cleanup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class _0209_19_MemberDAO {
    // ID, PW 를 이용해서 해당 유저를 조회하는 기능.
    public _0209_17_MemberVO getWithPassword(String mid, String mpw) throws Exception {
        // SQL 문장 선언,
        String sql = "select mid, mpw, mname from tbl_member where mid = ? and mpw = ?";

        // DB로 부터 조회된 유저를 담아둘, 임시 객체
        _0209_17_MemberVO memberVO = null;

        //DB 서버 연결
        @Cleanup Connection connection = _0209_7_ConnectionUtil.INSTANCE.getConnection();

        // SQL 담아두는 객체,
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // mid, mpw 를 와일드 카드 ? 값 넣기.
        preparedStatement.setString(1, mid);
        preparedStatement.setString(2, mpw);

        // DB 정보를 받아와서, 임시 보관하는 객체,
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        // 받아온 DB 정보를 -> 우리가 사용할 임시 객체에 담기.
        resultSet.next();
        memberVO = _0209_17_MemberVO.builder()
                .mid(resultSet.getString(1))
                .mpw(resultSet.getString(2))
                .mname(resultSet.getString(3))
                .build();
        return memberVO;
    }

    // 자동로그인 기능 추가 0209, 순서4
    // 임시로 생성된 랜덤한 문자열을 업데이트 하는 기능.
    public void updateUuid(String mid, String uuid) throws Exception {
        // sql 문장 정의
        String sql = "update tbl_member set uuid =? where mid = ?";
        // DB 서버 연결 객체,
        @Cleanup Connection connection = _0209_7_ConnectionUtil.INSTANCE.getConnection();
        // sql 문장 담을 객체
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // sql 업데이트할 데이터 넣기 작업
        preparedStatement.setString(1, uuid);
        preparedStatement.setString(2, mid);
        // sql를 전달하기.
        preparedStatement.executeUpdate();
    }

    // uuid 를 통해서, 유저를 조회하는 기능 구현.
    public _0209_17_MemberVO selectUUID(String uuid) throws Exception {
        // sql 문장 정의
        String sql = "select mid,mpw,mname,uuid from tbl_member where uuid = ?";
        // DB 서버 연결 객체,
        @Cleanup Connection connection = _0209_7_ConnectionUtil.INSTANCE.getConnection();
        // sql 문장 담을 객체
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // sql 업데이트할 데이터 넣기 작업
        preparedStatement.setString(1, uuid);
        // sql를 전달하기.
        @Cleanup ResultSet resultSet =  preparedStatement.executeQuery();

        //DB로 부터 유저 정보를 받아오고, 디비 -> _0209_17_MemberVO 변경.
        resultSet.next();

        _0209_17_MemberVO memberVO = _0209_17_MemberVO.builder()
                .mid(resultSet.getString(1))
                .mpw(resultSet.getString(2))
                .mname(resultSet.getString(3))
                .uuid(resultSet.getString(4))
                .build();
        return memberVO;
    }
}
