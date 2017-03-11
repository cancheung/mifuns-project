package com.mifuns.common.web.contants;

/**
 * Created by miguangying on 2017/3/11.
 */
public class ResponseConstant {
    /**
     * 成功
     **/
    public static final int CODE_SUCUESS = 100000;
    public static final String MSG_SUCUESS = "成功";
    /**
     * 身份验证无效
     **/
    public static final int CODE_TOKEN_INVALID = 100001;
    public static final String MSG_TOKEN_INVALID = "身份验证无效";



    /**
     * 签名无效
     **/
    public static final int CODE_SIGN_INVALID = 100002;
    public static final String MSG_SIGN_INVALID = "请求签名无效";


    public static final int CODE_PARAMETER_INVALID = 100003;
    public static final String MSG_PARAMETER_INVALID = "请求签名无效";


    /**
     * 服务器出现错误请联系管理员
     **/
    public static final int CODE_ERROR_INVALID = 100004;
    public static final String MSG_ERROR_INVALID = "服务器出现错误请联系管理员";


    /**
     * 无效的参数
     **/
    public static final int CODE_PARM_INVALID = 100005;
    public static final String MSG_PARM_INVALID = "无效的参数";

    public static final int CODE_LOGIN_INVALID = 100006;
    public static final String MSG_LOGIN_INVALID = "用户名或密码错误";
}
