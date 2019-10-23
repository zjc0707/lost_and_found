package com.jc.lost_and_found.model;

import lombok.Data;

import java.util.List;

/**
 * @author zjc
 * @date 2019/10/17
 */
@Data
public class MessageDetailVO {
    private Integer id;
    private String title;
    private Integer userId;
    private String content;
    private Long deployTime;
    private Boolean top;
    private String userName;
    //0-待审核，1-正常，2-已处理
    private Integer state;
    private List<String> fileUrls;

    private String qq;
    private String wechat;
    private String telephone;
}
