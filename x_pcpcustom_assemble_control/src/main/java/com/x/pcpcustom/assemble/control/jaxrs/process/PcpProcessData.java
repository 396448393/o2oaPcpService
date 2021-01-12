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
import com.x.pcpcustom.assemble.control.action.ActionUpdataProcessData;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("process/data")
@JaxrsDescribe("PCP平台流程数据操作接口")
public class PcpProcessData extends StandardJaxrsAction {
    private static Logger logger = LoggerFactory.getLogger( PcpServiceAction.class );

	@JaxrsMethodDescribe( value = "数据更新接口", action = ActionUpdataProcessData.class )
	@POST
	@Path("updata/data")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void updataProcessData(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
                                  @JaxrsParameterDescribe("参数信息") JsonElement jsonElement ) {
		ActionResult<ActionUpdataProcessData.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);
		try {
			result = new ActionUpdataProcessData().execute( request, effectivePerson, jsonElement.getAsJsonObject() );
//			result.setMessage(ret);
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}
}
