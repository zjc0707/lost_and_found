package com.jc.lost_and_found.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hx
 * @date 2019/8/19 12:06
 */
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Data
public class MongoConnectParamConfig {
    private String host;
    private String port;
    private String authenticationDatabase;
    private String database;
    private String username;
    private String password;
    private Integer bucketCollectNum;
    private Integer maxCollectFileSize;
}
