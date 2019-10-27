package com.jc.lost_and_found;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.jc.lost_and_found.mapper")
@EnableCaching
public class LostAndFoundApplication {

    public static void main(String[] args) {
        SpringApplication.run(LostAndFoundApplication.class, args);
    }

}
