package com.x.pcpcustom.assemble.control.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.pcpcustom.assemble.control.action.entity.UploadFileParamEntity;
import com.x.pcpcustom.assemble.control.action.entity.UploadFileReturnEntity;
import com.x.pcpcustom.assemble.control.jaxrs.sample.BaseAction;
import com.x.pcpcustom.assemble.control.service.ProcessService;
import com.x.pcpcustom.core.entity.SampleEntityClassName;
import org.bouncycastle.operator.MacCalculatorProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 附件上传
 */
public class ActionUploadProcessFiles extends BaseAction {

	private static  Logger logger = LoggerFactory.getLogger( ActionUploadProcessFiles.class );

	public ActionResult<Wo> execute(HttpServletRequest request, EffectivePerson effectivePerson, JsonObject jsonObject) throws Exception {
		ActionResult<Wo> result = new ActionResult<>();
		//Wi wi = this.convertToWrapIn( jsonElement, Wi.class );

		String workId= jsonObject.get("workId").getAsString();
		String loginName= jsonObject.get("loginName").getAsString();
		String xtoken= jsonObject.get("xtoken").getAsString();
		JsonArray fileList= jsonObject.get("fileList").getAsJsonArray();
		ProcessService processService=new ProcessService();
		//上传附件
		UploadFileReturnEntity [] retArray=new UploadFileReturnEntity[fileList.size()];
		for(int i=0;i<fileList.size();i++){
			JsonObject file = fileList.get(i).getAsJsonObject();
			retArray[i]=processService.uploadFile(xtoken,workId,loginName,file);
		}

		Wo wo = new Wo(result.getType().toString(),retArray);
		result.setData(wo);
		return result;
	}

	/**
	 * 用于接受前端传入的对象型参数的帮助类
	 *
	 */
	public static class Wi extends UploadFileParamEntity{

		public static WrapCopier<Wi, UploadFileParamEntity> copier = WrapCopierFactory.wi( Wi.class, UploadFileParamEntity.class, null, JpaObject.FieldsUnmodify );

	}

	/**
	 * 用于输出响应内容的帮助类
	 *
	 */
	public static class Wo extends WoId {
		public Wo( String type,UploadFileReturnEntity[] retArr ) {
			this.setType(type);
			this.setMessage(retArr);
		}
		String type;
		UploadFileReturnEntity[] message;

		public void setType(String type) {
			this.type = type;
		}

		public void setMessage(UploadFileReturnEntity[] message) {
			this.message = message;
		}
	}
}
