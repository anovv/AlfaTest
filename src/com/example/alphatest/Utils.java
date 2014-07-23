package com.example.alphatest;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/*
 * contains method to get items from server*/

public class Utils {
	
	private static final String URL = "http://pda.alfabank.ru/_/rss/_rss.html";
	
	public static List<Item> getItemsFromServer(){
		HttpClient httpclient = new DefaultHttpClient();		
		HttpGet httpget = new HttpGet(URL); 			
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);			
		} catch (Exception e) {	
			return null;
		}		
		
		String requestResult = null;
		try {
			requestResult = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {	
			System.out.print(e.toString());
			return null;
		}	
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder builder;  
	    Document document;
	    try {  
	        builder = factory.newDocumentBuilder();  
	        document = builder.parse(new InputSource(new StringReader(requestResult)));  
	    } catch (Exception e) {  
			return null;
	    } 
	    
	    List<Item> items = new ArrayList<Item>();
	    
	    NodeList nodeList = document.getDocumentElement().getChildNodes();
	    
	    Node n = nodeList.item(1);
	    
	    nodeList = n.getChildNodes();
	    
	    for(int i = 0; i < nodeList.getLength(); i++){
	    	Node node = nodeList.item(i);
	    	if(node.getNodeName().equals("item")){
	    		NodeList childNodes = node.getChildNodes();
	    		String title = null, pubDate = null, link = null;
	    		
	    		for(int j = 0; j < childNodes.getLength(); j++){
	    			Node cNode = childNodes.item(j);
	    			String content = cNode.getTextContent().trim();
	    			
	    			String nodeName = cNode.getNodeName();
	    			
	    			if(nodeName.equals("title")){
    					title = content;
	    			}else if(nodeName.equals("link")){
	    				link = content;
	    			}else if(nodeName.equals("pubDate")){
	    				pubDate = content;
	    			}
	    		}	    	
	    		
	    		Item item = new Item(title, pubDate, link);	    		
	    		items.add(item);
	    	}
	    }
	    
	    return items;
	}
}
