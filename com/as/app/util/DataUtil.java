package com.as.app.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.as.app.beans.Car;
import com.as.app.beans.Part;
import com.as.app.dao.CarPartsDao;
import com.as.app.dao.DBManager;

public class DataUtil {
	
	CarPartsDao dao = DBManager.getCarPartsDaoInstance();
	
	public void calculateActivePartPrices() {
		CarPartsDao dao = DBManager.getCarPartsDaoInstance();
		dao.clearActiveParts();
		dao.populateActiveParts();
	}
	
	public void calculateSpecificPartPrices() {
		CarPartsDao dao = DBManager.getCarPartsDaoInstance();
		dao.clearActiveParts();
		dao.calculateSpecificPartPrices();
	}
	
	public void addPartGroup() {
		
		HashMap<Car, ArrayList<Part>> activeParts = dao.getPartsByPartnumberInTemp();
		addPartGroup(activeParts);		
	}
	
	public void addPartGroup(ArrayList<String> partnumbers) {	
		
		HashMap<Car, ArrayList<Part>> activeParts = dao.getPartsByPartnumber(partnumbers);
		addPartGroup(activeParts);
	}
	
	public void addPartGroup(HashMap<Car, ArrayList<Part>> activeParts) {
		Iterator<Entry<Car, ArrayList<Part>>> iterator = activeParts.entrySet().iterator();			

		while (iterator.hasNext()) {
			//group title - part id Map
			HashMap <String, ArrayList<String>> group = new  HashMap <String, ArrayList<String>>();
			Entry<Car, ArrayList<Part>> next = iterator.next();
			ArrayList<Part> parts = next.getValue();
//			System.out.println(parts.size());
			for (Part part : parts) {
		//		part.getPartName().trim().s;
//				group.get(key)
				
				String key = ""; 
				if(part.getPartName().contains(", RH")) { //RH
					key = StringUtil.removeSubstring(part.getPartName(), ", RH");					
				} else if (part.getPartName().contains("RH, ")) {
					key = StringUtil.removeSubstring(part.getPartName(), "RH, ");
				} else if (part.getPartName().contains("RH,")) {
					key = StringUtil.removeSubstring(part.getPartName(), "RH,");
				} else if ((part.getPartName().contains(" RH"))) {
					key = StringUtil.removeSubstring(part.getPartName(), " RH");
				} else if (part.getPartName().contains("RH ")) {					
					key = StringUtil.removeSubstring(part.getPartName(), "RH ");
				} else if (part.getPartName().contains("RH")) {
					key = StringUtil.removeSubstring(part.getPartName(), "RH");
					

				} else if (part.getPartName().contains(", LH")) {  //LH
					key = StringUtil.removeSubstring(part.getPartName(), ", LH");
				} else if (part.getPartName().contains("LH, ")) {
					key = StringUtil.removeSubstring(part.getPartName(), "LH, ");
				} else if (part.getPartName().contains("LH,")) {
					key = StringUtil.removeSubstring(part.getPartName(), "LH,");
				} else if ((part.getPartName().contains(" LH"))) {
					key = StringUtil.removeSubstring(part.getPartName(), " LH");
				} else if (part.getPartName().contains("LH ")) {					
					key = StringUtil.removeSubstring(part.getPartName(), "LH ");
				} else if (part.getPartName().contains("LH")) {
					key = StringUtil.removeSubstring(part.getPartName(), "LH");

				} else if (part.getPartName().contains(", RF")) {  //RF
					key = StringUtil.removeSubstring(part.getPartName(), ", RF");
				} else if (part.getPartName().contains("RF, ")) {
					key = StringUtil.removeSubstring(part.getPartName(), "RF, ");
				} else if (part.getPartName().contains("RF,")) {
					key = StringUtil.removeSubstring(part.getPartName(), "RF,");
				} else if ((part.getPartName().contains(" RF"))) {
					key = StringUtil.removeSubstring(part.getPartName(), " RF");
				} else if (part.getPartName().contains("RF ")) {					
					key = StringUtil.removeSubstring(part.getPartName(), "RF ");
				} else if (part.getPartName().contains("RF")) {
					key = StringUtil.removeSubstring(part.getPartName(), "RF");
				
				
				} else if (part.getPartName().contains(", LF")) {  //LF
					key = StringUtil.removeSubstring(part.getPartName(), ", LF");
				} else if (part.getPartName().contains("LF, ")) {
					key = StringUtil.removeSubstring(part.getPartName(), "LF, ");
				} else if (part.getPartName().contains("LF,")) {
					key = StringUtil.removeSubstring(part.getPartName(), "LF,");
				} else if ((part.getPartName().contains(" LF"))) {
					key = StringUtil.removeSubstring(part.getPartName(), " LF");
				} else if (part.getPartName().contains("LF ")) {					
					key = StringUtil.removeSubstring(part.getPartName(), "LF ");
				} else if (part.getPartName().contains("LF")) {
					key = StringUtil.removeSubstring(part.getPartName(), "LF");
				}
				
//				System.out.println(part.getPartName() + " => "+ key + "(" + part.getPartNumber()+ ")");
				
				if (!key.isEmpty() &&  key.length() < part.getPartName().trim().length()) {
					if (group.containsKey(key)) {
						ArrayList<String> ids = group.get(key);
						ids.add(part.getPartNumber());
						group.put(key, ids);
					} else {
						ArrayList <String> ids = new ArrayList <String>();
						ids.add(part.getPartNumber());
						group.put(key, ids);
					}
				}
			}
			
//			System.out.println(group.size());
			//save to database
			dao.saveGroups(group);
			Iterator<Entry<String, ArrayList<String>>> iterator2 = group.entrySet().iterator();
			while (iterator2.hasNext())  {
				Entry<String, ArrayList<String>> next2 = iterator2.next();
				String groupName = next2.getKey();
				ArrayList<String> partNumbers = next2.getValue();
				System.out.print(groupName + " : " );
				for (String num : partNumbers) {
					System.out.print(num + ", ");
				}
				System.out.println();
			}
			
		}
		
		
		
	}

	public static void main(String args[]) {
		DataUtil u = new DataUtil();
		
//		u.calculateSpecificPartPrices();
//		u.calculateActivePartPrices();
		u.addPartGroup(new ArrayList<String>(Arrays.asList("D4085", "D4084") ));
//		u.addPartGroup();
	}
}
