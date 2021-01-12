package com.x.pcpcustom.assemble.control.entity;

/**
 * 创建流程方法返回值
 */
public class CreateProcessReturnEntity {
    private String message;
    private String workId;//工作id
    private String serialNumber;//流程单号

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
