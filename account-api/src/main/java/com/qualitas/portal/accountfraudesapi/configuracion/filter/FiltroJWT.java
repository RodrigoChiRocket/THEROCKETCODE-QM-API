package com.qualitas.portal.accountfraudesapi.configuracion.filter;

import com.qualitas.portal.fraudes.account.util.jwt.JwtUtil;
import com.qualitas.portal.fraudes.account.util.jwt.JWTPayload;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class FiltroJWT implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Logger.getLogger(FiltroJWT.class.getName()).info("FiltroJWT inicializado");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String token = httpRequest.getHeader("Authorization").substring(7).trim().replaceAll("\\\\", "").replace(" ","");

        try{
            JWTPayload payload = JwtUtil.getPayload(token, JwtUtil.JWT_SEMILLA);

            if (payload == null)
                throw new RuntimeException("");

            httpRequest.setAttribute("usuario", payload);

            filterChain.doFilter(httpRequest, httpResponse);
        }catch(Exception ignore){
            httpResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"msg\": \"token invalido\"}");
        }
    }

    @Override
    public void destroy() {}


}
