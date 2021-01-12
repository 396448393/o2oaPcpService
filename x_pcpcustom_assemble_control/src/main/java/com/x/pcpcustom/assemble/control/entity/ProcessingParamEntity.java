package com.x.pcpcustom.assemble.control.entity;

import com.x.base.core.project.annotation.FieldDescribe;

import java.util.Map;

/**
 * 流程流转方法参数
 */
public class ProcessingParamEntity {
    @FieldDescribe("提交路径")
    private String routeName;

    @FieldDescribe("创建流程返回的workId")
    private String workId;

    @FieldDescribe("xtoken")
    private String xtoken;

    @FieldDescribe("流程意见")
    private String opinion;

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
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

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
