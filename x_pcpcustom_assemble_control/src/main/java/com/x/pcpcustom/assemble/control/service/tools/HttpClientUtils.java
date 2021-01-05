package com.x.pcpcustom.assemble.control.service.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
    public static final int THREAD_POOL_SIZE = 5;
    public interface HttpClientDownLoadProgress {
        public void onProgress(int progress);
    }

    private static HttpClientUtils httpClientDownload;

    private ExecutorService downloadExcutorService;

    private HttpClientUtils() {
        downloadExcutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public static HttpClientUtils getInstance() {
        if (httpClientDownload == null) {
            httpClientDownload = new HttpClientUtils();
        }
        return httpClientDownload;
    }

    /**
     *
     *
     * @param url
     * @param filePath
     */
    public void download(final String url, final String filePath) {
        downloadExcutorService.execute(new Runnable() {

            @Override
            public void run() {
                httpDownloadFile(url, filePath, null, null);
            }
        });
    }

    /**
     * �
     *
     * @param url
     * @param filePath
     * @param progress
     *          
     */
    public void download(final String url, final String filePath,
                         final HttpClientDownLoadProgress progress) {
        downloadExcutorService.execute(new Runnable() {

            @Override
            public void run() {
                httpDownloadFile(url, filePath, progress, null);
            }
        });
    }

    /**
     *
     *
     * @param url
     * @param filePath
     */
    private void httpDownloadFile(String url, String filePath,
                                  HttpClientDownLoadProgress progress, Map<String, String> headMap) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            setGetHead(httpGet, headMap);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity httpEntity = response1.getEntity();
                long contentLength = httpEntity.getContentLength();
                InputStream is = httpEntity.getContent();
               
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int r = 0;
                long totalRead = 0;
                while ((r = is.read(buffer)) > 0) {
                    output.write(buffer, 0, r);
                    totalRead += r;
                    if (progress != null) {
                        progress.onProgress((int) (totalRead * 100 / contentLength));
                    }
                }
                FileOutputStream fos = new FileOutputStream(filePath);
                output.writeTo(fos);
                output.flush();
                output.close();
                fos.close();
                EntityUtils.consume(httpEntity);
            } finally {
                response1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     *
     * @param url
     * @return
     */
    public String httpGet(String url) {
        return httpGet(url, null);
    }

    /**
     * http get
     *
     * @param url
     * @return
     */
    public String httpGet(String url, Map<String, String> headMap) {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            setGetHead(httpGet, headMap);
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity = response1.getEntity();
                responseContent = getRespString(entity);
                System.out.println("debug:" + responseContent);
                EntityUtils.consume(entity);
            } finally {
                response1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    public String httpPost(String url, Map<String, String> paramsMap) {
        return httpPost(url, paramsMap, null);
    }



    /**
     * 
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public String httpPost(String url, Map<String, String> paramsMap,
                           Map<String, String> headMap) {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            setPostHead(httpPost, headMap);
            setPostParams(httpPost, paramsMap);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                System.out.println(response.getStatusLine());
                HttpEntity entity = response.getEntity();
                responseContent = getRespString(entity);
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("responseContent = " + responseContent);
        return responseContent;
    }

    /**
     *
     *
     * @param httpPost
     * @param headMap
     */
    private void setPostHead(HttpPost httpPost, Map<String, String> headMap) {
        if (headMap != null && headMap.size() > 0) {
            Set<String> keySet = headMap.keySet();
            for (String key : keySet) {
                httpPost.addHeader(key, headMap.get(key));
            }
        }
    }

    /**
     * 
     *
     * @param httpGet
     * @param headMap
     */
    private void setGetHead(HttpGet httpGet, Map<String, String> headMap) {
        if (headMap != null && headMap.size() > 0) {
            Set<String> keySet = headMap.keySet();
            for (String key : keySet) {
                httpGet.addHeader(key, headMap.get(key));
            }
        }
    }

    /**
     *
     *
     * @param serverUrl
     *  ַ
     * @param localFilePath
     *          
     * @param serverFieldName
     * @param params
     * @return
     * @throws Exception
     */
    public String uploadFileImpl(String serverUrl, String localFilePath,String serverFieldName, Map<String, String> params, Map<String, String> paramshead)
            throws Exception {
        String respStr = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(serverUrl);
          
          // httppost.setHeader("Cookie", "x-token=Xb9XTOjIQJa-6kyV0T4KAl1QUygkW0UgzZByx9ojDbK5WpQ-5IZ5pw");
          // httppost.setHeader("accept", "*/*");  
          // httppost.setHeader("connection", "Keep-Alive");  
          // httppost.setHeader("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
           
          // httppost.setHeader("Content-Type", "charset=utf-8");
          //conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            
            setPostHead(httppost, paramshead);
            
            FileBody binFileBody = new FileBody(new File(localFilePath));

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // add the file params
            
            multipartEntityBuilder.addPart(serverFieldName, binFileBody);
        
            setUploadParams(multipartEntityBuilder, params);

            HttpEntity reqEntity = multipartEntityBuilder.build();
            httppost.setEntity(reqEntity);

            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                respStr = getRespString(resEntity);
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        System.out.println("resp=" + respStr);
        return respStr;
    }

    /**
     * 
     *
     * @param multipartEntityBuilder
     * @param params
     */
    private void setUploadParams(MultipartEntityBuilder multipartEntityBuilder,
                                 Map<String, String> params) {
        if (params != null && params.size() > 0) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                multipartEntityBuilder.addPart(key, new StringBody(params.get(key),ContentType.APPLICATION_JSON));
                
            }
        }
    }

    /**
     * String
     *
     * @param entity
     * @return
     * @throws Exception
     */
    private String getRespString(HttpEntity entity) throws Exception {
        if (entity == null) {
            return null;
        }
        InputStream is = entity.getContent();
        StringBuffer strBuf = new StringBuffer();
        byte[] buffer = new byte[4096];
        int r = 0;
        while ((r = is.read(buffer)) > 0) {
            strBuf.append(new String(buffer, 0, r, "UTF-8"));
        }
        return strBuf.toString();
    }
    /**
     * 
     *
     * @param httpPost
     * @param paramsMap
     * @throws Exception
   */
    private void setPostParams(HttpPost httpPost, Map<String, String> paramsMap)
            throws Exception {
        if (paramsMap != null && paramsMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<String> keySet = paramsMap.keySet();
            for (String key : keySet) {
                nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        }
    }  
    
    public static String sendPost2(String url, String token, String paramsStr) throws IOException, Exception {
    	
    	   //POST请求
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //请求参数
        StringEntity postingString = new StringEntity(paramsStr, "utf-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(postingString);

        //请求头设置
        httpPost.addHeader("Cookie", "x-token=" + token);
        httpPost.addHeader("accept", "*/*");
        httpPost.addHeader("connection", "Keep-Alive");
        httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
        httpPost.addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

        //执行请求
        CloseableHttpResponse execute = httpclient.execute(httpPost);

        //处理返回结果
        String restStr = IOUtils.toString(execute.getEntity().getContent(), "utf-8");
   
        return restStr;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            //setPostHead(realUrl, headMap);
            
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println(" POST"+e);
            e.printStackTrace();
        }
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        return result;
    } 
    
//
//    public static void main(String[] args) throws IOException, Exception {
//        String token = "";
//
//        //第一步：单点登入
//        String path = "http://172.16.99.20:20020/x_organization_assemble_authentication/jaxrs/sso";
//        long time = new Date().getTime();
//        String login_uid = "test";
//        String sso_key = "12345678";
//        String xtoken = null;
//
//		try {
//			xtoken = Crypto.encrypt( login_uid + "#" + time, sso_key );
//			System.out.println(xtoken);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//
//        String string = "{\"token\": "+xtoken+", \"client\": \"unicom\"}";
//        String  str =  HttpClientUtils.getInstance().sendPost(path,string);
//        try {
//            JSONObject jsonObj = (JSONObject)(new JSONParser().parse(str));
//           // System.out.println(jsonObj.toJSONString() + "\n" + jsonObj.getClass());
//            JSONObject data = (JSONObject) jsonObj.get("data");
//            System.out.println(data.get("token"));
//            token = (String) data.get("token");
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//		//token = "HeEoZIVgPjQ6Yg_9vdfLipw0-_pO0Z6MOqWW7Hfryq9U_mplRNTZYQ";
//
//         //创建流程
//        path = "http://172.16.99.20:20020/x_processplatform_assemble_surface/jaxrs/work/process/2d99204b-a821-41e5-9ff6-23f13cc0138b";
//        JSONObject result = new JSONObject();
//
//        result.put("latest", false);
//        result.put("title", "演示系统2020-06-24测试(二)");
//        result.put("identity", "test@4697391e-b7d7-4a07-b17e-f40f650cf29d@I");
//
//        JSONObject data = new JSONObject();
//        data.put("subject", "演示系统2020-06-24测试（二）");
//        data.put("calendar","2020-04-13 12:00:00");
//        data.put("textfield_1","测试2020-06-24");
//        result.put("data", data);
//
//     	 String strflow =  HttpClientUtils.getInstance().sendPost2(path,token,result.toJSONString());
//     	 String work = "";
//
//	     //获取返回的workid
//	     JsonParser parser = new JsonParser();
//	     JsonObject object = null;
//	     object =(JsonObject) parser.parse(strflow);
//	     JsonArray value =object.getAsJsonArray("data");
//	     object = value.get(0).getAsJsonObject();
//	     work= object.get("work").getAsString();
//
//
//	     System.out.println("work=" + work);
//
//		//上传附件
//
//		try {
//		    Map<String,String> uploadParams = new LinkedHashMap<String, String>();
//		    uploadParams.put("fileName", "云化统一信息平台二期演示视频解说词0515v3.docx");
//		    uploadParams.put("site", "attachment");
//		    uploadParams.put("extraParam","");
//		    Map<String, String> headMap = new HashMap<String, String>();
//		    headMap.put("Cookie", "x-token=" + token);
//		    headMap.put("accept", "*/*");
//		    headMap.put("connection", "Keep-Alive");
//		    headMap.put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//
//		    String strurl = "http://172.16.99.20:20020/x_processplatform_assemble_surface/jaxrs/attachment/upload/work/" + work;
//
//		    HttpClientUtils.getInstance().uploadFileImpl(
//		    		strurl, "F:\\教程总结\\mootools详细教程.doc",
//		            "file", uploadParams, headMap);
//		   } catch (Exception e) {
//		    e.printStackTrace();
//		}
//
//
//
//        /* 通过接口创建
//        path = "http://127.0.0.1:20030/x_program_center/jaxrs/invoke/creatework/execute";
//     	JSONObject result = new JSONObject();
//        result.put("latest", false);
//        result.put("title", "演示系统2019-10-19wwww");
//        result.put("identity", "test@a2b7b360-2685-4989-ae70-77a2f207c6d7@I");
//
//        JSONObject department = new JSONObject();
//        department.put("subject", "测试20191018java启动wwww");
//        result.put("data", department);
//
//     	String strflow =  HttpClientUtils.getInstance().sendPost2(path,token,result.toJSONString());
//
//    	 String work = "";
//		 try {
//		     JSONObject jsonObj = (JSONObject)(new JSONParser().parse(strflow));
//		     JSONObject data = (JSONObject) jsonObj.get("data");
//		     JSONObject value = (JSONObject) data.get("value");
//		                data = (JSONObject)value.get("data");
//		     JSONObject data0 = (JSONObject)data.get("0");
//		                work = (String)data0.get("work");
//		     System.out.println(work);
//		 } catch (ParseException e) {
//		     e.printStackTrace();
//		 }
//	     */
//
//    }

    public static String sendGet(String url, String token) throws IOException, Exception {

        //GET请求
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //请求参数
        //StringEntity postingString = new StringEntity(paramsStr, "utf-8");
        //HttpPost httpPost = new HttpPost(url);
        //httpGet.setEntity(postingString);
        HttpGet httpGet = new HttpGet(url);
        //请求头设置
        httpGet.addHeader("Cookie", "x-token=" + token);
        httpGet.addHeader("accept", "*/*");
        httpGet.addHeader("connection", "Keep-Alive");
        httpGet.addHeader("Content-Type", "application/json; charset=utf-8");
        httpGet.addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

        //执行请求
        CloseableHttpResponse execute = httpclient.execute(httpGet);

        //处理返回结果
        String restStr = IOUtils.toString(execute.getEntity().getContent(), "utf-8");

        return restStr;
    }
}
