package com.jc.lost_and_found.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.lost_and_found.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zjc
 * @date 2019/9/27
 */
public interface MessageService extends IService<MessageDO> {
    IPage<MessagePageVO> page(BaseSearchParam baseSearchParam);
    IPage<MessagePageVO> pageMy(BaseSearchParam baseSearchParam);
    MessageDetailVO detail(Long id);
    boolean updateState(Long id, Integer state);
    boolean topById(Long id, Boolean top);
    boolean removeById(Long id);
    void deploy(ShiroUserVO currentUser, BaseMessageDetailParam param);
    IPage<MessagePageVO> pageRemove(BaseSearchParam baseSearchParam);
    List<MessageDO> listDeployTimeLE(Long time);
}
