package com.regexextractors.store;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class StoreActivator implements BundleActivator {
	
	ServiceRegistration storeService;

	public void start(BundleContext context) throws Exception {
		
		System.out.println("Store service started");
		StorePublish storePublisher = new StorePublishImpl();
		storeService = context.registerService(StorePublish.class.getName(), storePublisher, null);
		
	}

	public void stop(BundleContext context) throws Exception {
		
		System.out.println("Store service stopped");
		storeService.unregister();
		
	}

}
