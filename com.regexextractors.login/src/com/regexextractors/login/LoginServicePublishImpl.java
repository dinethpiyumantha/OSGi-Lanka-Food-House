package com.regexextractors.login;

import com.regexextractors.store.StorePublish;
import java.io.Console;
import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LoginServicePublishImpl implements LoginServicePublish {
	
	private static final String FILE_PATH = "D:\\BSc. Information Technology (sp.) - SLIIT\\3rd Year 2nd Semester\\Assesments\\SA\\Assignment 1\\OSGi-plugin-project\\com.regexextractors.login\\src\\com\\regexextractors\\login\\user-data.xml";
	private File userFile = new File(FILE_PATH);
	
	private Scanner scanner = new Scanner(System.in);
	
	
	/**
	 * Log in to the system using user data
	 * @return void 
	 */
	@Override
	public void systemLogin() {
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document userDocument = documentBuilder.parse(userFile);
			userDocument.getDocumentElement().normalize();
			NodeList userNodes = userDocument.getElementsByTagName("user");
			
			BundleContext context = FrameworkUtil.getBundle(LoginServicePublish.class).getBundleContext();
			ServiceReference storeServiceReference = context.getServiceReference(StorePublish.class.getName());
			StorePublish store = (StorePublish) context.getService(storeServiceReference);
			
			while (true) {
				System.out.println("***************************\nLANKA FOOD HOUSE\n***************************\n\nLogin Form\n\n");
				System.out.print("Username\t: ");
				String username = scanner.next();
				
				Console console = System.console();
				if (console == null) {
					System.out.println("Cannot get password (Console Error!)");
				} else {
					System.exit(0);
				}
				String password = new String(console.readPassword("Password: (hidden)"));
				
				for (int i = 0; i < userNodes.getLength(); i++) {
					Node userNode = userNodes.item(i);
					if(userNode.getNodeType() == Node.ELEMENT_NODE) {
						Element userElement = (Element) userNode;
						
						if(userElement.getElementsByTagName("username").equals(username)) {
							if(userElement.getElementsByTagName("password").equals(password)) {
								store.displayMenu();
								store.selectItemFromMenu();
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
