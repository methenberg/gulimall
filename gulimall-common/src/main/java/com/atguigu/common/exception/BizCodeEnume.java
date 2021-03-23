package com.atguigu.common.exception;

public enum BizCodeEnume {
    UNKOWNM_EXCEPTION(10000,"系统未知异常"),
    VALID_EXCEPTION(10001,"参数格式校验失败"),
    VALID_SMS_CODE(10002,"短信验证码获取频率太高"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常"),
    USER_EXIST_EXCEPTION(15001,"用户已被注册"),
    PHONE_EXIST_EXCEPTION(15002,"手机已被注册"),
    LOGINACCT_PASSWORD_INVALID_EXCEPTION(15003,"账号密码错误");

    private int code;
    private String  msg;
    BizCodeEnume(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
