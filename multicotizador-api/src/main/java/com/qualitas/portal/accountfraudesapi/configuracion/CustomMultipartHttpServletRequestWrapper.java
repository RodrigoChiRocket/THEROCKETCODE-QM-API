package com.qualitas.portal.accountfraudesapi.configuracion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

public class CustomMultipartHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private MultipartHttpServletRequest multipartRequest;

    public CustomMultipartHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.multipartRequest = new DefaultMultipartHttpServletRequest(request);
    }

    @Override
    public String getContentType() {
        return this.multipartRequest.getContentType();
    }

    // Puedes sobreescribir otros métodos según sea necesario
}