package com.regexextractors.store;

import com.regexextractors.cart.CartPublish;
import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This OSGi plug-in service is implemented as Store Service
 * @author RegEx Extractors
 */
public class StorePublishImpl implements StorePublish {
	
	// Constants
	private static final String FILE_PATH = "D:\\BSc. Information Technology (sp.) - SLIIT\\3rd Year 2nd Semester\\Assesments\\SA\\Assignment 1\\OSGi-plugin-project\\com.regexextractors.store\\src\\com\\regexextractors\\store\\store-data.xml";
	
	// Initialize Store data file to a new instance
	private File storeFile = new File(FILE_PATH);
	
	private Scanner scanner = new Scanner(System.in);
				
	
	/**
	 * Getting all products from 'storeData.xml' 
	 * and Display all menu items.
	 * @return void
	 */
	@Override
	public void displayMenu() {

		// Catch exceptions on runtime
		try {
			
			// Assign all 'products' to a NodeList from storeData.xml
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document storeDocument = documentBuilder.parse(this.storeFile);
			storeDocument.getDocumentElement().normalize();
			NodeList productList = storeDocument.getElementsByTagName("product");
			
			// Display product list from a NodeList
			System.out.println("==== LANKA FOOD HOUSE ===== \n" + "All Products --------------\n\n");
			System.out.println("ID\t| Product\t| Price (LKR.)\t| Quantity");
			
			// Iterate productList over using loop and print in console
			for (int i = 0; i < productList.getLength(); i++) {
				Node productNode = productList.item(i);
				if(productNode.getNodeType() == Node.ELEMENT_NODE) {
					Element productElement = (Element) productNode;
					
					// Print all details of a productElement content
					System.out.print(productElement.getElementsByTagName("id").item(0).getTextContent() + "\t|");
					System.out.print(productElement.getElementsByTagName("name").item(0).getTextContent() + "\t\t|");
					System.out.print(productElement.getElementsByTagName("price").item(0).getTextContent() + "\t|");
					System.out.print(productElement.getElementsByTagName("quantity").item(0).getTextContent() + "\t");
					System.out.println();
				}
			}
			
		} catch (Exception e) {
			// Print exception details in console
			e.printStackTrace();
		}

	}

	

