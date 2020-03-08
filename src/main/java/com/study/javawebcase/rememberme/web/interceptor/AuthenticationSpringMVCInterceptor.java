package com.study.javawebcase.rememberme.web.interceptor;


import com.study.javawebcase.rememberme.dto.AccountAuthorizationDto;
import com.study.javawebcase.rememberme.entity.AccountEntity;
import com.study.javawebcase.rememberme.service.AccountAuthenticationService;
import com.study.javawebcase.rememberme.web.controller.support.RememberMeDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;

@Slf4j
public class AuthenticationSpringMVCInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        log.debug("判断是否可以放行");
        if(isAuthorization(req,resp)){
            log.debug("认证通过，放行");
            return true;
        }
        log.debug("无认证，无权限，要求登录");

        //且删除过期cookie
        Cookie rememberMeCookie=new Cookie(RememberMeDomain.COOKIE_NAME,null);
        rememberMeCookie.setMaxAge(-1);
        resp.addCookie(rememberMeCookie);
        resp.sendRedirect("/error/401.html");
        return false;
    }

    private boolean isAuthorization(HttpServletRequest req,HttpServletResponse resp) throws Exception {

        log.debug("判断是否是已登录认证通过");
        if(isAuthorizationByLogin(req)){
            log.debug("通过了登录认证");
            return true;
        }
        log.debug("未登录或超时，判断是否设置了记住我X天登录认证");
        if(isAuthorizationByRememberMe(req)){
            log.debug("通过了记住我X天登录认证");
            return true;
        }
        return false;
    }

    private boolean isAuthorizationByLogin(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        log.debug("判断是否存在会话session");
        if(session==null){
            log.debug("无会话session，是第一次请求，无认证");
            return false;
        }
        //是否已授权
        log.debug("存在会话session，判断登录认证标识");
        Object authorizationCtx = session.getAttribute(session.getId());
        if(authorizationCtx==null){
            log.debug("无登录认证标识，无法通过");
            return false;
        }
        log.debug("有登录认证标识，通过");
        return true;
    }

    private boolean isAuthorizationByRememberMe(HttpServletRequest req)throws Exception{
        log.debug("查找是否存在rememberme认证cookie");
        String rememberMeCookieValue=findRememberMeCookieValue(req);
        if(rememberMeCookieValue==null){
            log.debug("不存在rememberme认证cookie");
            return false;
        }
        log.debug("存在rememberme认证cookie，判断合法性");
        if(!validateRememberCookieValue(req,rememberMeCookieValue)){
            log.debug("rememberme认证cookie不合法");
            return false;
        }
        log.debug("rememberme认证cookie合法");
        return true;
    }

    private String findRememberMeCookieValue(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        if(cookies==null)return null;
        for(Cookie cookie:cookies){
            if(RememberMeDomain.COOKIE_NAME.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }

    @Autowired
    AccountAuthenticationService authenticationService;


    private boolean validateRememberCookieValue(HttpServletRequest req,String rememberMeCookieValue)throws Exception{
        final String rememberMeToken=rememberMeCookieValue;
            log.debug("通过{}查找帐号",rememberMeToken);
            final AccountEntity account = authenticationService.queryAccountByRememberMeToken(rememberMeToken);
            if(account==null){
                log.debug("未查找到帐号");
                return false;
            }
            log.debug("查找到帐号,判断token是否超时");
            if(isRememberMeExpire(account.getRememberMeExpireInSeconds())){
                log.debug("token已超时");
                return false;
            }
            log.debug("token未超时，判断是否是同一客户端发出");
            RememberMeDomain rm=new RememberMeDomain();
            rm.setId(account.getId());
            rm.setAccount(account.getAccount());
            rm.setUserAgent(req.getHeader("user-agent"));
            rm.setExpirationSeconds(account.getRememberMeExpireInSeconds());


            if(!rm.getToken().equals(rememberMeToken)){
                log.debug("和上一次不一致，上次:{},本次{}",account.getRememberMeToken(),rm.getToken());
                return false;
            }
            //save the session
            log.debug("一致，生成登录认证信息存放到session");
            final HttpSession session = req.getSession();
            session.setAttribute(session.getId(), AccountAuthorizationDto.of(account));

            return true;
    }


    private static boolean isRememberMeExpire(Long expirationSeconds){
        if(expirationSeconds==null)return true;
        if(Instant.now().getEpochSecond()<=expirationSeconds){
            return false;
        }
        return true;
    }

}
