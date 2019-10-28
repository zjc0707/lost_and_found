package com.jc.lost_and_found.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.lost_and_found.mapper.provider.UserBaseInfoProvider;
import com.jc.lost_and_found.model.BaseSearchParam;
import com.jc.lost_and_found.model.UserBaseInfoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author zjc
 * @date 2019/9/27
 */
public interface UserBaseInfoMapper extends BaseMapper<UserBaseInfoDO> {

    @SelectProvider(type = UserBaseInfoProvider.class, method = "pageByRoleId")
    List<UserBaseInfoDO> pageByRoleId(BaseSearchParam baseSearchParam, Page<UserBaseInfoDO> page);
}
