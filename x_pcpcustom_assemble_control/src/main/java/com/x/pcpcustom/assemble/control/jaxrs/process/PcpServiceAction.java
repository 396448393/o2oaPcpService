package com.x.pcpcustom.assemble.control.jaxrs.process;


import com.google.gson.JsonElement;
import com.x.base.core.project.annotation.JaxrsDescribe;
import com.x.base.core.project.annotation.JaxrsMethodDescribe;
import com.x.base.core.project.annotation.JaxrsParameterDescribe;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.http.HttpMediaType;
import com.x.base.core.project.jaxrs.ResponseFactory;
import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.tools.Crypto;
import com.x.pcpcustom.assemble.control.action.ActionCreateProcess;
import com.x.pcpcustom.assemble.control.action.ActionLogin;
import com.x.pcpcustom.assemble.control.action.ActionUploadProcessFiles;
import com.x.pcpcustom.assemble.control.jaxrs.sample.*;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Path("process/pcp")
@JaxrsDescribe("PCP平台外部调用服务")
public class PcpServiceAction extends StandardJaxrsAction{

	private static Logger logger = LoggerFactory.getLogger( PcpServiceAction.class );

//	@JaxrsMethodDescribe( value = "根据用户名获取token", action = ActionLogin.class )
//	@POST
//	@Path("server/login")
//	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public void loginWithUser(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
//							  @JaxrsParameterDescribe("登陆信息") JsonElement jsonElement) {
//		//logger.debug("参数数据：",jsonElement);
//
//		ActionResult<ActionLogin.Wo> result = new ActionResult<>();
//		EffectivePerson effectivePerson = this.effectivePerson(request);
//
//		try {
////			Map<String,String> paramMap= new HashMap<>();
////			paramMap.put("userName",loginUser);
////			paramMap.put("passWord",passWord);
//			//logger.info("获取token!");
//			result = new ActionLogin().execute( request, effectivePerson,jsonElement.getAsJsonObject());
//			//String ret = "获取token！";
//
//			result.setMessage(result.getType().toString());
//		} catch (Exception e) {
//			logger.error(e, effectivePerson, request, null);
//			result.error(e);
//		}
//		//result.setMessage(jsonElement.toString());
//		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
//	}

	@JaxrsMethodDescribe( value = "根据流程名称启动流程接口", action = ActionCreateProcess.class )
	@POST
	@Path("process/create")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void createProcess(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
					@JaxrsParameterDescribe("流程启动传入信息") JsonElement jsonElement) {
		ActionResult<ActionCreateProcess.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionCreateProcess().execute( request, effectivePerson, jsonElement.getAsJsonObject() );

			//result.setMessage(result.getType().toString());
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}
//
	@JaxrsMethodDescribe( value = "上传表单附件接口", action = ActionUploadProcessFiles.class )
	@POST
	@Path("attachment/update")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
			@JaxrsParameterDescribe("附件信息") JsonElement jsonElement ) {
		ActionResult<ActionUploadProcessFiles.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionUploadProcessFiles().execute( request, effectivePerson, jsonElement.getAsJsonObject() );
//			result.setMessage(ret);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}


}
