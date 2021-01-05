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
import com.x.pcpcustom.assemble.control.action.ActionLogin;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@Path("pcpserv")
@JaxrsDescribe("PCP平台登陆服务")
public class PcpLoginWithUserAndPassd extends StandardJaxrsAction{

	private static Logger logger = LoggerFactory.getLogger( PcpLoginWithUserAndPassd.class );
//	static final String  URL_SSOLOGIN="/x_organization_assemble_authentication/jaxrs/sso";

	@JaxrsMethodDescribe( value = "根据用户名获取token", action = ActionLogin.class )
	@POST
	@Path("login/userlogin")
	@Produces(HttpMediaType.APPLICATION_JSON_UTF_8)
	@Consumes(MediaType.APPLICATION_JSON)
	public void loginWithUser(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest request,
							  @JaxrsParameterDescribe("登陆信息") JsonElement jsonElement) {
		//logger.debug("参数数据：",jsonElement);

		ActionResult<ActionLogin.Wo> result = new ActionResult<>();
		EffectivePerson effectivePerson = this.effectivePerson(request);

		try {

			result = new ActionLogin().execute( request, effectivePerson,jsonElement.getAsJsonObject());

			//result.setMessage(result.getType().toString());
		} catch (Exception e) {
			logger.error(e, effectivePerson, request, null);
			result.error(e);
		}
		asyncResponse.resume(ResponseFactory.getEntityTagActionResultResponse(request, result));
	}

}
