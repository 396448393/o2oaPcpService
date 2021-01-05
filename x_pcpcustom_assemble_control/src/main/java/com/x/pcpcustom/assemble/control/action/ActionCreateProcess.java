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
import com.x.pcpcustom.assemble.control.action.entity.CreateProcessParamEntity;
import com.x.pcpcustom.assemble.control.action.entity.CreateProcessReturnEntity;
import com.x.pcpcustom.assemble.control.jaxrs.sample.BaseAction;
import com.x.pcpcustom.assemble.control.service.ProcessService;


import javax.servlet.http.HttpServletRequest;

/**
 * 创建流程
 */
public class ActionCreateProcess extends BaseAction {

	private static  Logger logger = LoggerFactory.getLogger( ActionCreateProcess.class );

	public ActionResult<Wo> execute(HttpServletRequest request, EffectivePerson effectivePerson, JsonObject jsonObject) throws Exception {
		ActionResult<Wo> result = new ActionResult<>();
//		Wi wi = this.convertToWrapIn( jsonElement, Wi.class );

		String processName= jsonObject.get("processName").getAsString();
		String title= jsonObject.get("title").getAsString();
		String xtoken= jsonObject.get("xtoken").getAsString();
		String startProcessPerson= jsonObject.get("startProcessPerson").getAsString();
		JsonObject dataJson= jsonObject.get("data").getAsJsonObject();
		ProcessService processService=new ProcessService();
		//创建流程
		CreateProcessReturnEntity retData=processService.createProcess(processName,title,xtoken,startProcessPerson,dataJson);

		Wo wo = new Wo(retData);
		result.setData(wo);
		return result;
	}

	/**
	 * 用于接受前端传入的对象型参数的帮助类
	 *
	 */
	public static class Wi extends CreateProcessParamEntity {

		public static WrapCopier<Wi, CreateProcessParamEntity> copier = WrapCopierFactory.wi( Wi.class, CreateProcessParamEntity.class, null, JpaObject.FieldsUnmodify );

	}

	/**
	 * 用于输出响应内容的帮助类
	 *
	 */
	public static class Wo extends WoId {
		public Wo( CreateProcessReturnEntity ret ) {
			this.setMessage(ret.getMessage());
			this.setSerialNumber(ret.getSerialNumber());
			this.setWorkId(ret.getWorkId());
		}
		private String message;
		private String workId;//工作id
		private String serialNumber;//流程单号

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getWorkId() {
			return workId;
		}

		public void setWorkId(String workId) {
			this.workId = workId;
		}

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}
	}
}
