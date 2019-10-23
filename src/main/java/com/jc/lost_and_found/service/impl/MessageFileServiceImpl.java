package com.jc.lost_and_found.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.lost_and_found.mapper.MessageFileMapper;
import com.jc.lost_and_found.model.MessageFileDO;
import com.jc.lost_and_found.service.MessageFileService;
import org.springframework.stereotype.Service;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Service
public class MessageFileServiceImpl extends ServiceImpl<MessageFileMapper, MessageFileDO> implements MessageFileService {
}
