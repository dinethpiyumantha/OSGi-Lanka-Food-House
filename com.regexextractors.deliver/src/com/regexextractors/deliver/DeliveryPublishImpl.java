package com.regexextractors.deliver;

import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DeliveryPublishImpl implements DeliveryPublish {
	
	private Scanner scanner = new Scanner(System.in);
	
	private static final String FILE_PATH = "D:\\BSc. Information Technology (sp.) - SLIIT\\3rd Year 2nd Semester\\Assesments\\SA\\Assignment 1\\OSGi-plugin-project\\com.regexextractors.deliver\\src\\com\\regexextractors\\deliver\\delivery-data.xml";
	private File deliveryFile = new File(FILE_PATH);
	private Element name, orderNo, contact, address;
	
	/**
	 * Deliver
	 * @return void
	 */
	@Override
	public void deliver(String orderId) {
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document deliveryDocument = documentBuilder.newDocument();
			Element informationElement = deliveryDocument.createElement("information");
			Element rootElement = deliveryDocument.createElement("delivery");
			deliveryDocument.appendChild(rootElement);
			
			DOMSource source = new DOMSource(deliveryDocument);
			StreamResult result = new StreamResult(deliveryFile);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			System.out.println("\nDelivery Information Form ===");
			System.out.print("Order ID - " + orderId);
			orderNo = deliveryDocument.createElement("orderid");
			orderNo.setTextContent(orderId);
			
			System.out.print("Name: ");
			name = deliveryDocument.createElement("name");
			name.setTextContent(scanner.next());
			
			System.out.print("Phone: ");
			contact = deliveryDocument.createElement("contact");
			contact.setTextContent(scanner.next());
			
			System.out.print("Address: ");
			address = deliveryDocument.createElement("address");
			address.setTextContent(scanner.next());
			
			informationElement.appendChild(orderNo);
			informationElement.appendChild(name);
			informationElement.appendChild(contact);
			informationElement.appendChild(address);
			
			rootElement.appendChild(informationElement);
			
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			
			System.out.println("****** THANK YOU ********\nYour order is on the way!\n*************************\n");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
