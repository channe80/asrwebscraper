package com.as.app.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import com.as.app.beans.Car;
import com.as.app.beans.Model;
import com.as.app.beans.Part;
import com.as.app.beans.Wholesaler;
import com.as.app.dao.CarPartsDao;
import com.as.app.dao.CarPartsDao.ModelSubInfo;
import com.as.app.dao.DBManager;
import com.as.app.price.util.APPWebsitePriceCalculator;
import com.as.app.price.util.TrademePriceCalculator;

public class PrestaCsvCreator {
	
	private final static String SITE_IMG_DIR = "http://localhost/prestashop_5.1/img/carparts/";
	private final static String SITE__MODEL_IMG_DIR = "http://localhost/prestashop_5.1/img/carmodels/";
	private final static String SAVE_CSV_DIR = "data/csv/";
	private BufferedWriter outCategories;


	public void createCategoriesCsv () {
		int categoryCnt = 3;
		FileWriter fstream2;
		try {
			fstream2 = new FileWriter(SAVE_CSV_DIR + "categories.csv");
			outCategories = new BufferedWriter(fstream2);		
		//	outCategories.write(categoryCnt + ";" + 1 + ";" + make.makeName() + ";" + "Home" + ";" + 0 + ";" + make.makeName() + ";;;;;;" + 1 +"\n");
			CarPartsDao dao = DBManager.getCarPartsDaoInstance();

			ArrayList<String> categories = new ArrayList<String>();
			HashMap<Integer, String> makeNames = dao.getMakeNames();
			Set<Entry<Integer, String>> entrySet = makeNames.entrySet();
			Iterator<Entry<Integer, String>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<Integer, String> next = iterator.next();
				Integer makeId = next.getKey();
				String makeName = next.getValue();
				System.out.println(makeName + ";Home"  );
				outCategories.write(categoryCnt + ";" + 1 + ";" + makeName + ";" + "Home" + ";" + 0 + ";" + makeName + ";;;;;;" + 1 +"\n");
				categoryCnt++;
				ArrayList<String> mainModelNamesByMake = dao.getMainModelNamesByMake(makeId);
				for (String mainModel : mainModelNamesByMake) {

					ArrayList<ModelSubInfo> subModelAndYearArr = dao.getSubModelNamesAndYearByMainModel(mainModel);
					if (subModelAndYearArr.size() == 1) {
						if (subModelAndYearArr.get(0).getSubname() == null || subModelAndYearArr.get(0).getSubname().trim() == "") {
							String cat = mainModel + " - " + subModelAndYearArr.get(0).getYear();
							String imgurl = SITE__MODEL_IMG_DIR + subModelAndYearArr.get(0).getPhotoname();
							outCategories.write(categoryCnt + ";" + 1 + ";" + cat + ";" + makeName + ";" + 0 + ";" + cat + ";;;;;" +imgurl+ ";" + 1 +"\n");
							categoryCnt++;
							System.out.println(cat + ";" + makeName);	
						} else  {
							outCategories.write(categoryCnt + ";" + 1 + ";" + mainModel + ";" + makeName + ";" + 0 + ";" + mainModel + ";;;;;;" + 1 +"\n");
							categoryCnt++;
							String imgurl = SITE__MODEL_IMG_DIR + subModelAndYearArr.get(0).getPhotoname();
							String cat = subModelAndYearArr.get(0).getSubname() + " - " + subModelAndYearArr.get(0).getYear();
							outCategories.write(categoryCnt + ";" + 1 + ";" + cat + ";" + mainModel + ";" + 0 + ";" + cat + ";;;;;" +imgurl+ ";" + 1 +"\n");
							categoryCnt++;
							
							System.out.println(cat + ";" + makeName);
						}

					} else {
						outCategories.write(categoryCnt + ";" + 1 + ";" + mainModel + ";" + makeName + ";" + 0 + ";" + mainModel + ";;;;;;" + 1 +"\n");
						categoryCnt++;
						System.out.println(mainModel + ";"  + makeName);
						for (ModelSubInfo submodelYear : subModelAndYearArr) {
							String cat = submodelYear.getSubname() + " - " + submodelYear.getYear();
							System.out.println(cat + ";" + mainModel);
							String imgurl = SITE__MODEL_IMG_DIR + submodelYear.getPhotoname();
							outCategories.write(categoryCnt + ";" + 1 + ";" + cat + ";" + mainModel + ";" + 0 + ";" + cat + ";;;;;" +imgurl+ ";" + 1 +"\n");
							categoryCnt++;
						}
					}
				}
			}
			
			outCategories.close();
			fstream2.close();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void createProductCsv() {

		int productCnt = 1;


		try {
			CarPartsDao dao = DBManager.getCarPartsDaoInstance();
			HashMap<Integer, Wholesaler> wholesalers = dao.getAllWholesalers();
			ArrayList<Integer> allMakeIds = dao.getAllMakeIds();
			for(Integer makeid : allMakeIds) { 
	
				HashMap<Car, ArrayList<Part>> carparts = dao.getAllPartsByMake(makeid);
				FileWriter fstream2 = new FileWriter(SAVE_CSV_DIR + makeid + "_test_products.csv");
				BufferedWriter outProducts  = new BufferedWriter(fstream2);		
				StringBuffer sb = new StringBuffer();

				Iterator<Entry<Car, ArrayList<Part>>> iterator = carparts.entrySet().iterator();			
				while (iterator.hasNext()) {
					Entry<Car, ArrayList<Part>> next = iterator.next();
					Car car = next.getKey();
					Model model = car.getModel();
//					System.out.println("Car: " + car.getMake() + " " + model.getName() + " " + model.getSubName());
					ArrayList<Part> parts = next.getValue();
					for (Part part : parts) {	
						System.out.println(part.getPartNumber());
						//					ID
						sb.append(productCnt).append(';');
						//					Active (0/1)
						sb.append(part.isActive() == true ? 1 : 0).append(';');
						//					Name *
						sb.append(part.getPartName()).append(';');
						//					Categories (x,y,z...)
						
						String categories = car.getMake() + ',' + model.getName();
						if (null != model.getSubName() || !model.getSubName().isEmpty()){							
							categories += ',' + model.getSubName();
						}	
						categories += " - " + car.getYear();
						System.out.println("Categories: " + categories);
						sb.append(categories).append(';');
						//Price tax excl. or Price tax incl.
						APPWebsitePriceCalculator calc = new APPWebsitePriceCalculator(part.getWholesalePrice(), part.getRetailPrice());
						sb.append(calc.getAppPrice()).append(';'); //TODO: create calculator for website prices
						//Tax rules ID (??)
						sb.append(1).append(';');
						//Wholesale price
						sb.append(part.getWholesalePrice()).append(';');
						//On sale (0/1)
						sb.append(0).append(';');
						//Discount amount
						sb.append(';');
						//Discount percent
						sb.append(';');
						//Discount from (yyyy-mm-dd)
						sb.append(';');
						//Discount to (yyyy-mm-dd)
						sb.append(';');
						//Reference # => wholesalercode + wholesalerPartNumber
						sb.append(wholesalers.get(part.getWholesalerId()).getCode()).append(part.getPartNumber()).append(';'); 
						//Supplier reference #						
						sb.append(part.getWholesalerId()).append(';');   
						//Supplier
						sb.append(wholesalers.get(part.getWholesalerId()).getName()).append(';');
						//Manufacturer
						sb.append(car.getMake()).append(';');
						//EAN13
						sb.append(';');
						//UPC
						sb.append(';');
						//Ecotax
						sb.append(';');
						//Weight
						sb.append(';');
						//Quantity - the website stock level will be 2 less than the actual stock level from the wholesaler
						sb.append((part.getStockLevel()-2) < 0 ?  0 : part.getStockLevel()-2).append(';');
						
						//Short description
						sb.append(car.getMake()+ " " + model.getName() + " " + model.getSubName() + " - " + car.getYear() + part.getSource()).append(" ").append(part.getPartName()).append(';');
						//Description
						sb.append(part.getSource()).append(" ").append(part.getPartName());
						if(null != part.getDetails() || !part.getDetails().isEmpty()) {
							sb.append(" ").append(part.getDetails());
						}						
						sb.append(';');
						
						//Tags (x,y,z...)
						sb.append(';');
						//Meta-title
						sb.append(';');
						//Meta-keywords
						sb.append(';');
						//Meta-description
						sb.append(';');
						//URL rewritten
						sb.append(';');
						//Text when in stock
						sb.append("In Stock").append(';');
						//Text when backorder allowed
						sb.append(';');
						//Available for order (0 = No, 1 = Yes)
						sb.append(part.isActive() == true ? 1 : 0).append(';'); 
						//Product creation date
						sb.append(';');
						//Show price (0 = No, 1 = Yes)
						sb.append(1).append(';');
						//Image URLs (x,y,z...)						
						String imgurl = SITE_IMG_DIR + part.getPhotoName();
						sb.append(imgurl).append(';');					
						//Delete existing images (0 = No, 1 = Yes)
						sb.append(';');
						//Feature(Name:Value:Position)
						sb.append(';');
						//Available online only (0 = No, 1 = Yes)
						sb.append(';');
						//Condition
						sb.append(';');
						//ID / Name of shop
						sb.append("1\n");

						productCnt ++;
					}
				}

				outProducts.write(sb.toString());

				outProducts.close();
				fstream2.close();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String args[]) {
		PrestaCsvCreator csv = new PrestaCsvCreator();
//		csv.createCategoriesCsv();
		csv.createProductCsv();
	}
}
