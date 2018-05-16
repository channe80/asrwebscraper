package com.as.app.checker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.as.app.ASAdmin;
import com.as.app.beans.Car;
import com.as.app.beans.ListingStatus;
import com.as.app.beans.Part;
import com.as.app.beans.Wholesaler;
import com.as.app.dao.CarPartsDao;
import com.as.app.dao.DBManager;
import com.as.app.dao.PrestashopDao;
import com.as.app.trademe.beans.Item;
import com.as.app.trademe.beans.Items;
import com.as.app.trademe.beans.SoldItems;
import com.as.app.trademe.beans.UnsoldItems;
import com.as.app.trademe.util.TrademeApi;
import com.as.app.webscraper.AutoPartSource;
import com.as.app.webscraper.AutoPartsScraper;
import com.as.app.webscraper.AutoPartsScraperFactory;

public class StockLevelChecker {

	public void updateActivePartsStockLevel() {	
		//get active items
		int errCount = 0;
		StringBuffer sb = new StringBuffer();
		CarPartsDao dao = DBManager.getCarPartsDaoInstance();
		HashMap<Car, ArrayList<Part>> activeParts = dao.getActiveParts();
		
		final AutoPartsScraperFactory factory = new AutoPartsScraperFactory();
		final AutoPartsScraper scraper = factory.getScraper(AutoPartSource.NASA);		
		
		//loop through each active part
		System.out.println("\n\n============ Updating ACTIVE PARTS DATA ============");
		Iterator<Entry<Car, ArrayList<Part>>> iterator = activeParts.entrySet().iterator();			
		scraper.login();
		while (iterator.hasNext()) {
			Entry<Car, ArrayList<Part>> next = iterator.next();
			ArrayList<Part> parts = next.getValue();
			for (Part part : parts) {	
				//scrape and save part details
				try {
					scraper.scrapePartDetails(part.getModelId(), part.getLinkId());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					sb.append("Error when scraping linkid: " + part.getLinkId() + " for part: " + part.getPartNumber() + " model: " + part.getModelId() + "\n");
					errCount++;
				}
			}
		}
		scraper.logout();
		if (errCount > 0) {
			System.out.println("Error Count: " + errCount);
			System.out.println(sb.toString());
		}
		System.out.println("FINISHED Updating active parts");
		System.out.println("===================================================");

	}
	
	public int updateSoldItemsStatus() {		
		System.out.println("\n\n============ Updating SOLD ITEM status ============");
		TrademeApi trademe = new TrademeApi(ASAdmin.TRADEME_DOMAIN);
		
		ArrayList<Long> listingIds = new ArrayList<Long>();
		int page = 1;
		SoldItems soldItems = trademe.getSoldItems(page);
		if(soldItems.getTotalCount() <=0) {
			System.out.println("FINISHED Updating 0 Sold items status");
			System.out.println("===================================================");
			return 0;
		}
		
		CarPartsDao dao =DBManager.getCarPartsDaoInstance();
		
		do {

			List<Item> soldItemsList = soldItems.getList();
			for (Item item : soldItemsList) {
				listingIds.add(item.getListingId());
				if (item.getBidderAndWatchers() > 0 || item.getViewCount() > 0 )  {
					System.out.println("TITLE: " + item.getTitle() + " Views: " + item.getViewCount() + " Watch: " + item.getBidderAndWatchers() + " BidAmount: " +item.getMaxBidAmount());			
					dao.updateListedItem(item.getListingId(), item.getBidderAndWatchers(), item.getMaxBidAmount(), item.getViewCount());
				}				
			}
			page++;
			soldItems = trademe.getSoldItems(page);
		} while(!soldItems.getList().isEmpty());

		if (!listingIds.isEmpty()) {
			dao.updateItemsStatusTo(listingIds, ListingStatus.SOLD, new ArrayList<ListingStatus>(Arrays.asList(ListingStatus.LISTED, ListingStatus.UNSOLD)));
		}
		System.out.println("FINISHED Updating " + listingIds.size() + " Sold items status");
		System.out.println("===================================================");
		
		return soldItems.getTotalCount();
	}
	
	
	public int updateUnsoldItemsStatus() {		
		System.out.println("\n\n============ Updating UNSOLD ITEMS status ==========");
		TrademeApi trademe = new TrademeApi(ASAdmin.TRADEME_DOMAIN);
		
		ArrayList<Long> listingIds = new ArrayList<Long>();
		int page = 1;
		UnsoldItems unsoldItems = trademe.getUnsoldItems(page);
		if(unsoldItems.getTotalCount() <=0) {
			System.out.println("FINISHED Updating 0 Unsold items status");
			System.out.println("===================================================");

			return 0;
		}

		CarPartsDao dao =DBManager.getCarPartsDaoInstance();
		
		do {
			List<Item> unsoldItemsList =  unsoldItems.getList();
			for (Item item : unsoldItemsList) {
				listingIds.add(item.getListingId());
				if (item.getBidderAndWatchers() > 0 || item.getViewCount() > 0 )  {
					System.out.println("TITLE: " + item.getTitle() + " Views: " + item.getViewCount() + " Watch: " + item.getBidderAndWatchers() + " BidAmount: " +item.getMaxBidAmount());			
					dao.updateListedItem(item.getListingId(), item.getBidderAndWatchers(), item.getMaxBidAmount(), item.getViewCount());
				}
			}
			page++;
			unsoldItems = trademe.getUnsoldItems(page);
		} while(!unsoldItems.getList().isEmpty());
		
		if (!listingIds.isEmpty()) {
			int updateItemsStatusTo = dao.updateItemsStatusTo(listingIds, ListingStatus.UNSOLD, new ArrayList<ListingStatus>(Arrays.asList(ListingStatus.LISTED)));
			System.out.println(updateItemsStatusTo);
		}
		
		System.out.println("FINISHED Updating " + listingIds.size() + " Unsold items status");
		System.out.println("===================================================");
		
		return unsoldItems.getTotalCount();
	}
	
