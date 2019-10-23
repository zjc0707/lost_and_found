package com.jc.lost_and_found.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.lost_and_found.mapper.MessageMapper;
import com.jc.lost_and_found.model.BaseSearchParam;
import com.jc.lost_and_found.model.MessageDO;
import com.jc.lost_and_found.model.MessageDetailVO;
import com.jc.lost_and_found.model.MessagePageVO;
import com.jc.lost_and_found.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageDO> implements MessageService {
    @Override
    public IPage<MessagePageVO> page(Integer state, BaseSearchParam baseSearchParam) {
        Page<MessagePageVO> page = new Page<>(baseSearchParam.getPageIndex(), baseSearchParam.getPageSize());
        page.setRecords(super.baseMapper.page(state, page));
        return page;
    }

    @Override
    public IPage<MessagePageVO> pageMy(Integer state, BaseSearchParam baseSearchParam) {
        Page<MessagePageVO> page = new Page<>(baseSearchParam.getPageIndex(), baseSearchParam.getPageSize());
        page.setRecords(super.baseMapper.pageByUserId(state, baseSearchParam.getUserId(), page));
        return page;
    }

    @Override
    public MessageDetailVO detail(Integer id) {
        return super.baseMapper.detail(id);
    }
}
