package com.regexextractors.login;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class LoginActivator implements BundleActivator {

	ServiceRegistration loginService;

	public void start(BundleContext context) throws Exception {
		
		System.out.println("Login service started");
		LoginServicePublish loginServicePublish = new LoginServicePublishImpl();
		loginService = context.registerService(LoginServicePublish.class.getName(), loginServicePublish, null);
		
	}

	public void stop(BundleContext context) throws Exception {
		
		System.out.println("Login service stopped");
		loginService.unregister();
		
	}

}
