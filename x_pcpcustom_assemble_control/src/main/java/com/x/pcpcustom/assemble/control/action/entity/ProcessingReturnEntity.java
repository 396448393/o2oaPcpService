package com.x.pcpcustom.assemble.control.action.entity;

import java.util.Map;

/**
 * 创建流程方法返回值
 */
public class ProcessingReturnEntity {
    private String resultState;
    private Map<String,String> resultData;

    public String getResultState() {
        return resultState;
    }

    public void setResultState(String resultState) {
        this.resultState = resultState;
    }

    public Map<String, String> getResultData() {
        return resultData;
    }

    public void setResultData(Map<String, String> resultData) {
        this.resultData = resultData;
    }
}
