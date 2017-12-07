package com.alcor.ril.security;

import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.ELRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created with RIL.
 * User: duduba - 邓良玉
 * Date: 2017/12/6
 * Time: 16:16
 */
public class RequestUtil {
    private static final RequestMatcher REQUEST_MATCHER = new ELRequestMatcher("hasHeader('X-Requested-With','XMLHttpRequest')");

    public static Boolean isAjaxRequest(HttpServletRequest request) {
        return REQUEST_MATCHER.matches(request);
    }

    public static void sendJsonResponse(HttpServletResponse response, String jsonValue) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setHeader("Cache-Control", "no-cache");
        try {
            response.getWriter().write(jsonValue);
        } catch (IOException e) {
//            LOGGER.error("error writing json to response", e);
        }
    }
}
