package com.jc.lost_and_found.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.lost_and_found.model.MessageDO;
import com.jc.lost_and_found.model.MessageDetailVO;
import com.jc.lost_and_found.model.MessagePageVO;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zjc
 * @date 2019/9/27
 */
public interface MessageMapper extends BaseMapper<MessageDO> {

    @Select("SELECT m.id, m.title, m.user_id, m.deploy_time, m.top, u.user_name, GROUP_CONCAT(ur.role_id) AS role " +
            "FROM message m INNER JOIN user_base_info u ON u.id = m.user_id " +
            "LEFT JOIN user_role ur ON u.id = ur.user_id " +
            "WHERE m.state = #{state} " +
            "AND m.delete_flag = 0 " +
            "GROUP BY m.id " +
            "ORDER BY m.top DESC, m.deploy_time DESC")
    List<MessagePageVO> page(Integer state, Page<MessagePageVO> page);

    @Select("SELECT id, title, user_id, deploy_time, top " +
            "FROM message " +
            "WHERE user_id = #{userId} " +
            "AND state = #{state} " +
            "AND delete_flag = 0 " +
            "ORDER BY top DESC, deploy_time DESC")
    List<MessagePageVO> pageByUserId(Integer state, Long userId, Page<MessagePageVO> page);

    @Select("SELECT m.id, m.title, m.user_id, m.content, m.deploy_time, m.top, m.state, u.user_name, u.qq, u.wechat, u.telephone " +
            "FROM message m " +
            "LEFT JOIN user_base_info u ON u.id = m.user_id " +
            "WHERE m.id = #{id} ")
    @Results({
            @Result(property = "fileUrls", column = "id", javaType = List.class,
                    many = @Many(select = "com.jc.lost_and_found.mapper.MessageFileMapper.listByMessageId"))
    })
    MessageDetailVO detail(Integer id);
}
