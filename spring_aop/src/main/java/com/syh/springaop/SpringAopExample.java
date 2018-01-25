package com.syh.springaop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SpringAopExample {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringAopExample.class);
    }

}
