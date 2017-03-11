package com.mifuns.common.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by miguangying on 2017/3/11.
 */
public class LiveFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setContentType("text/plain;charset=utf-8");
        servletResponse.getWriter().print("ok");
    }

    @Override
    public void destroy() {

    }
}
