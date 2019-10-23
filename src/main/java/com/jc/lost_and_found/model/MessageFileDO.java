package com.jc.lost_and_found.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Data
@NoArgsConstructor
@TableName("message_file")
@EqualsAndHashCode(callSuper = false)
public class MessageFileDO extends BaseEntity<MessageFileDO> {
    public MessageFileDO(Long messageId, String fileUrl) {
        this.messageId = messageId;
        this.fileUrl = fileUrl;
    }

    private Long messageId;
    private String fileUrl;
}
