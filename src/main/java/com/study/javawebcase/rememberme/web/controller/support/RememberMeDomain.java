package com.study.javawebcase.rememberme.web.controller.support;

import com.study.javawebcase.util.Md5;
import lombok.Data;

@Data
public class RememberMeDomain {
    public static final String COOKIE_NAME = "REMEMBER-ME";
    private long id;
    private String account;
    private String userAgent;
    private long expirationSeconds;
    private String token;





    public String getToken() {
        if (token != null) {
            return token;
        }
        System.out.println(this);
        StringBuilder sb = new StringBuilder();
        String md5 = Md5.encode(sb.append(this.getAccount())
                .append(":").append(this.getExpirationSeconds())
                .append(":").append(this.getId())
                .append(":").append(this.getUserAgent())
                .toString());
        return token=md5;
    }

    @Override
    public String toString() {
        return "RememberMeDomain{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", expirationSeconds=" + expirationSeconds +
                ", token='" + token + '\'' +
                '}';
    }
}
