package com.zheng.hz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Administrator on 2016/11/20/020.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.zheng.infras","com.zheng.hz"})
public class ApplicationRun {

    public static void main(String[] args){
        SpringApplication.run(ApplicationRun.class,args);
    }
}
