package com.regexextractors.payment;

import com.regexextractors.deliver.DeliveryPublish;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PaymentServiceImpl implements PaymentService {
	
	private static final String FILE_PATH = "D:\\BSc. Information Technology (sp.) - SLIIT\\3rd Year 2nd Semester\\Assesments\\SA\\Assignment 1\\OSGi-plugin-project\\com.regexextractors.cart\\src\\com\\regexextractors\\cart\\cart-xml.xml";
	private File ordersFile = new File(FILE_PATH);
	private String paymentType = null;
	private String orderIdGenerated = null;
	
	/**
	 * Make a payment with any type method
	 * @param amount - Payment Amount
	 */
	@Override
	public void makePayment(double amount) {
		
		System.out.println("Payment Gateway ===========");
		
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document ordersDocument = documentBuilder.newDocument();
			Element rootElement = ordersDocument.createElement("payment");
			ordersDocument.appendChild(rootElement);
			
			// Generate order by current time moment
			for (int i = 0; i < 50; i++) {
		        Date now = new Date();
		        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
		        orderIdGenerated = ft.format(now);
			}
			
			Element order = ordersDocument.createElement("order");
			
			Element orderId = ordersDocument.createElement("id");
			orderId.setTextContent(orderIdGenerated);
			
			Element orderAmount = ordersDocument.createElement("amount");
			orderAmount.setTextContent(String.valueOf(amount));
			
			selectPaymentOptions(amount);				
			Element orderMethod = ordersDocument.createElement("method");
			orderAmount.setTextContent(paymentType);
			
			order.appendChild(orderId);
			order.appendChild(orderAmount);
			order.appendChild(orderMethod);
			
			rootElement.appendChild(order);
			
			DOMSource source = new DOMSource(ordersDocument);
			StreamResult result = new StreamResult(ordersFile);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			// Set indentation of the elements
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Helps to console payment options and get an input
	 * @return void
	 */
	private void selectPaymentOptions (double amount) {
		
		String cardNumber, cvv, validTill, paymentType;
		double cash;
		Scanner scanner = new Scanner(System.in);
		
		System.out.print(">>> Payment Options >>>\n(1) - Cash\\n(2) - Creadit Card\\n(3) - Debit Card\n\nEnter: ");
		
		while(true) {
			switch(scanner.nextInt()) {
			case 1:
				System.out.println("Creadit Card No: ");
				cardNumber = scanner.next();
				System.out.println("Valid Till (MM/YY): ");
				paymentType = scanner.next();
				System.out.println("CVV: ");
				cvv = scanner.next();
				
				paymentType = "Creadit Card";
				
				System.out.println("Payment LKR."+ amount +" deducted from account successfully!");
				break;
				
			case 2:
				System.out.println("Debit Card No: ");
				cardNumber = scanner.next();
				System.out.println("Valid Till (MM/YY): ");
				paymentType = scanner.next();
				System.out.println("CVV: ");
				cvv = scanner.next();
				
				paymentType = "Debit Card";
				
				System.out.println("Payment LKR."+ amount +" deducted from account successfully!");
			
			case 3:
				paymentType = "Cash";
				
				System.out.println("Order placed. Payment will complete at delivery place\nBalance: " + amount);
			
			default:
				System.out.println("\nInvalid payment input!\nPlease re-enter...\n");
				continue;
			}
		}
	}
	
	
	/**
	 * Navigate to the Delivery service
	 * @return void
	 */
	@Override
	public void navigateToDelivery () {
		
		System.out.println("Loading Delivery Services ...");
		
		// Access payment as a service
		BundleContext context = FrameworkUtil.getBundle(PaymentService.class).getBundleContext();
		ServiceReference deleveryService = context.getServiceReference(DeliveryPublish.class.getName());
		DeliveryPublish delivery = (DeliveryPublish) context.getService(deleveryService);
		
		try {
			delivery.deliver(this.orderIdGenerated);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
