package com.jc.lost_and_found.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jc.lost_and_found.component.ResponseData;
import com.jc.lost_and_found.component.ResponseDataUtil;
import com.jc.lost_and_found.constant.ResultEnums;
import com.jc.lost_and_found.model.*;
import com.jc.lost_and_found.service.FileMongoService;
import com.jc.lost_and_found.service.MessageFileService;
import com.jc.lost_and_found.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Slf4j
@RestController
@RequestMapping("message")
public class MessageController extends AbstractCrudController<MessageService, MessageDO> {

    @Autowired
    MessageService messageService;

    @Autowired
    FileMongoService fileMongoService;

    @Autowired
    MessageFileService messageFileService;

    /**
     * 查看消息列表，未解决的 和 已解决的
     */
    @PostMapping("page")
    public ResponseData page(Integer state, @ModelAttribute BaseSearchParam baseSearchParam){
        if (state != MessageDO.STATE_NORMAL && state != MessageDO.STATE_OVER){
            return ResponseDataUtil.buildSend(ResultEnums.PARAM_ERROR);
        }
        IPage<MessagePageVO> page = messageService.page(state ,baseSearchParam);

        if(page.getRecords().isEmpty()){
            return ResponseDataUtil.buildSend(ResultEnums.RESULT_IS_NULL);
        }
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 查看详情信息，anon
     * @param id message id
     * @return message
     */
    @PostMapping("detail")
    public ResponseData detail(Integer id){
        MessageDetailVO detail = messageService.detail(id);
        if(detail == null){
            return ResponseDataUtil.buildError();
        }
        return ResponseDataUtil.buildSuccess(detail);
    }

    /**
     * 查看自己发布的消息，三种状态皆可， auth
     */
    @PostMapping("pageMy")
    public ResponseData pageMy(Integer state, @ModelAttribute BaseSearchParam baseSearchParam){
        UserBaseInfoDO currentUser = (UserBaseInfoDO) SecurityUtils.getSubject().getPrincipal();
        baseSearchParam.setUserId(currentUser.getId());
        IPage<MessagePageVO> page = messageService.pageMy(state ,baseSearchParam);

        if(page.getRecords().isEmpty()){
            return ResponseDataUtil.buildSend(ResultEnums.RESULT_IS_NULL);
        }
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 发布消息，auth
     * @param title 标题
     * @param content 内容
     * @param files 配图，限制<=2
     * @return 是否成功
     */
    @PostMapping("deploy")
    public ResponseData deploy(String title, String content, List<MultipartFile> files){
        if(files.size() > 2){
            return ResponseDataUtil.buildSend(ResultEnums.PARAM_ERROR);
        }
        ShiroUserVO currentUser = (ShiroUserVO) SecurityUtils.getSubject().getPrincipal();
        MessageDO messageDO =
                new MessageDO(title, currentUser.getId(), content, System.currentTimeMillis()/1000,
                        //管理员等有身份的用户发布无需再审核
                        currentUser.getRoles().size() > 0 ? MessageDO.STATE_NORMAL : MessageDO.STATE_CHECK);
        try {
            messageService.save(messageDO);
            for(MultipartFile file : files){
                String fileUrl = fileMongoService.saveFile(file);
                messageFileService.save(new MessageFileDO(messageDO.getId(), fileUrl));
            }
        }catch (Exception e){
            if(messageDO.getId() != null){
                messageService.removeById(messageDO.getId());
            }
            log.error("deploy fail : {}", e.getMessage());
            return ResponseDataUtil.buildError();
        }
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 查看 需要审核 和 审核失败 的消息，需要修改message状态同一权限
     */
    @RequiresPermissions("/message/updateState")
    @PostMapping("pageCheck")
    public ResponseData pageCheck(Integer state, @ModelAttribute BaseSearchParam baseSearchParam){
        IPage<MessagePageVO> page = messageService.page(state ,baseSearchParam);

        if(page.getRecords().isEmpty()){
            return ResponseDataUtil.buildSend(ResultEnums.RESULT_IS_NULL);
        }
        return ResponseDataUtil.buildSuccess(page);
    }
    /**
     * 修改message状态，需要相应权限
     * @param id message id
     * @return 是否成功
     */
    @RequiresPermissions("/message/updateState")
    @PostMapping("updateState")
    public ResponseData updateState(Long id, Integer state){
        MessageDO update = new MessageDO();
        update.setId(id);
        update.setState(state);
        try{
            messageService.updateById(update);
        }catch (Exception e){
            log.error("deploy fail : {}", e.getMessage());
            return ResponseDataUtil.buildError();
        }
        return ResponseDataUtil.buildSuccess();
    }

    @RequiresPermissions("/message/topById")
    @PostMapping("topById")
    public ResponseData topById(Long id, boolean top){
        MessageDO update = new MessageDO();
        update.setId(id);
        update.setTop(top);
        try{
            messageService.updateById(update);
        }catch (Exception e){
            log.error("deploy fail : {}", e.getMessage());
            return ResponseDataUtil.buildError();
        }
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据message_id删去三个关联表的内容
     * @param id 表id
     */
    @Override
    @RequiresPermissions("/message/updateState")
    @PostMapping("removeById")
    public ResponseData removeById(Long id) {
        try{
            MessageFileDO searchTarget = new MessageFileDO();
            searchTarget.setMessageId(id);
            List<MessageFileDO> messageFileDOList = messageFileService.list(Wrappers.query(searchTarget));
            for(MessageFileDO messageFileDO : messageFileDOList){
                fileMongoService.removeFile(messageFileDO.getFileUrl());
                messageFileService.removeById(messageFileDO.getId());
            }

            return super.removeById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseDataUtil.buildError();
        }
    }
}
