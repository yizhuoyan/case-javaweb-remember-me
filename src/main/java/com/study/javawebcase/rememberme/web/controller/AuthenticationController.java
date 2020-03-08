package com.study.javawebcase.rememberme.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.javawebcase.rememberme.dto.AccountAuthorizationDto;
import com.study.javawebcase.rememberme.service.AccountAuthenticationService;
import com.study.javawebcase.rememberme.web.controller.support.RememberMeDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@RestController
public class AuthenticationController {

    @Autowired
    AccountAuthenticationService service;
    @Autowired
    ObjectMapper objectMapper;

    @Value("${system.accout.remember-me-days:7}")
    int rememberMeDays;

    @PostMapping("/api/session")
    public void login(HttpServletRequest req, HttpServletResponse resp)throws Exception{
        String account=req.getParameter("account");
        String password=req.getParameter("password");
        String rememberMe=req.getParameter("rememberMe");
        //登录验证
        AccountAuthorizationDto dto=service.login(account,password);
        //登录成功，是否需要记住X天
        if(Objects.equals(rememberMe,"1")){
            //设置rememberMe-cookie
            Cookie rememberMeCookie=this.createRememberMeCookie(req,dto);
            resp.addCookie(rememberMeCookie);
        }
        //save to session
        HttpSession session=req.getSession();
        session.setAttribute(session.getId(),dto);
        resp.setContentType("application/json;charset=utf-8");
        try(PrintWriter out = resp.getWriter()){
            out.write(objectMapper.writeValueAsString(dto));
        }
    }

    private Cookie createRememberMeCookie(HttpServletRequest req,AccountAuthorizationDto dto)throws Exception{
        //需要记住X天转换为秒
        final long rememberMeDaysInSeconds= Duration.ofDays(rememberMeDays).getSeconds();
        RememberMeDomain rm=new RememberMeDomain();
        rm.setId(dto.getId());
        rm.setAccount(dto.getAccount());
        rm.setUserAgent(req.getHeader("user-agent"));
        rm.setExpirationSeconds(rememberMeDaysInSeconds+ Instant.now().getEpochSecond());
        //更新rememberme信息到数据库
        service.updateAccountRememberMe(rm);

        Cookie cookie=new Cookie(RememberMeDomain.COOKIE_NAME,rm.getToken());
        //cookie.setDomain(req.getRemoteHost());
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)rememberMeDaysInSeconds);
        cookie.setPath("/");
        cookie.setSecure(false);
        //cookie.setVersion();

        return cookie;
    }



    @RequestMapping("/authorizationByRememberMe")
    public String authorizationByRememberMe(HttpServletRequest req,HttpServletResponse resp)throws Exception{
        String account= (String) req.getAttribute("account");
        String password= (String) req.getAttribute("password");
        //登录验证
        AccountAuthorizationDto dto=service.login(account,password);
        //save to session
        HttpSession session=req.getSession();
        session.setAttribute(session.getId(),dto);
        return null;
    }








}
