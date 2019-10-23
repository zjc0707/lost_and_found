package com.jc.lost_and_found.model;

import lombok.Data;

/**
 * @author zjc
 * @date 2019/10/9
 */
@Data
public class MessagePageVO {
    private Integer id;
    private String title;
    private Integer userId;
    private Long deployTime;
    private Boolean top;
    private String userName;
    /**
     * 所有对应role.id的','连接
     */
    private String role;
}
