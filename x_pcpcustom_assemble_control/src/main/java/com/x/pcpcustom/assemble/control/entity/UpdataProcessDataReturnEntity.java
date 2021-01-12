package com.x.pcpcustom.assemble.control.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 流程数据更新方法返回值
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
