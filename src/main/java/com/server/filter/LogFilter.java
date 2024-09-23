package com.server.filter;

import cn.hutool.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lklbjn
 */
@Slf4j
@Order(0)
@Configuration
@WebFilter(filterName = "LogFilter", urlPatterns = "/**")
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        ResponseWrapper wrapper = new ResponseWrapper(response);
        filterChain.doFilter(request, wrapper);
        //分界线，request拦截执行从这往前的代码，response拦截执行该行之后的代码
        StringBuilder buffer = new StringBuilder();
        if (response.getStatus() != HttpServletResponse.SC_OK) {
            if (ContentType.JSON.getValue().equals(request.getContentType())) {
                BufferedReader reader = request.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            }
            log.info(String.join("==>", getIp(request), request.getRequestURI(), request.getContentType(), buffer.toString()));
            log.error("Error message: {} {}" , response.getStatus(), request.getRequestURI());
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 获取request的所有参数
     *
     * @param request :
     * @return : java.util.Map<java.lang.String,java.lang.Object>
     * @Author: Deior
     * @date: 2018/11/12 0012 13:20
     * @Version
     */
    public static Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>(16);
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length > 0) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }

        return map;
    }


    public static String getIp(HttpServletRequest request) {
        // 使用x-forwarded-for或Proxy-Client-IP或WL-Proxy-Client-IP得到ip地址
        String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            // 若以上三种方式均为获取到ip则证明获得客户端并没有采用反向代理直接使用getRemoteAddr()获取客户端的ip地址
            ipString = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }

        return ipString;
    }

    private void writeResponse(ServletResponse response, String responseString) {
        try {
            ServletOutputStream out = response.getOutputStream();
            if (StringUtils.isNotEmpty(responseString)) {
                out.write(responseString.getBytes("utf-8"));
            }
            out.flush();
        } catch (Exception e) {
            log.error("返回错误异常:{}", e.getMessage(), e);
        }
    }

}