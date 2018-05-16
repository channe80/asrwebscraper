package com.as.app.lister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.as.app.ASAdmin;
import com.as.app.beans.Car;
import com.as.app.beans.ListingStatus;
import com.as.app.beans.Part;
import com.as.app.beans.PartGroup;
import com.as.app.dao.CarPartsDao;
import com.as.app.dao.DBManager;
import com.as.app.price.util.ActivePart;
import com.as.app.price.util.TrademePriceCalculator;
import com.as.app.trademe.beans.ListingDuration;
import com.as.app.trademe.beans.ListingRequest;
import com.as.app.trademe.beans.PhotoResponse;
import com.as.app.trademe.beans.PhotoUploadRequest;
import com.as.app.trademe.util.TrademeApi;
import com.as.app.util.DataUtil;

public class TrademeItemLister extends ItemLister {


	@Override
	/**
	 * List all active parts that that have stock level > 2 and is not listed or sold but not ordered
	 */
	public void listActiveItems() {		
		CarPartsDao dao = DBManager.getCarPartsDaoInstance(); 
		HashMap<Car, ArrayList<Part>> carparts = dao.getRelistableActiveParts();
		HashMap<String, Integer> photoIds = dao.getPhotoIds();		
		listItems(carparts, photoIds, ListingDuration.Four);	
	}

	private void listItems(final HashMap<Car, ArrayList<Part>> carparts, HashMap<String, Integer> photoIds, ListingDuration duration) {
		TrademeApi trademe = new TrademeApi(ASAdmin.TRADEME_DOMAIN);
		Iterator<Entry<Car, ArrayList<Part>>> iterator = carparts.entrySet().iterator();		
		CarPartsDao dao = DBManager.getCarPartsDaoInstance();
		HashMap<Integer, Long> groupListed = new HashMap<Integer, Long>();
		ArrayList<Integer> lowStockGroups = dao.getGroupWithLowStock();
		int itemsListed = 0;
		while (iterator.hasNext()) {
			Entry<Car, ArrayList<Part>> next = iterator.next();				
			Car car = next.getKey();
			ArrayList<Part> parts = next.getValue();
			for (Part part : parts) {
				PartGroup partGroup = part.getPartGroup();
//				System.out.println("Listing item: " + part.getPartNumber());
				//check if part group has been listed
				if (null == partGroup || (null != partGroup && !groupListed.containsKey(new Integer(partGroup.getGroupId())))) {
					int photoId =  0;
					if (photoIds.containsKey(part.getPartNumber())) {
						photoId = photoIds.get(part.getPartNumber());
					} else {	
						PhotoUploadRequest photoUploadReq = trademe.generatePhotoUploadRequest(part.getPhotoName());
						PhotoResponse photoResponse = trademe.uploadPhoto(photoUploadReq);
						photoId = photoResponse.getPhotoId();
						//save
						dao.savePhotoId(part.getPartNumber(), photoId);
					}

					//get app price
					TrademePriceCalculator calc = new TrademePriceCalculator(part.getWholesalePrice(), part.getRetailPrice());
					String partname = "";
					//TODO: check which part name to use - groupname or partname
					//if hasGroup then check if partner is in low stock list					
					if (partGroup != null) { 
					//	if group is in low stock list then partname is partname
						if(lowStockGroups.contains(partGroup.getGroupId())) {
							partname = part.getPartName();
						} else {
					//	else partname is groupname
							partname = partGroup.getGroupName();
						}	
					} else  {	
					//if no group then partname is partname
						partname = part.getPartName();
					}
					ListingRequest listRequest = trademe.generateListRequest(car, part, photoId, calc, partname, duration);
					long listingId = trademe.listAnItem(listRequest);
					System.out.println("Listing ID: " + listingId);
					if (listingId != -1) {
						//save to listedItems						
						dao.saveListedItem(part.getPartNumber(), listingId, ListingStatus.LISTED);
						//save partners?
						itemsListed ++;
												
						if (partGroup != null)  {
							groupListed.put(partGroup.getGroupId(), listingId);
						}
					}

				} else if (groupListed.containsKey(new Integer(partGroup.getGroupId()))) {
					//save partnumber as listed under same listingid (for stock level checker)
					Long listingId = groupListed.get(new Integer(partGroup.getGroupId()));
					dao.saveListedItem(part.getPartNumber(), listingId, ListingStatus.LISTED);
					System.out.println(part.getPartNumber() + " saved as listed in Listing Id: " + listingId);
				}

			}
		}
		
		System.out.println("=========================================================");
		System.out.println("FINISHED LISTING " + itemsListed + " ITEMS" );
		System.out.println("=========================================================");
		

	}

	public void listActiveItems2() {		
		CarPartsDao dao = DBManager.getCarPartsDaoInstance(); 
		HashMap<Car, ArrayList<Part>> carparts = dao.getRelistableActiveParts2();
		HashMap<String, Integer> photoIds = dao.getPhotoIds();		
		listItems(carparts, photoIds, ListingDuration.Seven);	
	}
	
	public static void main(String args[]) {
		TrademeItemLister l = new TrademeItemLister();
		l.listActiveItems();
	} 

}
