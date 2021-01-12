package com.x.pcpcustom.assemble.control.action.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 创建流程方法返回值
 */
public class ProcessingReturnEntity {
    private String resultState;
    private JSONObject resultData;

    public String getResultState() {
        return resultState;
    }

    public void setResultState(String resultState) {
        this.resultState = resultState;
    }

    public JSONObject getResultData() {
        return resultData;
    }

    public void setResultData(JSONObject resultData) {
        this.resultData = resultData;
    }
}
