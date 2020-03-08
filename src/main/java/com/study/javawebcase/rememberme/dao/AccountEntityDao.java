package com.study.javawebcase.rememberme.dao;

import com.study.javawebcase.rememberme.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;

public interface AccountEntityDao extends CrudRepository<AccountEntity,Long> {

    public AccountEntity findByAccount(String account)throws Exception;
    public AccountEntity findByRememberMeToken(String token)throws Exception;
}
