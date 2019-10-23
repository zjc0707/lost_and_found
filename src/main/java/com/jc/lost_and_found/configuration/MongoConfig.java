package com.jc.lost_and_found.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author InitUser
 */
@Configuration
@Slf4j
public class MongoConfig {

    @Autowired
    private MongoConnectParamConfig mongoConnectParamConfig;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private MongoOperations mongoOperations;

    private Map<String, GridFsOperations> gridFsOperationsMap = new HashMap<>();
    private Map<String, GridFSBucket> gridFSBucketMap = new HashMap<>();
    /**
     * 进行桶和集合的初始化生成,最大MAX_BUCKET_OR_COLLECTION_NUM
     * 其中 0 - (MAX_BUCKET_OR_COLLECTION_NUM - 1) 为集合名
     * MAX_BUCKET_OR_COLLECTION_NUM - （2*MAX_BUCKET_OR_COLLECTION_NUM - 1） 为桶名
     */
    @PostConstruct
    public void MongoInitBucketSAndCollectionS(){
        log.info("initialize MongoDb[" + getConnectParam() + "]!");
        for(int i = 0; i < mongoConnectParamConfig.getBucketCollectNum(); i++)
        {
            GridFsOperations gridFsOperations = new GridFsTemplate(mongoDbFactory, mappingMongoConverter(),Integer.toString(i + mongoConnectParamConfig.getBucketCollectNum()));
            gridFsOperationsMap.put(Integer.toString(i + mongoConnectParamConfig.getBucketCollectNum()), gridFsOperations);
            GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDbFactory.getDb(),Integer.toString(i + mongoConnectParamConfig.getBucketCollectNum()));
            gridFSBucketMap.put(Integer.toString(i + mongoConnectParamConfig.getBucketCollectNum()), gridFSBucket);
            if (!mongoOperations.collectionExists(Integer.toString(i))) {
                mongoOperations.createCollection(Integer.toString(i));
            }
        }
    }

    private String getConnectParam(){
        return mongoConnectParamConfig.toString();
    }

    public GridFsOperations getGridFsOperations(String bucket){
        return gridFsOperationsMap.get(bucket);
    }

    public GridFSBucket getGridFSBucket(String bucket){
        return gridFSBucketMap.get(bucket);
    }

    @Bean
    public MongoClient mongoClient(){
        log.info(this.toString());
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        // todo:此处可加优化参数
        //连接数
        builder.connectionsPerHost(10);
        //最小连接数
        builder.minConnectionsPerHost(2);
        //最大空闲时间
        builder.maxConnectionIdleTime(1000*300);
        MongoClientOptions mongoClientOptions = builder.build();
        // MongoDB地址列表
        ServerAddress serverAddress = new ServerAddress(mongoConnectParamConfig.getHost(), Integer.parseInt(mongoConnectParamConfig.getPort()));
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(mongoConnectParamConfig.getUsername(), mongoConnectParamConfig.getAuthenticationDatabase(), mongoConnectParamConfig.getPassword().toCharArray());

        return new MongoClient(serverAddress, mongoCredential, mongoClientOptions);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory,mappingMongoConverter());
    }

    @Bean
    public MongoDbFactory initMongoDbFactory(){
        return new SimpleMongoDbFactory(mongoClient, mongoConnectParamConfig.getDatabase());
    }

    private MappingMongoConverter mappingMongoConverter() {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setApplicationContext(applicationContext);

        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
        // 设置为 null，防止添加 _class
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mappingMongoConverter;
    }
}
