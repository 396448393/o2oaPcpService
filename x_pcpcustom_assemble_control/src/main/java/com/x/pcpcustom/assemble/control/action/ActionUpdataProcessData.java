package com.x.pcpcustom.assemble.control.action;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.pcpcustom.assemble.control.entity.UpdataProcessDataParamEntity;
import com.x.pcpcustom.assemble.control.entity.UpdataProcessDataReturnEntity;
import com.x.pcpcustom.assemble.control.jaxrs.sample.BaseAction;
import com.x.pcpcustom.assemble.control.service.ProcessService;

import javax.servlet.http.HttpServletRequest;

/**
 * 创建流程
 */
public class ActionUpdataProcessData extends BaseAction {

	private static  Logger logger = LoggerFactory.getLogger( ActionUpdataProcessData.class );

	public ActionResult<Wo> execute(HttpServletRequest request, EffectivePerson effectivePerson, JsonObject jsonObject) throws Exception {
		ActionResult<Wo> result = new ActionResult<>();

		String workId= jsonObject.get("workId").getAsString();
		String dataId= jsonObject.get("dataId").getAsString();
		String dataValue= jsonObject.get("dataValue").getAsString();
		String xtoken= jsonObject.get("xtoken").getAsString();

		ProcessService processService=new ProcessService();
		//创建流程
		UpdataProcessDataReturnEntity retData=processService.upProcessData(workId,dataId,dataValue,xtoken);

		Wo wo = new Wo(retData);
		result.setData(wo);
		return result;
	}

	/**
	 * 用于接受前端传入的对象型参数的帮助类
	 *
	 */
	public static class Wi extends UpdataProcessDataParamEntity {

		public static WrapCopier<Wi, UpdataProcessDataParamEntity> copier = WrapCopierFactory.wi( Wi.class, UpdataProcessDataParamEntity.class, null, JpaObject.FieldsUnmodify );

	}

	/**
	 * 用于输出响应内容的帮助类
	 *
	 */
	public static class Wo extends WoId {
		public Wo( UpdataProcessDataReturnEntity ret ) {
			this.setMessage(ret.getResultData());
			this.setType(ret.getResultState());
		}
		private String type;
		private JSONObject message;

		public void setType(String type) {
			this.type = type;
		}

		public void setMessage(JSONObject message) {
			this.message = message;
		}
	}
}
