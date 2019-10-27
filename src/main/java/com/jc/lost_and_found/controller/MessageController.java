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

    private static final int FILE_MAX_COUNT = 2;

    @Autowired
    MessageService messageService;
    /**
     * 查看消息列表，未解决的 和 已解决的
     */
    @PostMapping("page")
    public ResponseData page(@ModelAttribute BaseSearchParam baseSearchParam){
        if (baseSearchParam.getState() != MessageDO.STATE_NORMAL && baseSearchParam.getState() != MessageDO.STATE_OVER){
            return ResponseDataUtil.buildSend(ResultEnums.PARAM_ERROR);
        }
        IPage<MessagePageVO> page = messageService.page(baseSearchParam);

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
    public ResponseData detail(Long id){
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
    public ResponseData pageMy(@ModelAttribute BaseSearchParam baseSearchParam){
        UserBaseInfoDO currentUser = (UserBaseInfoDO) SecurityUtils.getSubject().getPrincipal();
        baseSearchParam.setUserId(currentUser.getId());
        IPage<MessagePageVO> page = messageService.pageMy(baseSearchParam);

        if(page.getRecords().isEmpty()){
            return ResponseDataUtil.buildSend(ResultEnums.RESULT_IS_NULL);
        }
        return ResponseDataUtil.buildSuccess(page);
    }
    /**
     * 发布消息，auth
     * @return 是否成功
     */
    @PostMapping("deploy")
    public ResponseData deploy(@ModelAttribute BaseMessageDetailParam param){
        if(param.getFiles().size() > FILE_MAX_COUNT){
            return ResponseDataUtil.buildSend(ResultEnums.PARAM_ERROR);
        }
        try {
            ShiroUserVO currentUser = (ShiroUserVO) SecurityUtils.getSubject().getPrincipal();
            messageService.deploy(currentUser, param);
        }catch (Exception e){
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
    public ResponseData pageCheck(@ModelAttribute BaseSearchParam baseSearchParam){
        IPage<MessagePageVO> page = messageService.page(baseSearchParam);
        if(page.getRecords().isEmpty()){
            return ResponseDataUtil.buildSend(ResultEnums.RESULT_IS_NULL);
        }
        return ResponseDataUtil.buildSuccess(page);
    }

    @RequiresPermissions("/message/removeById")
    @PostMapping("pageRemove")
    public ResponseData pageRemove(@ModelAttribute BaseSearchParam baseSearchParam){
        IPage<MessagePageVO> page = messageService.pageRemove(baseSearchParam);
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
        try{
            messageService.updateState(id, state);
        }catch (Exception e){
            log.error("deploy fail : {}", e.getMessage());
            return ResponseDataUtil.buildError();
        }
        return ResponseDataUtil.buildSuccess();
    }

    @RequiresPermissions("/message/topById")
    @PostMapping("topById")
    public ResponseData topById(Long id, boolean top){
        try{
            messageService.topById(id, top);
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
    @RequiresPermissions("/message/removeById")
    @PostMapping("removeById")
    public ResponseData removeById(Long id) {
        try{
            messageService.removeById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseDataUtil.buildError();
        }
        return ResponseDataUtil.buildSuccess();
    }
}
