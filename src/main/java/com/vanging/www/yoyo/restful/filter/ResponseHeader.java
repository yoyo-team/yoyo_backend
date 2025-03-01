package com.vanging.www.yoyo.restful.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class ResponseHeader implements Filter
{
    public void destroy()
    {

    }

    public void init(FilterConfig config) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.addHeader("Content-Type", "application/json;charset=utf-8");
        res.addHeader("Access-Control-Allow-Origin", "*");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
