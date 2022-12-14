package com.example.test.config;

import com.example.test.security.LoginFailureHandler;
import com.example.test.security.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity//Spring Security用于启用Web安全的注解
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启基于方法的安全认证机制，也就是说在web层的controller启用注解机制的安全确认
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Autowired
    LoginSuccessHandler loginSuccessHandler;
    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico"
    };
    protected void configure(HttpSecurity http) throws Exception {
       http.cors().and().csrf().disable() //关闭csrf保护
        //登陆配置
               .formLogin()
               .successHandler(loginSuccessHandler)
               .failureHandler(loginFailureHandler)
        //禁用session
               .and()
               .sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//不生成session策略

        //配置拦截规则
               .and()
               .authorizeRequests()
               .antMatchers(URL_WHITELIST).permitAll()//白名单允许
               .anyRequest().authenticated();
        //异常处理器

        //配置自定义过滤器



    }


}
