package com.study.javawebcase.rememberme.dto;

import com.study.javawebcase.rememberme.entity.AccountEntity;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AccountAuthorizationDto {
    Long id;
    String name;
    String account;
    LocalDateTime lastLoginTime;

    public static AccountAuthorizationDto of(AccountEntity e){
        AccountAuthorizationDto accountAuthorizationDto = new AccountAuthorizationDto();
        accountAuthorizationDto.setId(e.getId());
        accountAuthorizationDto.setName(e.getName());
        accountAuthorizationDto.setAccount(e.getAccount());
        return accountAuthorizationDto;
    }
}
