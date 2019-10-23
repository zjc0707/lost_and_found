package com.jc.lost_and_found.component;
import com.jc.lost_and_found.constant.ResultEnums;

/**
 * 用于对前后端数据交互的规范化封装
 * @author  sample
 * @date    2019/06/30
 */
public class ResponseDataUtil {
    /**
     * 带实体的统一返回
     * @param data 实体
     * @param <T>  实体类型
     * @return
     */
    public static <T> ResponseData buildSuccess(T data) {
        return new ResponseData<>(ResultEnums.SUCCESS, data);
    }

    public static ResponseData buildSuccess() {
        return new ResponseData(ResultEnums.SUCCESS);
    }

    public static ResponseData buildSend(Integer code, String msg) {
        return new ResponseData(code, msg);
    }

    public static <T> ResponseData buildSend(Integer code, String msg, T data) {
        return new ResponseData<>(code, msg, data);
    }

    public static ResponseData buildSend(ResultEnums resultEnums) {
        return new ResponseData(resultEnums);
    }

    public static <T> ResponseData buildSend(ResultEnums resultEnums,T data) {
        return new ResponseData(resultEnums,data);
    }

    public static <T> ResponseData buildError(T data) {
        return new ResponseData<>(ResultEnums.ERROR, data);
    }

    public static ResponseData buildError() {
        return new ResponseData(ResultEnums.ERROR);
    }

    public static ResponseData buildIllegal(){
        return new ResponseData(ResultEnums.ILLEGAL_ERROR);
    }

}
