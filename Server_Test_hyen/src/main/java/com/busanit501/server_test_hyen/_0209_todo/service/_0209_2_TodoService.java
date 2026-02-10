package com.busanit501.server_test_hyen._0209_todo.service;

import com.busanit501.server_test_hyen._0209_todo.dao._0209_3_TodoDAO;
import com.busanit501.server_test_hyen._0209_todo.domain._0209_5_TodoVO;
import com.busanit501.server_test_hyen._0209_todo.dto._0209_6_TodoDTO;
import com.busanit501.server_test_hyen._0209_todo.util._0209_4_MapperUtil;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 0204,Log4j2 1
@Log4j2
public enum _0209_2_TodoService {
    INSTANCE; // static final 생략이 되어있다.

    // 0204, 1 기능 추가. 클래스 가져오기
    // 1) DB 서버에 작업을 시키는 클래스
    private _0209_3_TodoDAO dao;
    // 2) dto<->vo 클래스를 변환 해주는 기능 클래스
    private ModelMapper modelMapper;

    // 0204, 2 생성자 호출
    _0209_2_TodoService() {
        // 위에 전역으로 선언만 한 객체를 여기서 초기화해서, 사용할수 있게 하기.
        dao = new _0209_3_TodoDAO();
        modelMapper = _0209_4_MapperUtil.INSTANCE.get();
    }


    // 0204, 3 , 기능 변경해서 사용하기.
    // 기능구현,
    // 글쓰기
    public void register(_0209_6_TodoDTO todoDTO) throws Exception {
       // todoDTO -> todoVo 변환
        _0209_5_TodoVO todoVO = modelMapper.map(todoDTO, _0209_5_TodoVO.class);

        // 변환 확인.
//        System.out.println("_0204_4_TodoService에서 register 작업중, 변환 결과 확인 todoVO : " + todoVO);
        // 0204,Log4j2 2
        log.info("_0204_4_TodoService에서 register 작업중, 변환 결과 확인 todoVO : " + todoVO);

        // DB 에 작업 시키는 클래스를 이용해서, DB 서버에 쓰기 작업하기.
        // 기본 메서드에, 예외처리를 가능하게 변경.
        dao.insert(todoVO);
    }
    // 목록조회
    // 이전 코드
    public List<_0209_6_TodoDTO> getList() {
        // 하드코딩, 더미 데이터로, 10개만 샘플 등록,
        // 반복문으로
        List<_0209_6_TodoDTO> todoDTOS = IntStream.range(0,10).mapToObj(
                i -> {
                    // todo 하나가, todoDTO 객체. 임시 객체 생성해서, 여기에 더미 값을 담기.
                    _0209_6_TodoDTO dto = new _0209_6_TodoDTO();
                    dto.setTno((long)i);
                    dto.setTitle("Todo.." + i);
                    dto.setDueDate(LocalDate.now());
                    return dto;
                }
        ).collect(Collectors.toList());// mapToObj 닫기 태그, 반복문으로 각각의 todo 객체를 생성해서, 리스트로 만들기.
        return todoDTOS;
    } //getList 닫기

    // 변경 코드
    // 전체 목록 조회 , 실제 데이터베이스에 가져오기.
    public List<_0209_6_TodoDTO> listAll() throws Exception {
        // 하드코딩, 더미 데이터로, 10개만 샘플 등록,
        List<_0209_5_TodoVO> voList = dao.selectAll();
        log.info("voList 확인 : " + voList);

        // DAO 로 데이터베이스에 데이터를 가져오면, 타입을 _0204_2_TodoVO 타입으로 받음
        // 화면에 전달하기 위해서, DTO 타입으로 변환 ,
        // 변환작업.
        // 작업 순서
        // 1) voList 에 있는 모든 요소를 순회합니다.
        // 2) 리스트에서, 요소를 하나씩 꺼내서, vo -> dto 타입으로 변환합니다. 모든 요소를.
        // 3) 변환된 요소들을 묶어서, 리스트로 변경합니다.
        // 4) 재할당. ->  List<_0204_1_TodoDTO> dtoList
        List<_0209_6_TodoDTO> dtoList = voList.stream()
                .map(vo -> modelMapper.map(vo, _0209_6_TodoDTO.class))
                .collect(Collectors.toList());

        return dtoList;
    } //listAll 닫기

    // 0205_1 ,
    //Todo 조회
    // 화면에서, 무엇을 조회할지는 알고 있다. 예시) tno = 1
    public _0209_6_TodoDTO get(Long tno) throws Exception {
        // DB로부터 전달 받아서, 사용.
        _0209_5_TodoVO todoVO = dao.selectOne(tno);
        // vo -> dto 타입으로 , 모델 맵퍼 이용해서, 변환.
        _0209_6_TodoDTO todoDTO = modelMapper.map(todoVO, _0209_6_TodoDTO.class);
        return todoDTO;
    }

    // 수정하기
    public void modify(_0209_6_TodoDTO todoDTO) throws Exception {
        log.info("todoDTO : " + todoDTO );
        // dto -> vo 타입으로 변경.
        _0209_5_TodoVO todoVO = modelMapper.map(todoDTO , _0209_5_TodoVO.class);
        // dao 의 도움을 받아서, DB 서버에게 일 시키기
        dao.updateOne(todoVO);
    }

    // 삭제하기
    public void remove(Long tno) throws Exception {
        log.info("삭제할 tno 번호 : " + tno);
        dao.deleteOne(tno);
    }


} //_4_TodoService 닫기
