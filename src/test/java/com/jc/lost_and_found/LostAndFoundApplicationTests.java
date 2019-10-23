package com.jc.lost_and_found;

import com.jc.lost_and_found.mapper.MessageMapper;
import com.jc.lost_and_found.model.MessageDetailVO;
import com.jc.lost_and_found.model.RoleInfoDO;
import com.jc.lost_and_found.service.RoleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LostAndFoundApplicationTests {

    @Resource
    private MessageMapper messageMapper;

    @Test
    public void contextLoads() {
        MessageDetailVO messageDetailVO = messageMapper.detail(1);
    }

}
