package com.jc.lost_and_found.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("message")
@EqualsAndHashCode(callSuper = false)
public class MessageDO extends BaseEntity<MessageDO>{

    public static final int STATE_CHECK = 0;
    public static final int STATE_NORMAL = 1;
    public static final int STATE_OVER = 2;
    public static final int STATE_CHECK_FAILURE = 3;


    public MessageDO(String title, Long userId, String content, Long deployTime, Integer state) {
        this.title = title;
        this.userId = userId;
        this.content = content;
        this.deployTime = deployTime;
        this.state = state;
    }

    private String title;
    private Long userId;
    private String content;
    private Long deployTime;
    //0-待审核，1-正常，2-已处理
    private Integer state;
    private Boolean top;
}
