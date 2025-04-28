package com.qualitas.portal.accountfraudesapi.configuracion;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MultipartFilter implements Filter {
    private static final Log logger = LogFactory.getLog(MultipartFilter.class);
    private StandardServletMultipartResolver multipartResolver;
    @Override
    public void init(FilterConfig filterConfig) {
        this.multipartResolver = new StandardServletMultipartResolver();
        logger.info("MultipartFilter initialized with StandardServletMultipartResolver");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String contentType = httpRequest.getContentType();


        // Verificación más robusta de contenido multipart
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/form-data")) {
            logger.info("Processing multipart request for URI: " + httpRequest.getRequestURI());

            try {
                if (!ServletFileUpload.isMultipartContent(httpRequest)) {
                    logger.warn("Request has multipart content-type but no boundary");
                    throw new ServletException("Invalid multipart request: no boundary found");
                }

                HttpServletRequest processedRequest = multipartResolver.resolveMultipart(httpRequest);
                chain.doFilter(processedRequest, response);

                if (processedRequest instanceof MultipartHttpServletRequest) {
                    multipartResolver.cleanupMultipart((MultipartHttpServletRequest) processedRequest);
                }
            } catch (Exception e) {
                logger.error("Failed to process multipart request", e);
                throw new ServletException("Multipart processing failed: " + e.getMessage(), e);
            }
        } else {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
        logger.info("MultipartFilter destroyed");
    }
}