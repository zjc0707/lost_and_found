package com.jc.lost_and_found.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.lost_and_found.model.MessageFileDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zjc
 * @date 2019/9/27
 */
public interface MessageFileMapper extends BaseMapper<MessageFileDO> {

    @Select("SELECT file_url FROM message_file WHERE message_id = #{messageId}")
    List<String> listByMessageId(Integer messageId);
}
