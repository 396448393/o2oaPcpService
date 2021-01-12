package com.x.pcpcustom.assemble.control.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.pcpcustom.assemble.control.action.ActionLogin;
import com.x.pcpcustom.assemble.control.entity.*;
import com.x.pcpcustom.assemble.control.service.tools.HttpClientUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 流程服务方法
 */
public class ProcessService {
    //登陆获取token
    private static Logger logger = LoggerFactory.getLogger( ActionLogin.class );

    private O2oaService o2oaService;

    private String serverPathUrl=null;

    public ProcessService () throws Exception {
        o2oaService = new O2oaService();
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
        String titleId = o2oaService.getFormTitleId();
        if(titleId==null || "".equals(titleId)){
            throw new Exception("读取配置信息中表单标题id失败！");
        }
        //组成请求参数
        JSONObject result =new JSONObject();
        result.put("latest", false);
        result.put("title", title);
        result.put("identity", identity);
        //将参数中的表单项放入
        String formDataJsonStr=formData.toString();
        JSONObject department = JSON.parseObject(formDataJsonStr);
        department.put(titleId, title);
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
        String serialId=o2oaService.getFormSerialId();
        if(serialId==null || "".equals(serialId)){
            throw new Exception("读取配置信息中单号Id失败！");
        }
        //获取表单单号
        String serial = o2oaService.getDataByWorkId(workId,"serial",xtoken);
        String serialNumber=null;
        if(serial!=null && !"".equals(serial)){
            String str1=o2oaService.substring(serial,0,4);
            String str2=o2oaService.substring(serial,4,12);
            String str3=o2oaService.substring(serial,12,serial.length());
            serialNumber=str1+"-"+str2+"-"+str3;
        }
        //保存单号
        JSONObject saveDataRet=o2oaService.updataData(workId,serialId,serialNumber,xtoken);
        String type=saveDataRet.getString("type");
        if("success".equals(type)){
            //返回单号
            retData.setSerialNumber(serialNumber);
        }else{
            throw new Exception("保存单号失败！");
        }
        return retData;
    }
    //上传文件
    public UploadFileReturnEntity uploadFile(String xtoken, String workId, String loginName, JsonObject file) throws Exception {

        String pathUrl=this.serverPathUrl;
        String functionUrl="/x_processplatform_assemble_surface/jaxrs/attachment/upload/work/";
        Map<String,String> retMap=new HashMap<>();
        UploadFileReturnEntity uploadFileReturnEntity= new UploadFileReturnEntity();
        String type="failure";
        try {
            String attachment = o2oaService.getAttachmentIdByWorkId(workId,xtoken);
            if(attachment==null || "".equals(attachment)){
                throw new Exception("读取配置信息中的附件标识为空！");
            }
            String fileName=file.get("filename").getAsString();
            String fileUrl=file.get("filepath").getAsString();
            Map<String,String> uploadParams = new LinkedHashMap<String, String>();
            uploadParams.put("fileName", fileName);
            uploadParams.put("site", attachment);//=====================
            uploadParams.put("extraParam","");

            Map<String, String> headMap = new HashMap<String, String>();
            headMap.put("Cookie", "x-token=" + xtoken);
            headMap.put("accept", "*/*");
            headMap.put("connection", "Keep-Alive");
            headMap.put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            String strurl = "http://"+pathUrl+functionUrl + workId;
            String retStr = HttpClientUtils.getInstance().uploadFileImpl(
                    strurl, fileUrl,
                    "file", uploadParams, headMap);
            JSONObject jsonObject = JSONObject.parseObject(retStr);
            type=jsonObject.getString("type");
            String attachmentId=null;


            if("success".equals(type)){
                JSONObject dataJson=jsonObject.getJSONObject("data");
                attachmentId=dataJson.getString("id");
                retMap.put("fileName",fileName);
                retMap.put("attachmentId",attachmentId);
            }else{
                retMap.put("fileName",fileName);
                retMap.put("false",type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("error",e.getMessage());
        }
        uploadFileReturnEntity.setResultData(retMap);
        uploadFileReturnEntity.setResultState(type);
        return uploadFileReturnEntity;
    }

    /**
     * 流转流程
     * @param workId
     * @param routeName
     * @param opinion
     * @param xtoken
     */
    public ProcessingReturnEntity processing(String workId, String routeName, String opinion, String xtoken) throws Exception {

        ProcessingReturnEntity processingReturnEntity =new ProcessingReturnEntity();
        //获取待办id
        JSONObject retIdObj=o2oaService.getIdByWorkId(workId,xtoken);
        String taskId=null;
        String type=retIdObj.getString("type");
        if("success".equals(type)){
            //返回单号
            JSONArray dataArr = retIdObj.getJSONArray("data");
            if(dataArr.size()>0){
                taskId=dataArr.getJSONObject(0).getString("id");
            }
        }else{
            throw new Exception("获取待办id失败！");
        }
        //流转流程
        if(taskId==null || "".equals(taskId)){
            throw new Exception("待办为空！");
        }
        JSONObject jsonObject=o2oaService.toProcessing(taskId,routeName,xtoken,opinion);
        type=jsonObject.getString("type");
        if("success".equals(type)){
            processingReturnEntity.setResultState(type);
            JSONObject dataObj=jsonObject.getJSONObject("data");
            JSONObject tempJson=dataObj;
            if(dataObj != null){
               JSONArray signalStack=dataObj.getJSONArray("signalStack");
                if(signalStack.size()>0){
                    JSONObject obj=signalStack.getJSONObject(0);
                    String name=obj.getString("name");
                    JSONArray identities=obj.getJSONObject("manualExecute").getJSONArray("identities");
                    tempJson = new JSONObject();
                    tempJson.put("name",name);
                    tempJson.put("identities",identities);
                }
            }
            processingReturnEntity.setResultData(tempJson);
        }else{
            processingReturnEntity.setResultState(type);
            JSONObject temp = new JSONObject();
            temp.put(type,"流程流转失败！");
            processingReturnEntity.setResultData(temp);
            throw new Exception("流程流转失败！");
        }

        return processingReturnEntity;
    }
    /**
     * 保存数据
     */
    public UpdataProcessDataReturnEntity upProcessData(String workId, String dataId, String dataValue, String xtoken) throws Exception {
        JSONObject saveDataRet=o2oaService.updataData(workId,dataId,dataValue,xtoken);
        UpdataProcessDataReturnEntity updataProcessDataReturnEntity=new UpdataProcessDataReturnEntity();
        String type=saveDataRet.getString("type");
        updataProcessDataReturnEntity.setResultState(type);
        if(!("success".equals(type))){
            JSONObject temp = new JSONObject();
            temp.put(type,"更新数据失败！");
            updataProcessDataReturnEntity.setResultData(temp);
            throw new Exception("更新数据失败！");
        }

        updataProcessDataReturnEntity.setResultData(saveDataRet.getJSONObject("data"));
        return updataProcessDataReturnEntity;
    }
}
