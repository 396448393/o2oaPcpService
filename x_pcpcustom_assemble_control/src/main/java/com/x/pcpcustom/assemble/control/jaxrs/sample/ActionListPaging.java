package com.x.pcpcustom.assemble.control.jaxrs.sample;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.pcpcustom.core.entity.SampleEntityClassName;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.List;

class ActionListPaging extends BaseAction {
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, Integer page, Integer size) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			EntityManager em = emc.get(SampleEntityClassName.class);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			Predicate p = cb.conjunction();
			List<Wo> wos = emc.fetchDescPaging(SampleEntityClassName.class, Wo.copier, p, page, size, SampleEntityClassName.sequence_FIELDNAME);
			result.setData(wos);
			result.setCount(emc.count(SampleEntityClassName.class, p));
			return result;
		}
	}

	public static class Wo extends SampleEntityClassName {

		private static final long serialVersionUID = -4635222902589827154L;

		static WrapCopier<SampleEntityClassName, Wo> copier = WrapCopierFactory.wo(SampleEntityClassName.class, Wo.class,
				JpaObject.singularAttributeField(SampleEntityClassName.class, true, true), null);

	}
}
