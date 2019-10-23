package com.jc.lost_and_found.component;

import com.jc.lost_and_found.constant.ResultEnums;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据规范化封装类
 * @author  sample
 * @date    2019/06/30
 */
@Data
public class ResponseData<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public ResponseData(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseData(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseData(ResultEnums resultEnums) {
        this.code = resultEnums.getCode();
        this.msg = resultEnums.getMsg();
    }

    public ResponseData(ResultEnums resultEnums, T data) {
        this.code = resultEnums.getCode();
        this.msg = resultEnums.getMsg();
        this.data = data;
    }
}
