package com.regexextractors.store;

import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * This OSGi plug-in service is implemented as Store Service
 * @author RegEx Extractors
 */
public interface StorePublish {

	/**
	 * Getting all products from 'storeData.xml' 
	 * and Display all menu items.
	 * @return void
	 */
	void displayMenu();

	/**
	 * Select a product using menu
	 * @return void
	 */
	void selectItemFromMenu();

	/**
	 * Insert a product into 'store-data.xml'
	 * @return void
	 */
	void insertItems();

	/**
	 * Get product availability
	 * @param String id - Product id
	 * @param int - Required quantity
	 * @return boolean
	 * @throws IOException 
	 * @throws SAXException 
	 */
	boolean isAvailable(String id, int quantity) throws SAXException, IOException;

	
	
}
