package com.study.javawebcase.rememberme.web.advise;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map> hanlderBizExeption(Exception e){
        e.printStackTrace();
        Map<String,Object> body=new HashMap<>();
        body.put("message",e.getMessage());
        return ResponseEntity
                .status(444).body(body);
    }

}
