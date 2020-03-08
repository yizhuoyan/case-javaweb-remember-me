package com.study.javawebcase.rememberme;

import com.study.javawebcase.rememberme.dao.AccountEntityDao;
import com.study.javawebcase.rememberme.entity.AccountEntity;
import com.study.javawebcase.rememberme.web.interceptor.AuthenticationSpringMVCInterceptor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CaseJavawebRememberMeApplication implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor authenticationSpringMVCInterceptor(){
        return new AuthenticationSpringMVCInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationSpringMVCInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/lib/**")
                .excludePathPatterns("/error/**")
                .excludePathPatterns("/api/session",
                                     "/login.html");
    }

    @Bean
    public CommandLineRunner run(AccountEntityDao dao){
        return new CommandLineRunner(){
            @Override
            public void run(String... args) throws Exception {
                List<AccountEntity> accounts=new ArrayList<>();
                accounts.add(new AccountEntity("10086","中国移动","10086"));
                accounts.add(new AccountEntity("10000","中国电信","10000"));
                accounts.add(new AccountEntity("10010","中国联通","10010"));
                dao.saveAll(accounts);
            }
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(CaseJavawebRememberMeApplication.class, args);
    }

}
