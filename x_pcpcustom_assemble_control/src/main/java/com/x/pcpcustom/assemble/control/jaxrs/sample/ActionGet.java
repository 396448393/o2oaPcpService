package com.x.pcpcustom.assemble.control.jaxrs.sample;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.organization.Identity;
import com.x.base.core.project.organization.Person;
import com.x.base.core.project.organization.Unit;
import com.x.pcpcustom.assemble.control.Business;
import com.x.pcpcustom.core.entity.SampleEntityClassName;

public class ActionGet extends BaseAction {

	private Logger logger = LoggerFactory.getLogger( ActionGet.class );

	protected ActionResult<Wo> execute( HttpServletRequest request, EffectivePerson effectivePerson, String id ) throws Exception {
		logger.info("execute action 'ActionGet'......");
		ActionResult<Wo> result = new ActionResult<>();
		Wo wrap = null;
		Business business;
		//与数据库交互示例, 根据ID查询单条记录
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			business = new Business(emc);
			//获取当前登录用户
			String curUser = effectivePerson.getDistinguishedName();
			//查询用户对象
			Person person = business.organization().person().getObject(curUser);
			//获取用户主身份对象
			Identity identity = business.organization().identity().getMajorWithPersonObject(curUser);
			//获取身份归属组织对象
			Unit unit = business.organization().unit().getObject(identity.getUnit());

			SampleEntityClassName sampleEntityClassName = emc.find( id, SampleEntityClassName.class );
			if( sampleEntityClassName != null ){
				wrap = Wo.copier.copy( sampleEntityClassName );
				result.setCount(1L);
				result.setData( wrap );
			}else {
				Exception exception = new ExceptionSampleEntityClassNameNotExists( id );
				result.error( exception );
			}
		} catch (Exception e) {
			Exception exception = new ExceptionSampleEntityClassFind( e, "系统在根据ID查询指定示例数据记录时发生异常！ID=" + id );
			result.error( exception );
			logger.error(e);
		}

		logger.info("action 'ActionGet' execute completed!");
		return result;
	}

	public static class Wo extends SampleEntityClassName  {

		private static final long serialVersionUID = -5076990764713538973L;

		public static List<String> Excludes = new ArrayList<String>();

		public static WrapCopier<SampleEntityClassName, Wo> copier = WrapCopierFactory.wo( SampleEntityClassName.class, Wo.class, null, JpaObject.FieldsInvisible);
	}

}
