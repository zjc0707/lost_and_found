package com.jc.lost_and_found.tasks;

import com.jc.lost_and_found.model.MessageDO;
import com.jc.lost_and_found.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zjc
 * @date 2019/10/31
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduledTasks {

    /**
     * 过期时长30天
     */
    private static final long TIME_DIFFERENCE = 60 * 60 * 24 * 30;

    @Autowired
    private MessageService messageService;
    /**
     * 定时逻辑删除过期数据库和删除存储图片
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteTask(){
        log.info("-------delete task start-------");
        List<MessageDO> list = messageService.listDeployTimeLE(System.currentTimeMillis()/1000 - TIME_DIFFERENCE);
        if(list.isEmpty()){
            log.info("is empty");
        }
        else{
            try{
                for(MessageDO messageDO : list){
                    messageService.removeById(messageDO.getId());
                    log.info("remove id:" + messageDO.getId());
                }
            }catch (Exception e){
                log.error(e.toString());
            }
        }
        log.info("-------delete task end-------");
    }
}
