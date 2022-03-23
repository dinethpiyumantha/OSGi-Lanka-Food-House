package com.regexextractors.payment;

public interface PaymentService {

	/**
	 * Navigate to the Delivery service
	 * @return void
	 */
	void navigateToDelivery();

	/**
	 * Make a payment with any type method
	 * @param amount - Payment Amount
	 */
	void makePayment(double amount);

}
