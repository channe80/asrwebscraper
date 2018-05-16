package com.as.app;

import com.as.app.checker.StockLevelChecker;
import com.as.app.dao.DBManager;
import com.as.app.lister.ASItemLister;
import com.as.app.webscraper.ASWebScraper;

public class ASAdmin {
	
	public static final String TRADEME_DOMAIN =   "trademe";//"tmsandbox"; //  

	public static void main(String[] args){

		System.out.println(args[0]);
		if (args[0].trim().equalsIgnoreCase("scraper")) {
			System.out.println(args[1]);
			ASWebScraper scraper = new ASWebScraper();
			scraper.scrapeAll(args[1]);
		} else if (args[0].trim().equalsIgnoreCase("lister")) { 	
			System.out.println(args[1]);
			ASItemLister lister = new ASItemLister();
			lister.listActiveItems(args[1]);
		} else if (args[0].trim().equalsIgnoreCase("checker")) { 	
			StockLevelChecker checker = new StockLevelChecker();
			checker.runChecker();
		} else if (args[0].trim().equalsIgnoreCase("report")) {	
			
		} else {	
			System.out.println("First parameter is incorrect. Please choose from the following options: scraper, lister, checker, report.");
		}	
			
	}
}
