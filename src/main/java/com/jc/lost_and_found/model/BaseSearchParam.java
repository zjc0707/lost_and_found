package com.jc.lost_and_found.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author zjc
 * @date 2019/10/9
 */
@Data
@ToString
public class BaseSearchParam {

    private Integer pageIndex;
    private Integer pageSize;
    private Long userId;
    private Integer state;
}
