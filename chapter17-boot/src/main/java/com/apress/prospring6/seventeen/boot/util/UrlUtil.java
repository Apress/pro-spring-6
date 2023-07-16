package com.apress.prospring6.seventeen.boot.util;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

public class UrlUtil {
    public static String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();

        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }

        pathSegment = UriUtils.encodePathSegment(pathSegment, enc);

        return pathSegment;
    }
}
