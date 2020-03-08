package com.study.javawebcase.rememberme.service.serviceimpl;

import com.study.javawebcase.rememberme.dao.AccountEntityDao;
import com.study.javawebcase.rememberme.dto.AccountAuthorizationDto;
import com.study.javawebcase.rememberme.entity.AccountEntity;
import com.study.javawebcase.rememberme.service.AccountAuthenticationService;
import com.study.javawebcase.rememberme.web.controller.support.RememberMeDomain;
import com.study.javawebcase.util.Md5;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AccountAuthenticationServiceImpl implements AccountAuthenticationService {
    private final static Pattern COOKIE_VALUE_PATTERN=Pattern.compile("^[a-z0-9]{32}$");
    @Autowired
    AccountEntityDao accountEntityDao;
    @Override
    public AccountAuthorizationDto login(@NonNull String account, @NonNull String password) throws Exception {

        AccountEntity e=accountEntityDao.findByAccount(account);
        if(e==null){
            throw  new RuntimeException("帐号和密码不匹配");
        }
        //模拟登录判断，帐号和密码一致即认为登录成功
        if(!password.equals(e.getPassword())){
            throw  new RuntimeException("帐号和密码不匹配");
        }

        AccountAuthorizationDto dto=new AccountAuthorizationDto();
        dto.setId(e.getId());
        dto.setAccount(account);
        dto.setLastLoginTime(null);
        dto.setName(e.getName());
        return dto;
    }




    public AccountEntity queryAccountByRememberMeToken(@NonNull String rememberMeToken) throws Exception {
        //base64 decode
        Matcher matcher = COOKIE_VALUE_PATTERN.matcher(rememberMeToken);
        if (!matcher.matches()) {
            return null;
        }
        AccountEntity account = accountEntityDao.findByRememberMeToken(rememberMeToken);
        return account;
    }

    @Override
    public void updateAccountRememberMe(RememberMeDomain domain) throws Exception {
        final AccountEntity entity = accountEntityDao.findById(domain.getId()).get();

        entity.setRememberMeAgent(domain.getUserAgent());
        entity.setRememberMeExpireInSeconds(domain.getExpirationSeconds());
        entity.setRememberMeToken(domain.getToken());


        accountEntityDao.save(entity);
    }
}
