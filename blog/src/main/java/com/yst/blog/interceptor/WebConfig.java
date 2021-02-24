package com.yst.blog.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//没有注解不管用
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {//拦截器，用来过滤没有登陆的请求
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**") //用LoginInterceptor的preHandle方法拦截admin下的所有页面
                .excludePathPatterns("/admin") //除了/admin自己
                .excludePathPatterns("/admin/login"); //除了/admin/login
    }
}
