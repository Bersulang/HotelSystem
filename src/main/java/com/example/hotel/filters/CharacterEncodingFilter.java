package com.example.hotel.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 字符编码过滤器，确保所有请求和响应使用UTF-8编码
 * 防止中文显示为乱码
 */
@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化过滤器
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 设置请求字符编码
        req.setCharacterEncoding("UTF-8");
        
        // 设置响应字符编码
        resp.setCharacterEncoding("UTF-8");
        
        // 如果是HTML响应，设置内容类型
        if (!resp.isCommitted() && resp.getContentType() == null && 
            !req.getRequestURI().endsWith(".css") && 
            !req.getRequestURI().endsWith(".js") && 
            !req.getRequestURI().endsWith(".jpg") && 
            !req.getRequestURI().endsWith(".png") && 
            !req.getRequestURI().endsWith(".gif")) {
            resp.setContentType("text/html; charset=UTF-8");
        }
        
        // 继续过滤链
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 销毁过滤器
    }
}
