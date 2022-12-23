package com.example.test.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.test.common.exception.CaptchaException;
import com.example.test.common.lang.Const;
import com.example.test.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//  验证码校验
@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();

        if("/login".equals(url) && request.getMethod().equals("POST")){
            try {
                //校验验证码
                validate(request);
            }catch (CaptchaException e){
                //交给失败处理器
                 loginFailureHandler.onAuthenticationFailure(request,response,e);
            }



            //如果不正确，就跳转到认证失败处理器


        }
    }
// 校验验证码逻辑
    private void validate(HttpServletRequest request) {

        String code = request.getParameter("code");// 拿到前端输入的验证码
        String key = request.getParameter("token");

        if(StringUtils.isBlank(code) || StringUtils.isBlank(key)){
            throw new CaptchaException("验证码错误");
        }
        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))){
            throw new CaptchaException("验证码错误");
        }
        //一次性使用
        redisUtil.hdel(Const.CAPTCHA_KEY,key);

    }
}
