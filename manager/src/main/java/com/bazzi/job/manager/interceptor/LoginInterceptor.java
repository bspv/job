package com.bazzi.job.manager.interceptor;

import com.bazzi.job.common.util.JsonUtil;
import com.bazzi.job.manager.util.Constant;
import com.bazzi.job.manager.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private final NamedThreadLocal<Long> timeThreadLocal = new NamedThreadLocal<>("StopWatch-StartTime");

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        timeThreadLocal.set(System.currentTimeMillis());
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("Completed--->URI:{}, Method:{},TokenHeader:{}, Parameter:{}, Result:{}, Time:{}ms",
                request.getRequestURI(),
                request.getMethod(),
                request.getHeader(Constant.TOKEN_HEADER),
                JsonUtil.toJsonString(ThreadLocalUtil.getParameter()),
                JsonUtil.toJsonString(ThreadLocalUtil.getResult()),
                System.currentTimeMillis() - timeThreadLocal.get());
        ThreadLocalUtil.clearThreadLocal();
    }

}
