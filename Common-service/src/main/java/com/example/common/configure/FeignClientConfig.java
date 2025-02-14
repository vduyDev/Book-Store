package com.example.common.configure;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignClientConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes != null) {
            var request = servletRequestAttributes.getRequest();
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.hasText(authorizationHeader)) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
            }
        }
    }

}