	/**
	 * Select a product using menu
	 * @return void
	 */
	@Override
	public void selectItemFromMenu() {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document productDocument = documentBuilder.parse(storeFile);
			productDocument.getDocumentElement().normalize();
			
			DocumentBuilderFactory cartDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder cartDocumentBuilder = cartDocumentBuilderFactory.newDocumentBuilder();
			Document cartProductDocument = cartDocumentBuilder.newDocument();
			Element rootOfCartElement = cartProductDocument.createElement("cart");
			cartProductDocument.appendChild(rootOfCartElement);
			
			BundleContext context = FrameworkUtil.getBundle(StorePublish.class).getBundleContext();
			ServiceReference cartService = context.getServiceReference(CartPublish.class.getName());
			CartPublish cart = (CartPublish) context.getService(cartService);
			
			NodeList productNodeList = cartProductDocument.getElementsByTagName("product");
			String response;
			
			while(true) {
				System.out.print("Product ID: ");
				String id = scanner.next();
				int quantity = 0;
				
				for (int i = 0; i < productNodeList.getLength(); i++) {
					
					Node productNode = productNodeList.item(i);
					
					if(productNode.getNodeType() == Node.ELEMENT_NODE) {
						
						Element productElement = (Element) productNode;
						
						if(id.equals(productElement.getElementsByTagName("id").item(0).getTextContent())) {
							System.out.print("Quantity: ");
							quantity = scanner.nextInt();
						
							if(this.isAvailable(id, quantity)) {
								double price = Double.parseDouble(productElement.getElementsByTagName("price").item(0).getTextContent());
								cart.addToCart(rootOfCartElement, cartProductDocument, id, i, quantity);
							} else {
								System.out.println("Amount quantity is not available!\n");
							}

						}
					}
				}
				
				System.out.print("Do you want to grab more (y/yes) or (n/no)? ");
				response = scanner.next();
				
				if(response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
					cart.storeCartInFile(cartProductDocument);
					cart.displayCart();
					break;
				} else if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
					continue;
				} else {
					System.out.println("Invalid Input!");
					break;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	/**
	 * Insert a product into 'store-data.xml'
	 * @return void
	 */
	@Override
	public void insertItems() {
		
		Scanner scanner = new Scanner(System.in);
		
		int count = 0;
		
		try {
			System.out.println();
			System.out.println("Add Products ============");
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document productDocument = documentBuilder.newDocument();
			
			Element rootElement = productDocument.createElement("store");
			productDocument.appendChild(rootElement);
			
			while (true) {
				
				count++;
				Element product = productDocument.createElement("product");
				
				// Get user inputs and set values into element objects
				Element idElement = productDocument.createElement("id");
				idElement.setTextContent(String.valueOf(count));
				System.out.println("Product ID: " + count);
				
				System.out.print("Product Name: ");
				Element nameElement = productDocument.createElement("name");
				nameElement.setTextContent(String.valueOf(scanner.nextLine()));
				
				System.out.print("Unit Price: ");
				Element priceElement = productDocument.createElement("price");
				priceElement.setTextContent(String.valueOf(scanner.nextLine()));
				
				System.out.print("Quantity: ");
				Element quantityElement = productDocument.createElement("name");
				quantityElement.setTextContent(String.valueOf(scanner.nextLine()));
				
				// Append all inserted values into product element
				product.appendChild(idElement);
				product.appendChild(nameElement);
				product.appendChild(priceElement);
				product.appendChild(quantityElement);
				
				// Append product element into root element
				rootElement.appendChild(product);
				
				// Asking for new insertion
				String insertValue;
			
				do {
					System.out.println("Do you want add more items (Y/Yes) or (N/No) ? ");
					insertValue = scanner.nextLine();
					System.out.println();
					
					if (insertValue.equalsIgnoreCase("YES") || insertValue.equalsIgnoreCase("Y")) {
						continue;
					} else if (insertValue.equalsIgnoreCase("NO") || insertValue.equalsIgnoreCase("N")) {
						break;
					} else {
						System.out.println("\nInvalid user input '"+ insertValue +"'");
						System.out.println("Try again ...");
					}
				} while (insertValue.equalsIgnoreCase("NO") || insertValue.equalsIgnoreCase("N"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Get product availability
	 * @param String id - Product id
	 * @param int - Required quantity
	 * @return boolean
	 */
	@Override
	public boolean isAvailable(String id, int quantity) {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document storeDocument = documentBuilder.parse(this.storeFile);
			storeDocument.getDocumentElement().normalize();
			
			// Get all product nodes
			NodeList productNodes = storeDocument.getElementsByTagName("product");
			
			while (true) {
				for (int i = 0; i < productNodes.getLength(); i++) {
					Node productNode = productNodes.item(i);
					if(productNode.getNodeType() == Node.ELEMENT_NODE) {
						Element productElement = (Element) productNode;
						
						// Check and access productNode by id
						if(id.equals(productElement.getElementsByTagName(id).item(0).getTextContent())) {
							int productQuantity = Integer.parseInt(productElement.getElementsByTagName("quantity").item(0).getTextContent());
							
							// Check product quantity is available or not and return value as a boolean value
							return (quantity > quantity) ? true : false ;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * This method generate 12 character string with spaces
	 * @param String string - Word
	 * @return String
	 */
	private String getDisplayString (String string) {
		int nullCharCount = 12 - string.length();
		String modifiedString = string;
		for (int i = 0; i < nullCharCount; i++) {
			modifiedString+=" ";
		}
		return modifiedString;
	}

}
