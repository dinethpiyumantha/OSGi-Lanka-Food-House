package com.regexextractors.cart;

import java.io.File;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.regexextractors.payment.PaymentService;
import com.regexextractors.store.StorePublish;


/**
 * This class is implemented for create cart service
 * @author RegEx Extractors
 */
public class CartPublishImpl implements CartPublish {
	
	private static final String FILE_PATH = "D:\\BSc. Information Technology (sp.) - SLIIT\\3rd Year 2nd Semester\\Assesments\\SA\\Assignment 1\\OSGi-plugin-project\\com.regexextractors.cart\\src\\com\\regexextractors\\cart";
	private File cartFile = new File(FILE_PATH);
	
	
	/**
	 * Display products in the cart.
	 * @return void
	 */
	@Override
	public void displayCart () {
		
		double total = 0.0;
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document cartDocument = documentBuilder.parse(cartFile);
			cartDocument.getDocumentElement().normalize();
			
			NodeList productNodeList = cartDocument.getElementsByTagName("product");
			
			System.out.println(" Product Cart --------------\n\n");
			System.out.println("ID\t| Product\t| Quantity\t| Price (LKR.)");
			
			for (int i = 0; i < productNodeList.getLength(); i++) {
				Node productNode = productNodeList.item(i);
				if(productNode.getNodeType() == Node.ELEMENT_NODE) {
					Element productElement = (Element) productNode;
					
					// Print all details of a productElement content
					System.out.print(productElement.getElementsByTagName("id").item(0).getTextContent() + "\t|");
					System.out.print(productElement.getElementsByTagName("name").item(0).getTextContent() + "\t\t|");
					
					int quantity = Integer.parseInt(productElement.getElementsByTagName("quantity").item(0).getTextContent());
					Double unitPrice = Double.parseDouble(productElement.getElementsByTagName("price").item(0).getTextContent());
					
					System.out.print(quantity + "\t|");
					System.out.print((unitPrice * quantity) + "\t");
					System.out.println();
					
					total+= (unitPrice * quantity);
				}
			}
			
			System.out.println(">>> TOTAL = " + total);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Store selected products 
	 * @param rootElement 
	 * @param document
	 * @param name
	 * @param price
	 * @param qty
	 * @return Document
	 */
	@Override
	public Document addToCart(Element rootElement, Document document, String name, double price, int quantity) {
		
		Document cartDocument = document;
		
		try {
			
			// Create new element object to store in file
			Element product, productName, productPrice, productQuantity;
			
			// Initialize elements with element name and 
			// set element content
			product = cartDocument.createElement("product");
			
			productName = cartDocument.createElement("name");
			productName.setTextContent(name);
			
			productPrice = cartDocument.createElement("price");
			productPrice.setTextContent(String.valueOf(price));
			
			productQuantity = cartDocument.createElement("quantity");
			productQuantity.setTextContent(String.valueOf(quantity));
			
			// Append all elements to newly created product element
			product.appendChild(productName);
			product.appendChild(productPrice);
			product.appendChild(productQuantity);
			
			// Append product element into root element
			rootElement.appendChild(product);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cartDocument;
	}
	
	
	/**
	 * Store cart details in 'cart-data.xml'
	 * @param document
	 */
	@Override
	public void storeCartInFile (Document document) {
		
		try {
			
			DOMSource source = new DOMSource(document);
			
			StreamResult result = new StreamResult(cartFile);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Navigate to the Product Store
	 * @return void
	 */
	@Override
	public void navigateToProducts() {
		 System.out.println("Loading Products...");
		 
		 // Access StorePublish as a service
		 BundleContext context = FrameworkUtil.getBundle(CartPublish.class).getBundleContext();
		 ServiceReference storeService = context.getServiceReference(StorePublish.class.getName());
		 StorePublish store = (StorePublish) context.getService(storeService);
		 
		 // Display store menu and select products to store in cart
		 store.displayMenu();
		 store.selectItemFromMenu();
	}
	
	
	/**
	 * Process the payment
	 * @param amount - Amount to process payment
	 */
	@Override
	public void navigateToPayment(double amount) {
		 System.out.println("Loading Payment gateway...");
		
		 // Access StorePublish as a service
		 BundleContext context = FrameworkUtil.getBundle(CartPublish.class).getBundleContext();
		 ServiceReference paymentService = context.getServiceReference(PaymentService.class.getName());
		 PaymentService payment = (PaymentService) context.getService(paymentService);
		 
		 // Make payment
		 payment.makePayment(amount);
	}
}
