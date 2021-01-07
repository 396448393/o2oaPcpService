package com.x.pcpcustom.assemble.control.action.entity;

import com.x.base.core.project.annotation.FieldDescribe;

import java.util.Map;

/**
 * 附件上传方法参数
 */
public class UploadFileParamEntity {
    @FieldDescribe("附件上传用户")
    private String loginName;

    @FieldDescribe("创建流程返回的workId")
    private String workId;

    @FieldDescribe("xtoken")
    private String xtoken;

    @FieldDescribe("附件列表")
    private Map<String,String> [] fileList;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getXtoken() {
        return xtoken;
    }

    public void setXtoken(String xtoken) {
        this.xtoken = xtoken;
    }

    public Map<String, String>[] getFileList() {
        return fileList;
    }

    public void setFileList(Map<String, String>[] fileList) {
        this.fileList = fileList;
    }
}
