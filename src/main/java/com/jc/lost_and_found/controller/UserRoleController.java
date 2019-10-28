package com.jc.lost_and_found.controller;

import com.jc.lost_and_found.component.ResponseData;
import com.jc.lost_and_found.component.ResponseDataUtil;
import com.jc.lost_and_found.model.BaseSearchParam;
import com.jc.lost_and_found.model.UserRoleDO;
import com.jc.lost_and_found.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjc
 * @date 2019/10/28
 */
@Slf4j
@RestController
@RequestMapping("userRole")
public class UserRoleController extends AbstractCrudController<UserRoleService, UserRoleDO>{

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("editRole")
    @RequiresPermissions("/userRole/editRole")
    public ResponseData editRole(@ModelAttribute BaseSearchParam baseSearchParam){
        try{
            userRoleService.editRole(baseSearchParam);
            return ResponseDataUtil.buildSuccess();
        }catch (Exception e){
            log.error(e.toString());
            return ResponseDataUtil.buildError();
        }
    }
}
