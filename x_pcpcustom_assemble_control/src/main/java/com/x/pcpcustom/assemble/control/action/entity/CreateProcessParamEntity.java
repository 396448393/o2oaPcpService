package com.x.pcpcustom.assemble.control.action.entity;

import com.x.base.core.project.annotation.FieldDescribe;

import java.util.Map;

/**
 * 创建流程方法参数
 */
public class CreateProcessParamEntity {
    @FieldDescribe("启动流程名")
    private String processName;

    @FieldDescribe("流程标题")
    private String title;

    @FieldDescribe("流程启动人")
    private String startProcessPerson;

    @FieldDescribe("xtoken")
    private String xtoken;

    @FieldDescribe("表单数据")
    private Map<String,String> data;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartProcessPerson() {
        return startProcessPerson;
    }

    public void setStartProcessPerson(String startProcessPerson) {
        this.startProcessPerson = startProcessPerson;
    }

    public String getXtoken() {
        return xtoken;
    }

    public void setXtoken(String xtoken) {
        this.xtoken = xtoken;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
