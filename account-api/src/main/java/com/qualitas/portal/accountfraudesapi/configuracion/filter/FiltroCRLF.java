package com.qualitas.portal.accountfraudesapi.configuracion.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class FiltroCRLF implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Logger.getLogger(FiltroCRLF.class.getName()).info("FiltroCRLF inicializado");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            try {
                filterChain.doFilter(new HttpServletRequestWrapper(httpRequest) {

                    @Override
                    public String getParameter(String valor) {
                        String val = super.getParameter(valor);
                        return limpiadorCandena(val);
                    }

                }, httpResponse);
            } catch (IOException | ServletException ignore) {
                httpResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"msg\": \"Solicitud corrupta o invalida\"}");
            }
    }

    @Override
    public void destroy() {}

    private String limpiadorCandena(String input) {
        if (Optional.ofNullable(input).isPresent()) {
            //String nuevaCadenaSCE = StringEscapeUtils.escapeJava(input);
            String cadenaSCXML = StringEscapeUtils.escapeXml10(input);
            String candenaSET = StringUtils.normalizeSpace(cadenaSCXML);
            return candenaSET.replaceAll("[\r\n]", "");
        }else
            return null;
    }

}
