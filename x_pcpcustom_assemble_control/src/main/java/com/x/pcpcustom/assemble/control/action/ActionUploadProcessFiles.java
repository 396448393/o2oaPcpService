package com.x.pcpcustom.assemble.control.action;

import com.google.gson.JsonElement;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.pcpcustom.assemble.control.jaxrs.sample.BaseAction;
import com.x.pcpcustom.assemble.control.service.ProcessService;
import com.x.pcpcustom.core.entity.SampleEntityClassName;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;

/**
 * 示例数据信息保存服务
 */
public class ActionUploadProcessFiles extends BaseAction {

	private static  Logger logger = LoggerFactory.getLogger( ActionUploadProcessFiles.class );

	public ActionResult<Wo> execute(HttpServletRequest request, EffectivePerson effectivePerson, JsonElement jsonElement) throws Exception {
		ActionResult<Wo> result = new ActionResult<>();
		Wi wi = this.convertToWrapIn( jsonElement, Wi.class );

		JsonArray fileList= (JsonArray) wi.get("fileList");
		String workId= (String) wi.get("workId");
		String xtoken= (String) wi.get("xtoken");
		String loginName= (String) wi.get("loginName");

		ProcessService processService=new ProcessService();
		//获取xtoken
//		String retStr=processService.uploadFile(xtoken,workId,fileName);
		String retStr="无数据";
//		if( StringUtils.isEmpty( wi.getName() )) {
//			throw new ExceptionSampleEntityClassNameEmpty();
//		}
//		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
//			SampleEntityClassName sampleEntityClassName = Wi.copier.copy( wi );
//			//启动事务
//			emc.beginTransaction( SampleEntityClassName.class );
//			//校验对象
//			emc.check( sampleEntityClassName, CheckPersistType.all );
//			//提交事务
//			emc.commit();
//
//			Wo wo = new Wo(sampleEntityClassName.getId());
//			result.setData(wo);
//		}
		logger.info("流程返回数据：",retStr);
		Wo wo = new Wo(retStr);
		result.setData(wo);
		return result;
	}

	/**
	 * 用于接受前端传入的对象型参数的帮助类
	 *
	 */
	public static class Wi extends SampleEntityClassName{

		public static WrapCopier<Wi, SampleEntityClassName> copier = WrapCopierFactory.wi( Wi.class, SampleEntityClassName.class, null, JpaObject.FieldsUnmodify );

	}

	/**
	 * 用于输出响应内容的帮助类
	 *
	 */
	public static class Wo extends WoId {
		public Wo( String id ) {
			setId( id );
		}
	}
}
