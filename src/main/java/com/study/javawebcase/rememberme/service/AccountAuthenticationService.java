package com.study.javawebcase.rememberme.service;

import com.study.javawebcase.rememberme.dto.AccountAuthorizationDto;
import com.study.javawebcase.rememberme.entity.AccountEntity;
import com.study.javawebcase.rememberme.web.controller.support.RememberMeDomain;
import lombok.NonNull;

public interface AccountAuthenticationService {

    /**
     * 帐号登录
     * @param account 帐号
     * @param password 密码
     * @return 帐号授权
     * @throws Exception 登录发送任何异常
     */
    public AccountAuthorizationDto login(String account,String password)throws Exception;

    /**
     * 通过
     * @param rememberMeToken
     * @return
     * @throws Exception
     */
    public AccountEntity queryAccountByRememberMeToken(@NonNull String rememberMeToken) throws Exception;

    public void updateAccountRememberMe(RememberMeDomain domain)throws Exception;
}
