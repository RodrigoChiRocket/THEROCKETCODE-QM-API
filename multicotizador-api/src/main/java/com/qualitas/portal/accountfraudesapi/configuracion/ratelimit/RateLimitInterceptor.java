package com.qualitas.portal.accountfraudesapi.configuracion.ratelimit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);

    @Autowired
    private RateLimiter rateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIP(request);
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        logger.info("Intercepting request - Method: {}, Path: {}, IP: {}", method, path, clientIp);
        
        if (!rateLimiter.tryConsume(clientIp)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String errorMessage = String.format(
                "{\"error\": \"Rate limit exceeded\", " +
                "\"message\": \"Maximum 2 requests per minute allowed. Please try again later.\", " +
                "\"path\": \"%s\", " +
                "\"method\": \"%s\", " +
                "\"ip\": \"%s\"}", 
                path, method, clientIp);
            response.getWriter().write(errorMessage);
            logger.warn("Rate limit exceeded - Method: {}, Path: {}, IP: {}", method, path, clientIp);
            return false;
        }
        
        logger.info("Request allowed - Method: {}, Path: {}, IP: {}", method, path, clientIp);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // No necesitamos implementar nada aquí
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // No necesitamos implementar nada aquí
    }

    private String getClientIP(HttpServletRequest request) {
        String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };

        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
} 