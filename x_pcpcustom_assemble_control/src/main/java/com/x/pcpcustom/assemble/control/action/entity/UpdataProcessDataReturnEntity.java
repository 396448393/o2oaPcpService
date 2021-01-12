package com.x.pcpcustom.assemble.control.action.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 创建流程方法返回值
 */
public class UpdataProcessDataReturnEntity {
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