	public int updateListedItems() {		
		System.out.println("\n\n============ Updating LISTED ITEMS =================");
		TrademeApi trademe = new TrademeApi(ASAdmin.TRADEME_DOMAIN);
		int page = 1;
		int listingsWithViews = 0;
		int withWatches=0;
		Items listedItems = trademe.getListedItems(page);
		
		if(listedItems.getTotalCount() <=0) {
			return 0;
		}
		
		do {

			List<Item> listedItemsList =  listedItems.getList();
			for (Item item : listedItemsList) {
				if (item.getBidderAndWatchers() > 0 || item.getViewCount() > 0 )  {
					System.out.println("TITLE: " + item.getTitle() + " Views: " + item.getViewCount() + " Watch: " + item.getBidderAndWatchers() + " BidAmount: " +item.getMaxBidAmount());
					//System.out.println("======================================================================================================");
					CarPartsDao dao =DBManager.getCarPartsDaoInstance();
					dao.updateListedItem(item.getListingId(), item.getBidderAndWatchers(), item.getMaxBidAmount(), item.getViewCount());
					listingsWithViews++;
					if (item.getBidderAndWatchers() > 0) {
						withWatches ++;
					}
				}
			} 
			page++;
			listedItems = trademe.getListedItems(page);
		} while (!listedItems.getList().isEmpty()) ;
		System.out.println("\n\n Listing with Views: " + listingsWithViews  + "   with Watches:  " + withWatches);
		System.out.println("===================================================");
		
		return listedItems.getTotalCount();
	}
	

	public int checkListingsWithUnsansweredQuestions() {		
		System.out.println("\n\n========= Checking items with UNANSWERED QUESTIONS ===========");
		TrademeApi trademe = new TrademeApi(ASAdmin.TRADEME_DOMAIN);
		Items listedItems = trademe.getListedItemsWithUnansweredQuestions(1);
		
		System.out.println("Listings with unanswered questions: " + listedItems.getTotalCount());
		System.out.println("===================================================");
		
		return listedItems.getTotalCount();
	}
	
	
	public void checkItemsStockLevels() {
		System.out.println("\n\n============ STOCK LEVEL CHECK ====================");
		CarPartsDao dao = DBManager.getCarPartsDaoInstance();		
		StringBuffer message = dao.checkLowStocks();
		System.out.println(message.toString());
		System.out.println("===================================================");
	}
	
	public void updatePrestaStockLevels() {
		System.out.println("\n\n========= Updating AP&P Website StockLevels ===========");
		CarPartsDao dao = DBManager.getCarPartsDaoInstance();
		PrestashopDao prestaDao = DBManager.getPrestashopDaoInstance();
		int cnt = 0;
		HashMap<Integer, Wholesaler> wholesalers = dao.getAllWholesalers();
		Iterator<Entry<Integer, Wholesaler>> i = wholesalers.entrySet().iterator();
		while(i.hasNext()) {
			//get parts and their stocks by wholesaler
			Wholesaler w = i.next().getValue();
			HashMap<String, Integer> partsStocks = dao.getActivePartsStockLevelByWholesaler(w.getId());		
			Iterator<Entry<String, Integer>> it = partsStocks.entrySet().iterator();		
			while (it.hasNext()) {
				Entry<String, Integer> partStock = it.next();
				String partnum = partStock.getKey();
				Integer stockTemp = partStock.getValue();
				//the website stock level will be 2 less than the actual stock level from the wholesaler
				int stock = (stockTemp - 2) < 0 ? 0 : stockTemp - 2;

				//get stock from presta database
				String partReference = w.getCode() + partnum;
				int prestaStock = prestaDao.getPartQuantity(partReference);

				//update presta stock level if following conditions are met:
				//stock level presta > stockinventory is lower
				//stock level presta < stockinventory (ie.someone bought from website and NP has not updated) and difference between them is 2 
				//(i.e. if nasa stock is higher by 2, then presta could be out dated. at the moment this is the safest update I can think of. )			
				if (prestaStock >= 0  
						&& (prestaStock > stock 
								|| (prestaStock < stock && stock - prestaStock == 2)) ) {
					prestaDao.updateAvailableStock(partReference, stock);
					System.out.println("PartId: " + partnum + " presta stock: " + prestaStock + " changed to: " + stock);
					cnt ++;
				}
			}
		}
		System.out.println("Updated stock levels of " + cnt + " products.");
		System.out.println("===================================================");
		
	}
	
