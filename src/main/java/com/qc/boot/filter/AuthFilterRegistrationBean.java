package com.qc.boot.filter;

import com.qc.boot.entity.User;
import com.qc.boot.service.UserService;
import com.qc.boot.web.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 功能描述: filter
 *
 * @author lijinhua
 * @date 2022/8/25 09:33
 */
@Order(10)//数字越大越靠后
@Component
//继承自FilterRegistrationBean工厂类
public class AuthFilterRegistrationBean extends FilterRegistrationBean<Filter> {
    @Autowired
    UserService userService;

    @Override
    /**重写getFilter方法，boot会自己调这个方法完成注册 */
    public Filter getFilter(){
        return  new AuthFilter();
    }


    class AuthFilter implements Filter{

        final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            //拿到request，respone和next(也就是chain)
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            try {
                authenticateByHeader(req);
            }catch (RuntimeException e){
                logger.warn("login by authorization header failed.", e);
            }
            filterChain.doFilter(servletRequest,servletResponse);
        }
        private void authenticateByHeader(HttpServletRequest req) {
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                logger.info("try authenticate by authorization header...");
                String up = new String(Base64.getDecoder().decode(authHeader.substring(6)), StandardCharsets.UTF_8);
                int pos = up.indexOf(':');
                if (pos > 0) {
                    String email = URLDecoder.decode(up.substring(0, pos), StandardCharsets.UTF_8);
                    String password = URLDecoder.decode(up.substring(pos + 1), StandardCharsets.UTF_8);
                    User user = userService.signin(email, password);
                    req.getSession().setAttribute(UserController.KEY_USER, user);
                    logger.info("user {} login by authorization header ok.", email);
                }
            }
        }
    }





}
