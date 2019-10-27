package com.jc.lost_and_found.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zjc
 * @date 2019/10/9
 */
@Data
public class MessagePageVO implements Serializable {
    private Integer id;
    private String title;
    private Integer userId;
    private Integer state;
    private Long deployTime;
    private Boolean top;
    private String userName;
    /**
     * 所有对应role.id的','连接
     */
    private String role;
}
