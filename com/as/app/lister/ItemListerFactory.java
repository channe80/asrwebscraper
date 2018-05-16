package com.as.app.lister;


public class ItemListerFactory {
	
	private ItemLister lister;

	public ItemLister getLister(final ListingSite site) {
		
		switch (site) {
		case TRADEME:
			//temp: tmsanbox must be in a config file 
			lister = new TrademeItemLister();
			
			break;

		default:
			lister = null;
			break;
		}
		
		return lister;
	}
}
