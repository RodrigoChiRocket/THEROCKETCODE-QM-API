package com.qualitas.portal.accountfraudesapi.configuracion.wrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

    public XSSRequestWrapper(HttpServletRequest request){
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {

        if (super.getParameterValues(parameter) == null) {
            return null;
        }

        List<String> values = Arrays.asList(super.getParameterValues(parameter));

        return values.stream().map(this::limpiador).toArray(String[]::new);
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);

        if(value == null ||value.isEmpty())
            return null;

        return limpiador(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);

        if(value == null || value.isEmpty())
            return null;

        return limpiador(value);
    }

    private String limpiador(String value) {

        if (value.isEmpty()) return null;

        return Stream.of(value).
                map(param -> (containsHtmlCharacters(param))? StringEscapeUtils.escapeHtml4(param) : param).
                map(StringEscapeUtils::escapeCsv).
                map(StringEscapeUtils::escapeJson).
                map(StringEscapeUtils::escapeJava).
                map(StringEscapeUtils::escapeXml10).
                map(StringEscapeUtils::escapeXSI).
                map(param -> (containsJavaScriptCharacters(param))? StringEscapeUtils.escapeEcmaScript(param) : param).
                map(StringUtils::normalizeSpace).
                findFirst().
                orElse(null);
    }

    private boolean containsJavaScriptCharacters(String value) {
        return value.matches(".*[\\x22\\x27\\x3C\\x3E\\x28\\x29\\x3D\\x26].*");
    }

    private boolean containsHtmlCharacters(String value) {
        return value.matches(".*[<>&\"].*");
    }

    private boolean containsCSS(String value) {
        //String CSS_REGEX = "(?:[^{}]+{[^}]*})*";
        String CSS_REGEX = "^[a-zA-Z_][a-zA-Z0-9_\\\\-]*$";
        Pattern pattern = Pattern.compile(CSS_REGEX, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

}
