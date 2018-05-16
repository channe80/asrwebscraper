package com.as.app.lister;


public class ASItemLister {

	private ListingSite determineSite(final String site) {
		
		if (ListingSite.TRADEME.value().equalsIgnoreCase(site)){
			return  ListingSite.TRADEME;
		}
		
		return null;
	}
	
	public void listActiveItems(final String site) {		
		final ItemListerFactory factory = new ItemListerFactory();
		ItemLister lister = factory.getLister(determineSite(site));
		lister.listActiveItems();
	}
}
