package app.albums.giphy.config;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.POST.matches(request.getMethod())) {
            return true;
        } else {
            response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
            return false;
        }
    }
}