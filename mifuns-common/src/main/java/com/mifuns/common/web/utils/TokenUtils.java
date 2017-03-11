package com.mifuns.common.web.utils;


import com.mifuns.common.util.MD5Util;
import com.mifuns.common.util.RandomsUtil;

import java.util.UUID;

/**
 * Created by miguangying on 2016/12/24.
 */
public class TokenUtils {
    /**
     * 生成Token
     * @param args
     * @return
     */
    public static String generateToken(String...args){
        StringBuffer buffer = new StringBuffer();
        for(String arg : args){
            buffer.append(arg);
        }
        buffer.append(RandomsUtil.generateCode());
        try {
            buffer.append(UUID.randomUUID());
            return MD5Util.md5UpperCase(buffer.toString());
        } catch (Exception e) {
            return buffer.toString();
        }
    }

    public static String generateAppKey(){
        String key = "AK47" + RandomsUtil.generateCode() + "_" + System.currentTimeMillis();
        byte[] keyByte = key.getBytes();
        return UUID.nameUUIDFromBytes(keyByte).toString();
    }
    public static String generateAppSecret(){
        String key = "M16" + RandomsUtil.generateCode() + "_" + System.currentTimeMillis();
        byte[] keyByte = key.getBytes();
        return UUID.nameUUIDFromBytes(keyByte).toString();
    }
}
