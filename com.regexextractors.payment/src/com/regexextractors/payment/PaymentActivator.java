package com.regexextractors.payment;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class PaymentActivator implements BundleActivator {
	
	ServiceRegistration paymentService;

	public void start(BundleContext context) throws Exception {
		
		System.out.println("Payment service started");
		PaymentService payment = new PaymentServiceImpl();
		paymentService = context.registerService(PaymentService.class.getName(), payment, null);
		
	}

	public void stop(BundleContext context) throws Exception {
		
		System.out.println("Payment service stopped");
		paymentService.unregister();
		
	}

}