	public void runChecker() {
		
		
//		try {
//			updateActivePartsStockLevel();
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("IOException occurred. Scraping all.. ");
//			final AutoPartsScraperFactory factory = new AutoPartsScraperFactory();
//			final AutoPartsScraper scraper = factory.getScraper(AutoPartSource.NASA);		
//			scraper.scrapeAll();
//		}
		
		updateActivePartsStockLevel();
		
		int soldItemsCnt = updateSoldItemsStatus();
		
		int unsoldItemsCnt = updateUnsoldItemsStatus();
		
		int listedItemsCnt = updateListedItems();
		
		int questionsCnt = checkListingsWithUnsansweredQuestions();
		
		System.out.println("\n\nUNSOLD Items: " + unsoldItemsCnt);
		System.out.println("LISTED Items: " + listedItemsCnt);
		System.out.println("SOLD Items: " + soldItemsCnt);
		System.out.println("QUESTIONS : " + questionsCnt);
		
		checkItemsStockLevels();
		
		//TODO: UPDATE presta quantity
		updatePrestaStockLevels();
		
		//generate report  - try sql first
		/*
		 *select m.name, o.name, o.subname, p.wholesalerpartno, p.stock, l.listingid, l.status,
		 
             CASE 
                  WHEN p.stock <= 2 
                     THEN 'STOCK IS LOW!' 
                  ELSE '' 
             END as alert
from part p, tm_listeditems l, make m, model o
where p.wholesalerpartno = l.wholesalerpartno and p.modelid = o.id and o.makeid = m.id
order by p.wholesalerpartno, l.listingid
		 */
		
		/*
		 select m.name, o.name, o.subname, o.year, p.wholesalerpartno, p.partname, p.stock, l.listingid, l.status,
		 
             CASE 
                  WHEN p.stock <= 2 
                     THEN 'STOCK IS LOW!' 
                  ELSE '' 
             END as alert
from  part p LEFT JOIN tm_listeditems l ON (p.wholesalerpartno = l.wholesalerpartno), make m, model o
where p.modelid = o.id and o.makeid = m.id and p.isactive = true
order by p.wholesalerpartno, l.listingid 
		 */
	}	
	

	public void updateSoldItemsRecords() {		
		
		TrademeApi trademe = new TrademeApi(ASAdmin.TRADEME_DOMAIN);
		
		ArrayList<Long> listingIds = new ArrayList<Long>();
		int page = 1;
		SoldItems soldItems = trademe.getSoldItems(page);
		if(soldItems.getTotalCount() <=0) {
			System.out.println("FINISHED Updating 0 Sold items status");
			System.out.println("===================================================");
			return;
		}
		
		CarPartsDao dao = DBManager.getCarPartsDaoInstance();
		do {

			List<Item> soldItemsList = soldItems.getList();
			for (Item item : soldItemsList) {
				dao.updateListedItem(item.getListingId(), item.getBidderAndWatchers(), item.getMaxBidAmount(), item.getViewCount());
			}
			page++;
			soldItems = trademe.getSoldItems(page);
		} while(!soldItems.getList().isEmpty());

	}
	
	
	public void updateUnsoldItemsRecords() {		
		System.out.println("\n\n============ Updating UNSOLD ITEMS status ==========");
		TrademeApi trademe = new TrademeApi(ASAdmin.TRADEME_DOMAIN);
		
		ArrayList<Long> listingIds = new ArrayList<Long>();
		int page = 1;
		UnsoldItems unsoldItems = trademe.getUnsoldItems(page);
		if(unsoldItems.getTotalCount() <=0) {
			System.out.println("FINISHED Updating 0 Unsold items status");
			System.out.println("===================================================");

			return;
		}
		CarPartsDao dao =DBManager.getCarPartsDaoInstance();
		do {
			List<Item> unsoldItemsList =  unsoldItems.getList();
			for (Item item : unsoldItemsList) {
				dao.updateListedItem(item.getListingId(), item.getBidderAndWatchers(), item.getMaxBidAmount(), item.getViewCount());
			}
			page++;
			unsoldItems = trademe.getUnsoldItems(page);
		} while(!unsoldItems.getList().isEmpty());
		
		
	}

	
	
	public static void main(String args[]){
		StockLevelChecker checker = new StockLevelChecker();
checker.updatePrestaStockLevels();
checker.checkItemsStockLevels();
	}
}
