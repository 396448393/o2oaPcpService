package com.x.pcpcustom.assemble.control.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.pcpcustom.assemble.control.Business;
import com.x.pcpcustom.assemble.control.action.ActionLogin;
import com.x.pcpcustom.assemble.control.action.entity.CreateProcessReturnEntity;
import com.x.pcpcustom.assemble.control.action.entity.LoginReturnEntity;
import com.x.pcpcustom.assemble.control.service.tools.HttpClientUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProcessService {
    //登陆获取token
    private static Logger logger = LoggerFactory.getLogger( ActionLogin.class );

    private static O2oaService o2oaService = new O2oaService();

    private String serverPathUrl=null;

    public ProcessService () throws Exception {
        this.serverPathUrl=o2oaService.getPathUrl();
    }


    public LoginReturnEntity getTockenToLogin(String userName, String passWord) throws Exception {

        String pathUrl=this.serverPathUrl;

        String urlLogin="/x_organization_assemble_authentication/jaxrs/authentication";
        String loginUrl = "http://" + pathUrl + urlLogin;
        String loginParams = String.format("{'credential':'%s','password':'%s'}", userName, passWord);
        String responseData = HttpClientUtils.getInstance().sendPost(loginUrl, loginParams);

        com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(responseData);
        String retType=jsonObject.getString("type");
        com.alibaba.fastjson.JSONObject jsonData=jsonObject.getJSONObject("data");
        String retToken=jsonData.getString("token");
        LoginReturnEntity retData=new LoginReturnEntity();
        String xtoken="";
        if("success".equals(retType)){
            xtoken=retToken;
        }
        retData.setXtoken(xtoken);
        retData.setMessage(retType);

        return retData;
    }
    //创建启动流程
    public CreateProcessReturnEntity createProcess(String processName, String title, String xtoken, String person, JsonObject formData) throws Exception {
        //获取环境ip和端口号
        String pathUrl=this.serverPathUrl;
        String functionUrl="/x_processplatform_assemble_surface/jaxrs/work/process/";
        //获取人员身份信息
        String identity=o2oaService.getIdentityByPerson(person,xtoken);
        if(identity==null || "".equals(identity)){
            throw new Exception("获取人员身份信息为空");
        }
        //获取流程id
        String processId=o2oaService.getProcessId(processName);
        if(processId==null || "".equals(processId)){
            throw new Exception("获取流程id为空！");
        }
        String path = "http://" + pathUrl + functionUrl+processId;
        //组成请求参数
        //JsonObject result = new JsonObject();
        JSONObject result =new JSONObject();
        result.put("latest", false);
        result.put("title", title);
        result.put("identity", identity);

        JSONObject department = new JSONObject();
        department.put("subject", title);
        result.put("data", department);
        //请求创建流程
        String retStr =  HttpClientUtils.getInstance().sendPost2(path,xtoken,result.toJSONString());
        //处理返回数据
        com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(retStr);
        String retType=jsonObject.getString("type");
        com.alibaba.fastjson.JSONArray jsonDataArray=jsonObject.getJSONArray("data");
        String workId=null;
        if("success".equals(retType)){
            workId=jsonDataArray.getJSONObject(0).getString("work");
        }
        if(workId==null || "".equals(workId)){
            throw new Exception("获取workId失败！");
        }
        CreateProcessReturnEntity retData=new CreateProcessReturnEntity();
        retData.setMessage(retType);
        retData.setWorkId(workId);

        //读取配置信息中的单号id
//        JsonArray jsonArray = o2oaService.getFormIdArrayById(processId);
//        String serialNumberId=null;
//        for(int i=0;i<jsonArray.size();i++){
//            JsonObject jsonObj = jsonArray.get(i).getAsJsonObject();
//            if(jsonObj.get("serialNumber")!=null){
//                serialNumberId=jsonObj.get("serialNumber").getAsString();
//            }
//            if(serialNumberId!=null && !"".equals(serialNumberId)){
//                break;
//            }
//        }
//        if(serialNumberId==null || "".equals(serialNumberId)){
//            throw new Exception("读取配置信息中的单号id失败或为空！");
//        }
        //获取表单单号
        String serialNumber = o2oaService.getDataByWorkId(workId,"serialNumber",xtoken);
        retData.setSerialNumber(serialNumber);

        return retData;
    }
    //上传文件
    public void uploadFile(String xtoken,String work,String fileName,String fileUrl) throws Exception {

        String pathUrl=this.serverPathUrl;
        String functionUrl="/x_processplatform_assemble_surface/jaxrs/attachment/upload/work/";
        try {
            Map<String,String> uploadParams = new LinkedHashMap<String, String>();
            uploadParams.put("fileName", fileName);
            uploadParams.put("site", "attachment");//=====================
            uploadParams.put("extraParam","");

            Map<String, String> headMap = new HashMap<String, String>();
            headMap.put("Cookie", "x-token=" + xtoken);
            headMap.put("accept", "*/*");
            headMap.put("connection", "Keep-Alive");
            headMap.put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            String strurl = "http://"+pathUrl+functionUrl + work;
            HttpClientUtils.getInstance().uploadFileImpl(
                    strurl, fileUrl,
                    "file", uploadParams, headMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
