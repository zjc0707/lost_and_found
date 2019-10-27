package com.jc.lost_and_found.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.lost_and_found.mapper.MessageMapper;
import com.jc.lost_and_found.model.*;
import com.jc.lost_and_found.service.FileMongoService;
import com.jc.lost_and_found.service.MessageFileService;
import com.jc.lost_and_found.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Slf4j
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageDO> implements MessageService {

    @Autowired
    FileMongoService fileMongoService;

    @Autowired
    MessageFileService messageFileService;

    @Cacheable(value = "messagePage", key = "#baseSearchParam")
    @Override
    public IPage<MessagePageVO> page(BaseSearchParam baseSearchParam) {
        Page<MessagePageVO> page = new Page<>(baseSearchParam.getPageIndex(), baseSearchParam.getPageSize());
        page.setRecords(super.baseMapper.page(baseSearchParam.getState(), page));
        return page;
    }

    @Cacheable(value = "messagePage", key = "'my_' + #baseSearchParam")
    @Override
    public IPage<MessagePageVO> pageMy(BaseSearchParam baseSearchParam) {
        Page<MessagePageVO> page = new Page<>(baseSearchParam.getPageIndex(), baseSearchParam.getPageSize());
        page.setRecords(super.baseMapper.pageByUserId(baseSearchParam.getState(), baseSearchParam.getUserId(), page));
        return page;
    }

    @Cacheable(value = "messagePage", key = "'remove_' + #baseSearchParam")
    @Override
    public IPage<MessagePageVO> pageRemove(BaseSearchParam baseSearchParam) {
        Page<MessagePageVO> page = new Page<>(baseSearchParam.getPageIndex(), baseSearchParam.getPageSize());
        page.setRecords(super.baseMapper.pageRemove(page));
        return page;
    }

    @Cacheable(value = "messagePageDetail")
    @Override
    public MessageDetailVO detail(Long id) {
        return super.baseMapper.detail(id);
    }

    @CacheEvict(value = {"messagePage", "messagePageDetail"}, allEntries = true)
    @Override
    public boolean updateState(Long id, Integer state) {
        MessageDO update = new MessageDO();
        update.setId(id);
        update.setState(state);
        return super.updateById(update);
    }

    @CacheEvict(value = {"messagePage"}, allEntries = true)
    @Override
    public boolean topById(Long id, Boolean top) {
        MessageDO update = new MessageDO();
        update.setId(id);
        update.setTop(top);
        return super.updateById(update);
    }

    @CacheEvict(value = {"messagePage", "messagePageDetail"}, allEntries = true)
    @Override
    public boolean removeById(Long id) {
        MessageFileDO searchTarget = new MessageFileDO();
        searchTarget.setMessageId(id);
        List<MessageFileDO> messageFileDOList = messageFileService.list(Wrappers.query(searchTarget));
        for(MessageFileDO messageFileDO : messageFileDOList){
            fileMongoService.removeFile(messageFileDO.getFileUrl());
            messageFileService.removeById(messageFileDO.getId());
        }
        return super.removeById(id);
    }

    @CacheEvict(value = {"messagePage"}, allEntries = true)
    @Override
    public void deploy(ShiroUserVO currentUser, BaseMessageDetailParam param) {
        MessageDO messageDO =
                new MessageDO(param.getTitle(), currentUser.getId(), param.getContent(), System.currentTimeMillis()/1000,
                        //管理员等有身份的用户发布无需再审核
                        currentUser.getRoles().size() > 0 ? MessageDO.STATE_NORMAL : MessageDO.STATE_CHECK);
        super.save(messageDO);
        for(MultipartFile file : param.getFiles()){
            String fileUrl = fileMongoService.saveFile(file);
            messageFileService.save(new MessageFileDO(messageDO.getId(), fileUrl));
        }
    }
}
