package com.x.pcpcustom.assemble.control.action.entity;

import com.x.base.core.project.annotation.FieldDescribe;

/**
 * 附件上传方法参数
 */
public class UpdataProcessDataParamEntity {
    @FieldDescribe("workId")
    private String workId;

    @FieldDescribe("数据表单标识")
    private String dataId;

    @FieldDescribe("数据值")
    private String dataValue;

    @FieldDescribe("xtoken")
    private String xtoken;

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getXtoken() {
        return xtoken;
    }

    public void setXtoken(String xtoken) {
        this.xtoken = xtoken;
    }
}
