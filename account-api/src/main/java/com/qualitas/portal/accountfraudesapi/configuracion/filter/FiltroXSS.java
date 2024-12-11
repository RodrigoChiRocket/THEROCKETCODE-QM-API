package com.qualitas.portal.accountfraudesapi.configuracion.filter;

import com.qualitas.portal.accountfraudesapi.configuracion.wrapper.XSSRequestWrapper;
import com.qualitas.portal.fraudes.account.util.exception.XSSFilterException;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class FiltroXSS implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Logger.getLogger(FiltroXSS.class.getName()).info("FiltroXSS inicializado");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        XSSRequestWrapper wrapperRequest = new XSSRequestWrapper(httpRequest);

        try{
            filterChain.doFilter(wrapperRequest, servletResponse);
        } catch (Exception e) {
            throw new XSSFilterException("Solicitud corrupta o invalida", e);
        }
    }

    @Override
    public void destroy() {}
}
