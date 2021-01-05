package com.x.pcpcustom.assemble.control.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.x.pcpcustom.assemble.control.Business;
import com.x.pcpcustom.assemble.control.service.tools.HttpClientUtils;
import org.json.JSONObject;

public class O2oaService {
    //根据workid获取数据
    public String getDataByWorkId(String workId,String dataId,String xtoken) throws Exception {
        String pathUrl=getPathUrl();
        String functionUrl="/x_processplatform_assemble_surface/jaxrs/data/work/";
        String path = "http://" + pathUrl + functionUrl+workId;
        String retData=null;
        String retStr =  HttpClientUtils.getInstance().sendGet(path,xtoken);
        com.alibaba.fastjson.JSONObject jsonObject= JSON.parseObject(retStr);
        String retType=jsonObject.getString("type");
        if("success".equals(retType)){
            com.alibaba.fastjson.JSONObject jsonData=jsonObject.getJSONObject("data");
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
        String strflow =  HttpClientUtils.getInstance().sendPost2(path,xtoken,result.toString());
        return strflow;
    }
    //根据人员信息获取身份信息
    public String getIdentityByPerson(String person,String xtoken) throws Exception {

        String pathUrl=getPathUrl();
        String functionUrl="/x_organization_assemble_control/jaxrs/person/";
        String path = "http://" + pathUrl + functionUrl+person;
        String retIdentity=null;
        String retStr =  HttpClientUtils.getInstance().sendGet(path,xtoken);
        com.alibaba.fastjson.JSONObject jsonObject= JSON.parseObject(retStr);
        String retType=jsonObject.getString("type");
        if("success".equals(retType)){
            com.alibaba.fastjson.JSONObject jsonData=jsonObject.getJSONObject("data");
            JSONArray identityList =jsonData.getJSONArray("woIdentityList");
            if(identityList.size()!=0){
                com.alibaba.fastjson.JSONObject identityObj=identityList.getJSONObject(0);
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
    //读取配置信息中需要获取的表单标识数组
//    public JsonArray getFormIdArrayById(String processId) throws Exception {
//        JsonArray jsonArray = Business.readConfig("process").get("value").getAsJsonArray();
//        for(int i=0;i<jsonArray.size();i++){
//            JsonObject tempJsonObj=jsonArray.get(i).getAsJsonObject();
//            String tempid=tempJsonObj.get("processId").getAsString();
//            if(processId.equals(tempid)){
//                return tempJsonObj.get("retFormData").getAsJsonArray();
//            }
//        }
//        return null;
//    }

}
