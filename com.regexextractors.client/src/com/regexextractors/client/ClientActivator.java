package com.regexextractors.client;

import com.regexextractors.login.LoginServicePublish;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

public class ClientActivator implements BundleActivator {
	
	ServiceReference loginService;

	public void start(BundleContext context) throws Exception {
		
		System.out.println("Client service started");
		loginService = context.getServiceReference(LoginServicePublish.class.getName());
		LoginServicePublish login = (LoginServicePublish) context.getService(loginService);
		
		login.systemLogin();
		
	}

	public void stop(BundleContext context) throws Exception {
		
		System.out.println("Client service stopped");

	}
}
