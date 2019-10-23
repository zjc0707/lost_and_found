package com.jc.lost_and_found.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zjc
 * @date 2019/7/4 14:44
 */
@Data
@NoArgsConstructor
@TableName("controller_url")
@EqualsAndHashCode(callSuper = false)
public class ControllerUrlDO extends BaseEntity<ControllerUrlDO> {

    /*public ControllerUrlDO(String url, String urlDescribe) {
        this.url = url;
        this.urlDescribe = urlDescribe;
    }*/

    private String url;
    private String urlDescribe;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
