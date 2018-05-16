package com.as.app.webscraper;

public class AutoPartsScraperFactory {
		
	private AutoPartsScraper scraper;
	
	public AutoPartsScraper getScraper(final AutoPartSource source) {
		
		switch (source) {
		case NASA:
			scraper = new NASAScraper();
			
			break;

		default:
			scraper = null;
			break;
		}
		
		return scraper;
	}
}
