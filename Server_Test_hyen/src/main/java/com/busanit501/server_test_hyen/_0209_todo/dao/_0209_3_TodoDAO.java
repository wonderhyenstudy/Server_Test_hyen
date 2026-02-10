package com.busanit501.jsp_server_project1._0209_todo.dao;

import com.busanit501.jsp_server_project1._0209_todo.domain._0209_5_TodoVO;
import lombok.Cleanup;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class _0209_3_TodoDAO {
    // 여기 클래스, 데이터베이스에 연결하는 기능을 모아둔 클래스.

    // 디비 서버에 연결해서,
    // 현재 시간을 조회하는 쿼리를 전달하고,
    // 현재 시간을 받아서, 가져오기.
    public String getTime() {
        // 현재 시간을 받아둘 임시 변수.
        String now = null;
        // try ~ resource with, 자동 닫기 내포되어 있음.
        // DB 서버에 연결하는 도구(디비 서버 주소, 계정 정보, 나머지 캐시 옵션이 설정되어 있는 객체)
        try (Connection connection = _0209_7_ConnectionUtil.INSTANCE.getConnection();
             // 현재 시간을 조회하는 쿼리를 전달하고
             PreparedStatement preparedStatement = connection.prepareStatement("select now()");
             // 디비 서버에 전달하고, 결과를 받아와서, 담아두기.
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            // resultSet, 담겨진 시간을 조회
            resultSet.next();
            now = resultSet.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    } //getTime 닫기

    // try ~ catch 구문 대신에, 어노테이션을 이용해서, @Cleanup
    public String getTime2() throws Exception {
        // @Cleanup와 같은 효과 = connection.close()
        @Cleanup Connection connection = _0209_7_ConnectionUtil.INSTANCE.getConnection();
        // 현재 시간을 조회하는 쿼리를 전달하고
        // @Cleanup와 같은 효과 = preparedStatement.close()
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement("select now()");
        // 디비 서버에 전달하고, 결과를 받아와서, 담아두기.
        // @Cleanup와 같은 효과 = resultSet.close()
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        // resultSet, 담겨진 시간을 조회
        resultSet.next();
        String now = resultSet.getString(1);

        return now;
    }

    //등록하기.
    // _0203_1_TodoVO vo 여기 객체에는 , 화면에서, 입력한 내용을 담고 있음.
    // 화면상에는 제목만 입력 할 예정(수동), tno 번호(자동), 완료여부(수동), 날짜(수동)
    public void insert(_0209_5_TodoVO vo) throws Exception {
        // sql 문장 작성,
        String sql = "insert into tbl_todo (title, dueDate, finished) values (?, ? ,?)";
        // 디비 서버에 연결하는 도구 설정.
        @Cleanup Connection connection = _0209_7_ConnectionUtil.INSTANCE.getConnection();
        // sql 문장을 담아 두는 기능
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // values (?, ? ,?) 값 지정 해주기.
        preparedStatement.setString(1, vo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(vo.getDueDate()));
        preparedStatement.setBoolean(3, vo.isFinished());
        // sql 문장을 디비 서버에 전달. 쓰기, 수정, 삭제 : executeUpdate()
        // 조회 : executeQuery();
        preparedStatement.executeUpdate();
    }

    // 목록 기능 구현하기.
    public List<_0209_5_TodoVO> selectAll() throws Exception {
        // sql 문장 작성,
        String sql = "select * from tbl_todo order by tno asc";

        // 디비 서버에 연결하는 도구 설정.(반복)
        @Cleanup Connection connection = _0209_7_ConnectionUtil.INSTANCE.getConnection();

        // sql 문장을 담아 두는 기능(반복)
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // sql 문장을 디비 서버에 전달.  and 결과 테이블을 받아와서, 담아두기.
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        // 데이터베이스로 받아온 내용을, 리스트로 변환 하는 작업.
        // 임시로 담아둘 리스트를 선언
        List<_0209_5_TodoVO> list = new ArrayList<>();

        // 반복문을 이용해서, 데이터베이스 내용 -> 리스트의 요소의 객체에 각각 담기 놀이.
        // resultSet, 테이블, 준비는 0행부터 준비를하고, next() 실행하면, 데이터가 있으면 다음행으로 갑니다.
        // 예시) 데이터베이스의 현재 데이터의 테이블
        // 0행은 없죠?
        //  tno,title,dueDate,finished
//     1행   1,샘플2수정,2026-02-03,0
//     2행   3,샘플제목,2026-02-03,0
//     3행   4,샘플제목22,2026-02-03,0

        while (resultSet.next()) {
            // 객체에 담기 위해서, 임시 객체를 생성, builder 패턴이용함.
            _0209_5_TodoVO vo = _0209_5_TodoVO.builder()
                    .tno(resultSet.getLong("tno"))
                    .title(resultSet.getString("title"))
                    // 타입을 일치 시켜주기,
                    .dueDate(resultSet.getDate("dueDate").toLocalDate())
                    .finished(resultSet.getBoolean("finished"))
                    .build();

            // 임시 리스트에, 데이터베이스의 내용 -> 객체로 변경한 요소를
            // 리스트에 담기.
            list.add(vo);
        }
        return list;
    }

    // 한개만 조회, 특징, 조회할 tno 번호를 알고 있다고 가정.
    // 조회할 todo 를 클릭을 하면, 클릭한 todo tno 번호를 화면으로 부터 전달을 받음.
    public _0209_5_TodoVO selectOne(Long tno) throws Exception {
        // sql 문장 작성,
        String sql = "select * from tbl_todo where tno = ?";

        // 디비 서버에 연결하는 도구 설정.(반복)
        @Cleanup Connection connection = _0209_7_ConnectionUtil.INSTANCE.getConnection();

        // sql 문장을 담아 두는 기능(반복)
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 와일드 카드 값을 추가. tno = ?
        preparedStatement.setLong(1,tno);

        // sql 문장을 디비 서버에 전달.  and 결과 테이블을 받아와서, 담아두기.
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        // 데이터베이스로 받아온 내용을, 하나 객체로 변환 하는 작업.


        // 반복문을 이용해서, 데이터베이스 내용 -> 리스트의 요소의 객체에 각각 담기 놀이.
        // resultSet, 테이블, 준비는 0행부터 준비를하고, next() 실행하면, 데이터가 있으면 다음행으로 갑니다.
        // 예시) 데이터베이스의 현재 데이터의 테이블
        // 0행은 없죠?
//        예시 -> tno = 4
        //  tno,title,dueDate,finished
//     1행   4,샘플제목22,2026-02-03,0

        resultSet.next();
            // 객체에 담기 위해서, 임시 객체를 생성, builder 패턴이용함.
            _0209_5_TodoVO vo = _0209_5_TodoVO.builder()
                    .tno(resultSet.getLong("tno"))
                    .title(resultSet.getString("title"))
                    // 타입을 일치 시켜주기,
                    .dueDate(resultSet.getDate("dueDate").toLocalDate())
                    .finished(resultSet.getBoolean("finished"))
                    .build();


        return vo;
    }

    //수정하기
    // 글 등록하기와 구조가 비슷함.
    // 수정할 내용을 담을 데이터가 받아 왔다고 가정하고, 진행.
    // 나중에, 화면에서 수정할 내용을 받아오게 됩니다. 받아오면, 그 데이터를 여기 : _0203_1_TodoVO todoVO 담기.
    // 여기서, _0203_1_TodoVO todoVO에서 변경할 내용을 꺼내서, -> 데이터베이스에 수정합니다.
    public void updateOne(_0209_5_TodoVO todoVO) throws Exception {

        // 글쓰기 작업과 거의 동일하므로, 위의 기능을 복붙해서, 수정하기.
        // sql 구문이 다름.
        // sql 문장 작성,
        String sql = "update tbl_todo set title = ?, dueDate = ?, finished = ? where tno = ?";
        // 디비 서버에 연결하는 도구 설정.(반복)
        @Cleanup Connection connection = _0209_7_ConnectionUtil.INSTANCE.getConnection();
        // sql 문장을 담아 두는 기능 (반복)
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // values (?, ? ,?) 값 지정 해주기.
        preparedStatement.setString(1, todoVO.getTitle());
        preparedStatement.setDate(2, Date.valueOf(todoVO.getDueDate()));
        preparedStatement.setBoolean(3, todoVO.isFinished());
        preparedStatement.setLong(4, todoVO.getTno());
        // sql 문장을 디비 서버에 전달. 쓰기, 수정, 삭제 : executeUpdate()
        // 조회 : executeQuery();
        preparedStatement.executeUpdate();
    }

    //삭제하기.
    // 삭제할 tno 번호를 알고 있음.
    public void deleteOne(Long tno) throws Exception {
        // tno = 4 번 삭제하기.
        // 글쓰기, 수정하기, 거의 비슷, 수정하기를 코드 복붙해서, sql 수정해서 사용하기.
        String sql = "delete from tbl_todo where tno = ?";
        // 디비 서버에 연결하는 도구 설정.(반복)
        @Cleanup Connection connection = _0209_7_ConnectionUtil.INSTANCE.getConnection();
        // sql 문장을 담아 두는 기능 (반복)
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // values (?, ? ,?) 값 지정 해주기.
        preparedStatement.setLong(1, tno);

        // sql 문장을 디비 서버에 전달. 쓰기, 수정, 삭제 : executeUpdate()
        // 조회 : executeQuery();
        preparedStatement.executeUpdate();

    }


} //_0203_4_TodoDAO 닫기
