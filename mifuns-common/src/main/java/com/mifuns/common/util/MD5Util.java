package com.mifuns.common.util;


import java.security.MessageDigest;

/**
 * @author Stony Created Date : 2016/4/21  11:52
 */
public abstract class MD5Util {

    private static byte[] getBytes(String text){
        return text.getBytes(CharsetUtil.UTF_8);
    }
    /**
     * 返回MD5 加密字符串
     * @param text
     * @return
     * @throws Exception
     */
    public static String md5(String text) throws Exception{
        return md5(getBytes(text));
    }

    public static byte[] md5ToByte(String text) throws Exception{
        return md5ToByte(getBytes(text));
    }

    public static byte[] md5ToByte(byte[] bytes) throws Exception{
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bytes);
        return messageDigest.digest();
    }
    /**
     * 返回MD5 加密字符串
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String md5(byte[] bytes) throws Exception{
        byte[] use = md5ToByte(bytes);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < use.length; i++) {
            if ((use[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString(use[i] & 0xff, 16));
        }
        return sb.toString();
    }

    /**
     * MD5方法
     * @param text
     * @return 大写
     * @throws Exception
     */
    public static String md5UpperCase(String text) throws Exception {
        byte[] bytes = getBytes(text);
        return md5(bytes).toUpperCase();
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @param salt 盐
     * @return 密文
     * @throws Exception
     */
    public static String md5UpperCase(String text, String salt) throws Exception {
        byte[] bytes = getBytes((text + salt));
        return md5(bytes).toUpperCase();
    }

    /**
     * MD5方法
     * @param text
     * @return 小写
     * @throws Exception
     */
    public static String md5LowerCase(String text) throws Exception {
        byte[] bytes = getBytes(text);
        return md5(bytes).toLowerCase();
    }
    /**
     * MD5方法
     *
     * @param text 明文
     * @param salt 盐
     * @return 密文
     * @throws Exception
     */
    public static String md5LowerCase(String text, String salt) throws Exception {
        byte[] bytes = getBytes((text + salt));
        return md5(bytes).toLowerCase();
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param salt 盐
     * @param md5  密文
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String text, String salt, String md5) throws Exception {
        String md5Text = md5UpperCase(text, salt);
        if (md5Text.equalsIgnoreCase(md5)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * MD5验证
     * @param text
     * @param md5
     * @return
     * @throws Exception
     */
    public static boolean verify(String text, String md5) throws Exception {
        String md5Text = md5UpperCase(text);
        if (md5Text.equalsIgnoreCase(md5)) {
            return true;
        } else {
            return false;
        }
    }
}
