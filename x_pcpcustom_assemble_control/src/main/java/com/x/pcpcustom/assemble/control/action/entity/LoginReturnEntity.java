package com.x.pcpcustom.assemble.control.action.entity;

/**
 * 登陆方法返回值
 */
public class LoginReturnEntity {
    private String xtoken;//返回的xtoken值
    private String message;

    public String getXtoken() {
        return xtoken;
    }

    public void setXtoken(String xtoken) {
        this.xtoken = xtoken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
