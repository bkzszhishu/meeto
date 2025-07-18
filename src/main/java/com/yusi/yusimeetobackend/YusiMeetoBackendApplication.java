package com.yusi.yusimeetobackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan("com.yusi.yusimeetobackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true) //开启代理
public class YusiMeetoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YusiMeetoBackendApplication.class, args);
        System.out.println("Swagger 启动地址: " + "http://localhost:8123/api/doc.html");
    }

}
