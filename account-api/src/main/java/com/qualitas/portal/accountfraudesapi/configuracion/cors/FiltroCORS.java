package com.qualitas.portal.accountfraudesapi.configuracion.cors;

import com.qualitas.portal.accountfraudesapi.configuracion.permitido.HostsPermitidos;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FiltroCORS implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        List<String> originsAllowed = HostsPermitidos.obtenerListaHostPermitidos();
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String origin = req.getHeader("Origin");
        String originCorregido = (origin != null) ? origin.replaceAll("\\\\\\\\", "") : "";

        if (origin != null && originsAllowed.contains(origin)) {
            res.setHeader("Access-Control-Allow-Origin", origin);
        }

        if (!originCorregido.isEmpty() && originsAllowed.contains(originCorregido)) {
            res.setHeader("Access-Control-Allow-Origin", originCorregido);
        }

        res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, X-Requested-With");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}