package com.x.pcpcustom.assemble.control.entity;

import java.util.Map;

/**
 * 附件上传方法返回
 */
public class UploadFileReturnEntity {
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
