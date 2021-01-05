package com.x.pcpcustom.assemble.control.jaxrs;

import java.util.Set;

import javax.ws.rs.ApplicationPath;

import com.x.base.core.project.jaxrs.AbstractActionApplication;
import com.x.pcpcustom.assemble.control.jaxrs.process.PcpLoginWithUserAndPassd;
import com.x.pcpcustom.assemble.control.jaxrs.process.PcpServiceAction;
import com.x.pcpcustom.assemble.control.jaxrs.sample.SampleEntityClassNameAction;

/**
 * Jaxrs服务注册类，在此类中注册的Action会向外提供服务
 */
@ApplicationPath("jaxrs")
public class ActionApplication extends AbstractActionApplication {

	public Set<Class<?>> getClasses() {
		
		//提供服务的Action类需要在这里注册，不然无法向外提供服务
//		this.classes.add( SampleEntityClassNameAction.class);
		this.classes.add( PcpServiceAction.class);
		this.classes.add( PcpLoginWithUserAndPassd.class);

		return this.classes;
	}

}