package com.as.app.webscraper;

import java.util.concurrent.TimeUnit;

public class ASWebScraper {

	private AutoPartSource determineSource(final String wholesaler) {
		
		if (AutoPartSource.NASA.value().equalsIgnoreCase(wholesaler)){
			return  AutoPartSource.NASA;
		}
		
		return null;
	}
	
	public void scrapeAll(final String wholesaler) {
		
		//TODO: figure out source base on wholesaler
		AutoPartSource source = determineSource(wholesaler);
		if (source != null) {
			final long time = System.currentTimeMillis();

			final AutoPartsScraperFactory factory = new AutoPartsScraperFactory();
			final AutoPartsScraper scraper = factory.getScraper(source);

//			scraper.connect();

			//TODO: login

			scraper.scrapeAll();

			long completedIn = System.currentTimeMillis() - time;

			String formatted = String.format("%d min, %d sec", 
					TimeUnit.MILLISECONDS.toMinutes(completedIn),
					TimeUnit.MILLISECONDS.toSeconds(completedIn) - 
					TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(completedIn))
					);

			System.out.println("completed in: " + formatted);

		}
	}
	
}
