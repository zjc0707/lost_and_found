package com.jc.lost_and_found.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.lost_and_found.mapper.RoleInfoMapper;
import com.jc.lost_and_found.model.RoleInfoDO;
import com.jc.lost_and_found.service.RoleInfoService;
import org.springframework.stereotype.Service;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfoDO> implements RoleInfoService {
}
