package com.regexextractors.deliver;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DeliverActivator implements BundleActivator {

	ServiceRegistration deliveryService;

	public void start(BundleContext context) throws Exception {
		
		System.out.println("Delivery service started");
		DeliveryPublish delivery = new DeliveryPublishImpl();
		deliveryService = context.registerService(DeliveryPublish.class.getName(), delivery, null);
		
	}

	public void stop(BundleContext context) throws Exception {
		
		System.out.println("Delivery service stopped");
		deliveryService.unregister();
		
	}

}
