package com.as.app.trademe.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.as.app.trademe.beans.Item;
import com.as.app.trademe.beans.Listings;
import com.as.app.trademe.beans.Watchlist;


public class TrademeWatchlist {

	public static void main(String[] args){		

		try {
			getLostItems();

			//WATCHLIST
//			getWatchlist();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void getWatchlist() throws IOException {
		TrademeApi trademe = new TrademeApi("trademe");
		int page = 1;
		boolean loop = true;

		int newItems = 0;
		int total = 0;
		FileWriter fstream2 = new FileWriter("data/" + "set2_watchlist" + ".txt");
		BufferedWriter out2 = new BufferedWriter(fstream2);		
		
		do {
			Watchlist watchlist = trademe.getWatchlist("All", page);
			if (!watchlist.getList().isEmpty()) {
				System.out.println("page: " + page);
				total = watchlist.getTotalCount();
				for (Item item :watchlist.getList()) {
					System.out.println(item.getCategoryPath()  + " | " + item.getListingId() + " | isNew? " + item.isNewItem() + " | " +  item.getTitle() + " | " + item.getBuyNowPrice() + "(BuyNow) | " + item.getStartPrice() +  "(StartPrice)" );
					out2.write(item.getCategoryPath()  + " | " + item.getListingId() + " | isNew? " + item.isNewItem() + " | " +  item.getTitle() + " | " + item.getBuyNowPrice() + "(BuyNow) | " + item.getStartPrice() +  "(StartPrice)"  + "\n");
					if (item.isNewItem()) newItems ++; 
				}
			} else {
				loop = false;
			}
			page ++;
		} while (loop);
		
		System.out.println("new items: " + newItems + " total watchlist: " + total);
		out2.write("new items: " + newItems + " total watchlist: " + total + "\n");			
		out2.close();
		fstream2.close();
	}

	private static void getLostItems() throws IOException {
		
		TrademeApi trademe = new TrademeApi("trademe");
		int page = 1;
		boolean loop = true;

		int newItems = 0;
		int total = 0;
		int bought = 0;
		int notBought = 0;
		FileWriter fstream = new FileWriter("data/" + "set2_lostItems" + ".txt");
		BufferedWriter out = new BufferedWriter(fstream);		

		do {
			Listings listings = trademe.getLostItems("All", page); 
			if (!listings.getList().isEmpty()) {
				total = listings.getTotalCount();
				System.out.println("page: " + page );
				for (Item item : listings.getList())  {
					if (item.getMaxBidAmount() > 0) {
						bought ++;
						System.out.println(item.getCategoryPath()  + " | " + item.getListingId() + " | isNew?" + item.isNewItem() + " | "  + item.getTitle() + " | " + item.getMaxBidAmount() + "(BidAmount) | "  + item.getEndDate());
						out.write(item.getCategoryPath()  + " | " + item.getListingId() + " | isNew?" + item.isNewItem() + " | "  + item.getTitle() + " | " + item.getMaxBidAmount() + "(BidAmount) | "  + item.getEndDate() + "\n");
						if (item.isNewItem()) newItems ++;
					} 
					else {
											notBought ++;
//											System.out.println(***"NOT BOUGHT: " + item.getCategoryPath()  + " | " + item.getListingId() + " | "  + item.getTitle() + " | "  + item.getStartPrice()  + " | "  + item.getEndDate());
					}
				}
			} else {
				loop = false;
			}
			page++;
		} while (loop);
		System.out.println("Total BOUGHT: " + bought + "  Total NOT BOUGHT: " + notBought);
		System.out.println("New Items Bought: " + newItems + "  Old Items Bought: " + ( bought - newItems));
		out.write("Total BOUGHT: " + bought + "  Total NOT BOUGHT: " + notBought +  "\n");
		out.write("New Items Bought: " + newItems + "  Old Items Bought: " + ( bought - newItems) +  "\n");
		out.close();
		fstream.close();
	}
}
