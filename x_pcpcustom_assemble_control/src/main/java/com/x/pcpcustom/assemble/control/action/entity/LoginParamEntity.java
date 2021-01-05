package com.x.pcpcustom.assemble.control.action.entity;

import com.x.base.core.project.annotation.FieldDescribe;

public class LoginParamEntity {
    @FieldDescribe("用户名")
    private String loginUser;

    @FieldDescribe("密码")
    private String passWord;

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
