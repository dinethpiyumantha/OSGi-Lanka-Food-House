package com.regexextractors.cart;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface CartPublish {

	/**
	 * Display products in the cart.
	 * @return void
	 */
	void displayCart();

	/**
	 * Store selected products in 'cart-data.xml'
	 * @param rootElement 
	 * @param document
	 * @param name
	 * @param price
	 * @param qty
	 * @return Document
	 */
	Document addToCart(Element rootElement, Document document, String name, double price, int qty);

	/**
	 * Store cart details in 'cart-data.xml'
	 * @param document
	 */
	void storeCartInFile(Document document);

	/**
	 * Navigate to the Product Store
	 * @return void
	 */
	void navigateToProducts();

	/**
	 * Process the payment
	 * @param amount - Amount to process payment
	 */
	void navigateToPayment(double amount);

}
