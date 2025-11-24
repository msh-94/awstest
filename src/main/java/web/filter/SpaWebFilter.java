package web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component; // Component 스캔으로 필터 등록

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component // Spring Bean으로 등록하여 필터 체인에 자동으로 추가되도록 함
public class SpaWebFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // 필터링에서 제외할 경로 시작 패턴 목록 (API 경로, 정적 리소스 등)
    // 실제 프로젝트의 API 경로 접두사, 정적 리소스 경로에 맞게 수정해야 합니다.
    private final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/api/",       // 모든 API 호출
            "/static/",    // 정적 리소스 (Create React App 기본)
            "/assets/"    // 정적 리소스 (Vite 기본)
            // 다른 백엔드 전용 경로 추가 가능
    );

    // 필터링에서 제외할 특정 파일 확장자 또는 파일명
    // точка(.)를 포함하지만 SPA 라우팅으로 처리해야 하는 경우는 여기에 추가하지 않음
    private final List<String> EXCLUDE_FILES = Arrays.asList(
            "/favicon.ico",
            "/robots.txt"
            // 다른 특정 정적 파일 추가 가능
    );


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        // 제외 경로 패턴 또는 제외 파일/확장자에 해당하지 않으면 index.html로 포워딩
        if (shouldForwardToSpa(path)) {
            logger.debug("SPA WebFilter: Forwarding request to /index.html for path: {}", path);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
            dispatcher.forward(request, response);
            return; // 포워딩 후 필터 체인 중단
        }

        // API 호출이나 정적 리소스 요청 등은 그대로 필터 체인 계속 진행
        chain.doFilter(request, response);
    }

    private boolean shouldForwardToSpa(String path) {
        // 1. 명시적인 제외 파일/경로인지 확인
        if (EXCLUDE_FILES.stream().anyMatch(p -> path.equals(p))) {
            return false;
        }
        // 2. 제외 경로 패턴으로 시작하는지 확인 (API, static 등)
        if (EXCLUDE_PATHS.stream().anyMatch(p -> path.startsWith(p))) {
            return false;
        }
        // 3. 경로에 .(점)이 포함되어 파일 확장자를 가질 가능성이 높은 경우 제외 (단순 휴리스틱)
        if (path.contains(".") && path.lastIndexOf('.') > path.lastIndexOf('/')) {
            // 예외: 점을 포함하지만 SPA 라우트인 경우 (예: /profile/user.name) 여기에 로직 추가 가능
            return false; // 일단 확장자 있는 경로는 제외
        }

        // 위 조건에 모두 해당하지 않으면 SPA 라우팅 대상일 가능성이 높음
        return true;
    }

    // init, destroy 메서드는 기본 구현 사용 가능
}