package com.qc.boot.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 功能描述: api filter
 *
 * @author lijinhua
 * @date 2022/8/25 09:33
 */
@Order(20)
@Component
public class ApiFilterRegistrationBean extends FilterRegistrationBean<Filter> {

    @PostConstruct
    /** 初始化方法*/
    public  void  init(){
        //有所区别，因为我们要过滤url，而不是对所有url生效，
        //因此在初始化方法中设置完实例之后，再调用setUrlPatterns()传入要过滤的URL列表
        setFilter(new ApiFilter());
        setUrlPatterns(List.of("/api/*"));
    }

    /** 创建ApiFilter */
    class  ApiFilter implements Filter{
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            //然后给返回加点东西
            resp.setHeader("X-Api-Version", "1.0");
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }


}
