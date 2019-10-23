package com.jc.lost_and_found.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.lost_and_found.model.BaseSearchParam;
import com.jc.lost_and_found.model.MessageDO;
import com.jc.lost_and_found.model.MessageDetailVO;
import com.jc.lost_and_found.model.MessagePageVO;

/**
 * @author zjc
 * @date 2019/9/27
 */
public interface MessageService extends IService<MessageDO> {
    IPage<MessagePageVO> page(Integer state, BaseSearchParam baseSearchParam);
    IPage<MessagePageVO> pageMy(Integer state, BaseSearchParam baseSearchParam);
    MessageDetailVO detail(Integer id);
}
