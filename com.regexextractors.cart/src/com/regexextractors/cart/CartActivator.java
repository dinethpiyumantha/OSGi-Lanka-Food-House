package com.regexextractors.cart;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class CartActivator implements BundleActivator {

	ServiceRegistration cartService;

	public void start(BundleContext context) throws Exception {
		
		System.out.println("Cart service started");
		CartPublish cartPublisher = new CartPublishImpl();
		cartService = context.registerService(CartPublish.class.getName(), cartPublisher, null);
		
	}

	public void stop(BundleContext context) throws Exception {
		
		System.out.println("Cart service stoped");
		cartService.unregister();
		
	}

}
