package com.as.app.webscraper;

import java.io.IOException;

public abstract class AutoPartsScraper {
	
	public abstract boolean login();
	public abstract boolean logout();
	public abstract void scrapeAll();
	public abstract void scrapePartDetails(int modelId, final String linkId) throws IOException;
	
}
