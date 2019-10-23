package com.jc.lost_and_found.model;

import lombok.Data;

/**
 * @author zjc
 * @date 2019/10/9
 */
@Data
public class BaseSearchParam {

    private Integer pageIndex;
    private Integer pageSize;
    private Long userId;
}
