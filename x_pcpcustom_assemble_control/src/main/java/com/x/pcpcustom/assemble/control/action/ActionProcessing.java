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
import com.x.pcpcustom.assemble.control.entity.ProcessingParamEntity;
import com.x.pcpcustom.assemble.control.entity.ProcessingReturnEntity;
import com.x.pcpcustom.assemble.control.jaxrs.sample.BaseAction;
import com.x.pcpcustom.assemble.control.service.ProcessService;

import javax.servlet.http.HttpServletRequest;

/**
 * 创建流程
 */
public class ActionProcessing extends BaseAction {

	private static  Logger logger = LoggerFactory.getLogger( ActionProcessing.class );

	public ActionResult<Wo> execute(HttpServletRequest request, EffectivePerson effectivePerson, JsonObject jsonObject) throws Exception {
		ActionResult<Wo> result = new ActionResult<>();

		String routeName= jsonObject.get("routeName").getAsString();
		String workId= jsonObject.get("workId").getAsString();
		String xtoken= jsonObject.get("xtoken").getAsString();
		String opinion= jsonObject.get("opinion").getAsString();
		ProcessService processService=new ProcessService();
		//流转流程
		ProcessingReturnEntity retData=processService.processing(workId,routeName,opinion,xtoken);

		Wo wo = new Wo(retData);
		result.setData(wo);
		return result;
	}

	/**
	 * 用于接受前端传入的对象型参数的帮助类
	 *
	 */
	public static class Wi extends ProcessingParamEntity {

		public static WrapCopier<Wi, ProcessingParamEntity> copier = WrapCopierFactory.wi( Wi.class, ProcessingParamEntity.class, null, JpaObject.FieldsUnmodify );

	}

	/**
	 * 用于输出响应内容的帮助类
	 *
	 */
	public static class Wo extends WoId {
		public Wo( ProcessingReturnEntity ret ) {
			this.setType(ret.getResultState());
		}
		private String type;

		public void setType(String type) {
			this.type = type;
		}

	}
}
