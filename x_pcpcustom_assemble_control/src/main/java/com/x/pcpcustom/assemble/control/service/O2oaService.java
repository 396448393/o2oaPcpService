package com.x.pcpcustom.assemble.control.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.x.pcpcustom.assemble.control.Business;
import com.x.pcpcustom.assemble.control.service.tools.HttpClientUtils;

/**
 * 调用系统内部接口和读取配置文件
 */
public class O2oaService {
    //根据workid获取流程单号
    public String getDataByWorkId(String workId,String dataId,String xtoken) throws Exception {
        String retData=null;
        JSONObject retJson=this.getWorkInfoOrWorkDataByWorkId(workId,"work",xtoken);
        if(retJson!=null){
            retData=retJson.getString(dataId);
        }
        return retData;
    }
    //根据workid获取data流程数据
    public String getProcessDataByWorkId(String workId,String dataId,String xtoken) throws Exception {
        String pathUrl=getPathUrl();
        String functionUrl="/x_processplatform_assemble_surface/jaxrs/data/work/";
        String path = "http://" + pathUrl + functionUrl+workId;
        String retData=null;
        String retStr =  HttpClientUtils.getInstance().sendGet(path,xtoken);
        JSONObject jsonObject= JSON.parseObject(retStr);
        String retType=jsonObject.getString("type");
        if("success".equals(retType)){
            JSONObject jsonData=jsonObject.getJSONObject("data");
            //com.alibaba.fastjson.JSONObject workData=jsonData.getJSONObject("$work");
            retData=jsonData.getString(dataId);
        }
        return retData;
    }
    /**根据workid获取流程信息和流程数据
     * 参数 ： workId
     *  dataType: 数据类型 work  或者 data
     *  xtoken:
     */
    public JSONObject getWorkInfoOrWorkDataByWorkId(String workId, String dataType, String xtoken) throws Exception {
        String pathUrl=getPathUrl();
        String functionUrl="/x_processplatform_assemble_surface/jaxrs/work/workorworkcompleted/";
        String path = "http://" + pathUrl + functionUrl+workId;
        JSONObject retData=null;
        String retStr =  HttpClientUtils.getInstance().sendGet(path,xtoken);
        JSONObject jsonObject= JSON.parseObject(retStr);
        String retType=jsonObject.getString("type");
        if("success".equals(retType)){
            JSONObject dataJson=jsonObject.getJSONObject("data");
            retData=dataJson.getJSONObject(dataType);
        }
        return retData;
    }

    //根据workid赋值
    public String setDataByWorkId(String workId,String dataId,String dataValue,String xtoken) throws Exception {
        String pathUrl=getPathUrl();
        String functionUrl="/x_processplatform_assemble_surface/jaxrs/data/work/";
        String path = "http://" + pathUrl + functionUrl+workId+"/mockputtopost";
        JSONObject result = new JSONObject();
        result.put(dataId, dataValue);
        String strflow =  HttpClientUtils.getInstance().sendPost2(path,xtoken,result.toJSONString());
        return strflow;
    }
    //根据人员信息获取身份信息
    public String getIdentityByPerson(String person,String xtoken) throws Exception {

        String pathUrl=getPathUrl();
        String functionUrl="/x_organization_assemble_control/jaxrs/person/";
        String path = "http://" + pathUrl + functionUrl+person;
        String retIdentity=null;
        String retStr =  HttpClientUtils.getInstance().sendGet(path,xtoken);
        JSONObject jsonObject= JSON.parseObject(retStr);
        String retType=jsonObject.getString("type");
        if("success".equals(retType)){
            JSONObject jsonData=jsonObject.getJSONObject("data");
            JSONArray identityList =jsonData.getJSONArray("woIdentityList");
            if(identityList.size()!=0){
                JSONObject identityObj=identityList.getJSONObject(0);
                retIdentity=identityObj.getString("distinguishedName");
            }
        }
        return retIdentity;
    }

    //读取配置信息中的环境信息
    public String getPathUrl() throws Exception {
        String runPath= Business.readConfig("runPath").get("value").getAsString();
        JsonObject serverPath= Business.readConfig("serverPath");
        JsonObject path = serverPath.get("value").getAsJsonObject().get(runPath).getAsJsonObject();
        String ip=path.get("ip").getAsString();
        String port=path.get("port").getAsString();
        return ip+":"+port;
    }
    //根据流程名读取配置信息中的id信息
    public String getProcessId(String processName) throws Exception {
        JsonArray jsonArray = Business.readConfig("process").get("value").getAsJsonArray();
        for(int i=0;i<jsonArray.size();i++){
            JsonObject tempJsonObj=jsonArray.get(i).getAsJsonObject();
            String tempName=tempJsonObj.get("processName").getAsString();
            if(tempName.indexOf(processName)!=-1){
                return tempJsonObj.get("processId").getAsString();
            }
        }
        return null;
    }
    //附件上传中根据workId获取配置信息中的附件id
    public String getAttachmentIdByWorkId(String workId,String xtoken) throws Exception {
        String processId = this.getProcessIdByworkId(workId,xtoken);
        return this.getAttachmentIdByProcessId(processId);
    }
    //根据workId获取流程id
    public String getProcessIdByworkId(String workId,String xtoken) throws Exception {
        JSONObject retJson = this.getWorkInfoOrWorkDataByWorkId(workId,"work",xtoken);
        String processId=null;
        processId=retJson.getString("process");
        return processId;
    }
    //根据流程id读取配置信息中的附件id
    public String getAttachmentIdByProcessId(String processId) throws Exception {
        if(processId==null || "".equals(processId)){
            throw new Exception("获取流程id为空！");
        }
        JsonArray jsonArray = Business.readConfig("process").get("value").getAsJsonArray();
        for(int i=0;i<jsonArray.size();i++){
            JsonObject tempJsonObj=jsonArray.get(i).getAsJsonObject();
            String tempid=tempJsonObj.get("processId").getAsString();
            if(processId.equals(tempid)){
                return tempJsonObj.get("site").getAsString();
            }
        }
        return null;
    }
    //读取配置信息中表单标题id
    public String getFormTitleId() throws Exception {
        String titleId=null;
        titleId = Business.readConfig("titleId").get("value").getAsString();

        return titleId;
    }
    //读取配置信息中表单单号id
//    public String getFormSerialId() throws Exception {
//        String serialId=null;
//        serialId = Business.readConfig("serialId").get("value").getAsString();
//
//        return serialId;
//    }
    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     *
     * @param str
     *            原始字符串
     * @param f
     *            开始位置
     * @param t
     *            结束位置
     * @return
     */
    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }
}
