package com.x.pcpcustom.assemble.control.action;

import com.google.gson.JsonObject;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.pcpcustom.assemble.control.action.entity.LoginReturnEntity;
import com.x.pcpcustom.assemble.control.jaxrs.sample.BaseAction;
import com.x.pcpcustom.assemble.control.service.ProcessService;
import com.x.pcpcustom.assemble.control.action.entity.LoginParamEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户登陆
 */
public class ActionLogin extends BaseAction {

	private static  Logger logger = LoggerFactory.getLogger( ActionLogin.class );

	public ActionResult<Wo> execute(HttpServletRequest request, EffectivePerson effectivePerson, JsonObject jsonObject) throws Exception {
		ActionResult<Wo> result = new ActionResult<>();

		String user = jsonObject.get("loginUser").getAsString();
		String passWord = jsonObject.get("passWord").getAsString();

		ProcessService processService=new ProcessService();
		//获取xtoken
		LoginReturnEntity retData=processService.getTockenToLogin(user,passWord);

		Wo wo = new Wo(retData);
		result.setData(wo);
		return result;
	}

	/**
	 * 用于接受前端传入的对象型参数的帮助类
	 *
	 */
	public static class Wi extends LoginParamEntity{

		public static WrapCopier<Wi, LoginParamEntity> copier = WrapCopierFactory.wi( Wi.class, LoginParamEntity.class, null, JpaObject.FieldsUnmodify );

	}

	/**
	 * 用于输出响应内容的帮助类
	 *
	 */
	public static class Wo extends WoId {

		public Wo( LoginReturnEntity retData ) {
			this.setXtoken(retData.getXtoken());
			this.setMessage(retData.getMessage());
		}

		private String xtoken;
		private String message;

		public String getXtoken() {
			return xtoken;
		}

		public void setXtoken(String xtoken) {
			this.xtoken = xtoken;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
