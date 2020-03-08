package com.study.javawebcase.rememberme.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_account")
@NoArgsConstructor
public class AccountEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
  private  Long id;
  private  String account;
  private  String name;
  private  String password;
  private  String rememberMeAgent;
  private  String rememberMeToken;
  private  long rememberMeExpireInSeconds;

    public AccountEntity(String account, String name, String password) {
        this.account = account;
        this.name = name;
        this.password = password;
    }
}
