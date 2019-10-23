package com.jc.lost_and_found.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @author zjc
 * @date 2019/9/27
 */
public class MyStringUtil {
    public static String reverse(String str){
        StringBuilder stringBuffer = new StringBuilder(str);
        return stringBuffer.reverse().toString();
    }
    public static String match(String pwd){
        return MyStringUtil.reverse(String.valueOf(new SimpleHash("MD5",pwd)));
    }
}
