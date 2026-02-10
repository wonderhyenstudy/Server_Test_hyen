package com.busanit501.server_test_hyen._0209_todo.filter;

import com.busanit501.server_test_hyen._0209_todo.dto._0209_18_MemberDTO;
import com.busanit501.server_test_hyen._0209_todo.service._0209_21_MemberService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Log4j2
// 톰캣서버의 todo/하위경로 만 적용대상.
@WebFilter(urlPatterns = {"/todo/*"})
public class _0209_14_LoginCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        // 여기서, 실제 동작을 정의 및 체크.
        log.info("자동 로그인 확인 _0209 순서1 체크 필터 1, 테스트");

        // 기존 글쓰기 폼화면에서 작업 하던 코드를 그대로 복붙 했다.
        // servletRequest : 범위가 좀더 커서, 기능이 추상적이라서,
        // 좀더 구체적인 타입으로 변경해서 작업, HttpServletRequest 으로 타입을 변경.
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        //0206 , 글쓰기 화면에 접근하기 전에, 세션에서 로그인 유저를 체크를해서,
        // JSESSIONID 를 체크. 있으면 진행. 없으면 로그인 페이지로 이동시키자.

        // 세션를 이용하기 위해서, 도구가 필요함. 객체를 이용하기.
        HttpSession session = req.getSession();

        // 기존에 접근 이용자인지? 새로운 이용자 인지? 여부 확인을 JSESSIONID
        if(session.isNew()){
            // 새로운 이용자이니, 서버 : JSESSIONID 생성해서, 서버 메모리에 가지고 있고,
            // 그리고, 웹 브라우저에게, 쿠키에 담아서, 보내기.
            log.info("JSESSIONID 쿠키가 새롭게 만들어진 사용자");
            // 새로 오셨네요? , 로그인 페이지로 가서, 로그인 해주세요. -> 아직 없음, 곧 만들 예정.
            resp.sendRedirect("/login_0209");
        }

        // 임시 로그인 처리라서,
        // JSESSIONID 있어요, 해당 서버에 완전 최초 접속자는 아니예요.
        // 세션이라는 공간에 loginInfo 라는 이름으로 저장을 할예정, 로그인한 유저를 .
        // 잠깐 대기. 왜? 로그아웃 일 경우, loginInfo 삭제가 된상태.
        if(session.getAttribute("loginInfo") != null) {
            log.info("로그인 정보가 없는 사용자입니다.");
//            resp.sendRedirect("/login_0209");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }


        // JSESSIONID 있고, loginInfo(명단), 로그인한 유저 정보가 있어요, -> 로그인 되었다고 가정.

        // 로그인 시, 조건)자동로그인 체크 유무 , 유-> 1) 멤버 테이블 uuid 생성 2) 메모리의 로그인한 유저도 같은 uuid 변경
        //3) 같은 uuid , 쿠키에 담아서, 웹브라우저에게 전달.
        // 자동 로그인 체크가 된상태에서, 다시 목록 접근시, 필터가 동작을 해서,
        // 쿠키의 remember-me  값이 있다면, 정상 로그인 처리 해주고, 없으면, 다시 로그인 진행.

        Cookie cookie = findCookie(req.getCookies(), "remember-me");
        log.info("자동 로그인 확인 _0209 순서2 체크 필터, 쿠키 확인cookie : " + cookie);

        // 쿠키가 없으면 : 자동 로그인 체크를 안했다는 말. 그냥 로그인 페이지로 이동시킴.
        if(cookie == null) {
            resp.sendRedirect("/login_0209");
            return;
        }

        // 임시 자동로그인 처리.
        // 쿠키가 있다면: 데이터베이스에 조회해서, 해당 유저의 uuid , 쿠키의 uuid 를 비교해서, 일치한다면, 로그인 처리 해줌.
        String uuid = cookie.getValue();
        log.info("자동 로그인 확인 _0209 순서3 체크 필터, uuid 확인 uuid : " + uuid);
        try {
            // 데이터베이스에, uuid 로 유저를 검색기능 확인. 이 기능을 이용하자.
            _0209_18_MemberDTO memberDTO = _0209_21_MemberService.INSTANCE.getByUUID(uuid);
            log.info("필터 검사에서, 자동로그인 체크후, uuid 로 유저 검색 결과 memberDTO  확인 : " + memberDTO);
            if (memberDTO == null) {
                throw new Exception("쿠키 값 uuid 값이 유효하지 않아요!!");
            }
            // uuid 일치한다면,
            // 세션에 회원의 정보를 추가.
            session.setAttribute("loginInfo", memberDTO);

            // 통과후, 원래 과정으로 진행하기. 컨트롤러에 가서, 라우팅 및 원래 하던 일 처리하기.
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/login_0209");
        }


    }

    // 쿠키를 찾는 메서드를 이용하기.
    private Cookie findCookie(Cookie[] cookies, String cookieName) {
        // 찾고자 하는 쿠키를 담을 임시 변수 선언,
        Cookie targetCookie = null;

        // 전체 목록의 기본 유효성 체크, null 아니고, 길이가 0 초과 : 쿠키가 있는 경우.
        if(cookies != null && cookies.length > 0) {
            for(Cookie ck: cookies) {
                if(ck.getName().equals(cookieName)) {
                    targetCookie = ck;
                    break;
                } //if
            } //for
        } // if

        return targetCookie;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
