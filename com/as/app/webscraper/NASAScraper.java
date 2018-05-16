package com.as.app.webscraper;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.as.app.beans.Car;
import com.as.app.beans.Model;
import com.as.app.beans.Part;
import com.as.app.dao.CarPartsDao;
import com.as.app.dao.DBManager;

public class NASAScraper extends AutoPartsScraper{
	
	private static final String SITE_IMG_DIR = "http://localhost/prestashop_5.1/img/carparts/";
	private static final String NASAPARTS_DETAILS_LINK = "http://nasaparts.co.nz/pdet.asp";
//	private static final String IMAGES_DIR = "images/";
	private static final String IMAGES_DIR = "C:\\dev\\temp\\" ; //"C:\\dev\\temp\\"; 
	private static final String IMAGES_DIR_MODELS = "C:\\dev\\temp\\models\\" ; //"C:\\dev\\temp\\";
	private final static String SAVE_DIR = "data/";
	private ArrayList<PartLink> partLink = new ArrayList<PartLink>();
	
	private StringBuffer errors = new StringBuffer();
	public class PartLink {
		
		public PartLink(String partId, String linkId) {
			this.partId = partId;
			this.linkId = linkId;
		}
		private String partId;
		private String linkId;
	}
	
	
	private enum NASAAvailableMakes {
				
		TOYOTA("Toyota", "http://nasaparts.co.nz/tparts.asp"),
		NISSAN("Nissan", "http://nasaparts.co.nz/nparts.asp"), 
		MITSUBISHI("Mitsubishi", "http://nasaparts.co.nz/mparts.asp");
		
		private final String link;
		private final String makeName;
		NASAAvailableMakes(String makeName, String link) {
			this.link = link;	
			this.makeName = makeName;
		}
		
		private String link() {
			return link;
		}
		private String makeName() {
			return makeName;
		}
		
	}
	
	private Map<String, String> cookies = new HashMap<String, String>();

	@Override
	public boolean login() {
		String loginLink = "http://nasaparts.co.nz/login.asp";
		try {
			Connection connect = Jsoup.connect(loginLink).timeout(10*1000);
			Response response = connect.data("login", "autopp").data("pw", "asretail").method(Method.POST).execute();
			this.cookies = response.cookies();
			Document doc = response.parse();
			return hasMyDetails(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	private boolean hasMyDetails(Document doc) {
		Elements myDetails = doc.select("a[href=hkuser.asp]");
		if(!myDetails.isEmpty()) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean logout() {
		String logoutLink = "http://nasaparts.co.nz/logout.asp";
		try {
			Document doc = Jsoup.connect(logoutLink).timeout(10*1000).get();
			
			boolean hasMyDetails = hasMyDetails(doc);
			if (!hasMyDetails) {
				this.cookies.clear();
				return true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public void scrapeAll() {
		
		long timeStarted = System.currentTimeMillis();
		boolean login = login();
		if (!login) {
			System.out.println("Login failed.");
			return;
		}
				
		System.out.println("Login successful.");
		CarPartsDao dao = DBManager.getCarPartsDaoInstance();
//		dao.clearParts();
		for (NASAAvailableMakes make : NASAAvailableMakes.values()){

			Document doc;
			try {					
				doc = Jsoup.connect(make.link).timeout(10*1000).get();
				System.out.println(make.makeName());
				//get model
				try {
				scrapeModels(make.makeName(), doc);
				} catch(IOException e) {
					e.printStackTrace();
					errors.append("scrapeModels() - " + make.makeName()+make.link).append("\n");					
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			System.out.println("Scraping Errors: ");
			System.out.println(errors.toString());
		}
		
		boolean logout = logout();
		if(logout) {
			System.out.println("Logout successful.");
		} else {
			System.out.println("Login failed.");
		}
		
		long diff = System.currentTimeMillis() - timeStarted;
		long diffMinutes = diff / (60 * 1000);
		System.out.println("scrapeAll took " + diffMinutes + " minutes.");
	}


	private void scrapeModels(final String make, final Document doc) throws IOException{
	
		Elements partsByModel = doc.select("a[href^=plist.asp?]:has(b)");
		Iterator<Element> iterator = partsByModel.iterator();
				
		CarPartsDao dao = DBManager.getCarPartsDaoInstance();
		
		while(iterator.hasNext()) {
			Element modelInfo = iterator.next();			
			Element modelElement = modelInfo.child(0);	

			String linkToParts = modelInfo.attr("href");
			//				System.out.println(linkToParts);
			String modelName = modelElement.text();
			String year = modelInfo.nextSibling().nextSibling().toString();			
			String linkId = linkToParts.substring(13);
			String imgName = "";
			try {
			Elements modelImageElements = doc.select("a[href=plist.asp?id=" + linkId +"]:has(img)");
			if(modelImageElements != null && !modelImageElements.isEmpty()) {
				if (modelImageElements.get(0).child(0) != null) {
					Element	imgElement = modelImageElements.get(0).child(0);
					imgName = imgElement.attr("src").trim().substring(7);
					final String imgUrl = imgElement.absUrl("src");
					final BufferedImage img = ImageIO.read(new URL(imgUrl));
					final File imgFile = new File(IMAGES_DIR_MODELS + imgName); 
					ImageIO.write(img, "jpg", imgFile);
				}
			}
			} catch (IIOException e) {
				System.out.println("model image not found");
				errors.append("scrapeModels() - ").append("model image not found: " + modelName);
				
			}
			



			//SAVE MODEL AND YEAR   should have a sign to know that it is a model-year
			System.out.println("MODEL-YEAR: " + modelName + "|" + year + "|" + imgName);				
			//outProducts.write("MODEL-YEAR: " + modelName + "|" + year +"\n");
			
			
			//TODO: database get index of car model (save new model if not in database) and pass to scrapePartsList(); - this method should return an array of parts details..
			Model model = Model.splitModelName(modelName);
			model.setPhotoName(imgName);
			final Car car = new Car(make, model, year);
			int modelId = dao.getModelId(car);
//			System.out.println("modelId: " + modelId);
			if (modelId == -1) {				
				modelId = dao.saveModel(car);
			} else {
				dao.updateModel(model.getName(), model.getSubName(), model.getPhotoName(), modelId);
			}
			
			try {				
				scrapePartsList(modelId, linkId.trim());
			}catch(IOException e) {
				errors.append("scrapePartList() - ").append(" modelId: " + modelId).append(" partListLink: " + linkId).append("\n");
			}
			
			
		};

	}


	
	
	
	public void scrapePartsList(final int modelId, final String linkId) throws IOException{
		try {
			Document doc = Jsoup.connect("http://nasaparts.co.nz/plist.asp").timeout(10*1000).data("id", linkId).get();
			Elements camPics = doc.select("img[src=images/campic2.gif]");
			Iterator<Element> iterator = camPics.iterator();
			
			while (iterator.hasNext()) {
				Element campic = iterator.next();
				//TODO: for each row get id
				Element anchorElement = campic.parent();
				if (anchorElement.tagName().equalsIgnoreCase("a"))  {
					String linkToDetails = anchorElement.attr("href");
					String partId = linkToDetails.substring(12);
					try{
						scrapePartDetails(modelId, partId.trim());
					} catch(IOException e)  {
						System.out.println("Error scraping part details with linkid: " + partId);
						errors.append("scrapePartDetails() - ").append("modelid: " + modelId).append(" partLink:" + partId).append("\n");
					}
				}
			
			};

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	public void scrapePartDetails(final int modelId, final String linkId) throws IOException {
		
		try {
			Document doc = Jsoup.connect(NASAPARTS_DETAILS_LINK).timeout(10*1000).data("id", linkId).get();
			
			final CarPartsDao dao = DBManager.getCarPartsDaoInstance();
			Part part = new Part();
			
			//Part Number
			final Element partNumberElement = doc.select("td[align=right]:contains(Part Number)").get(0);
			final String partNumber = partNumberElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();
	
			
			//Part Name
			final Element partNameElement = doc.select("td[align=right]:contains(Part Name)").get(0);
			final String partName = partNameElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();			

			//Details
			final Element partDetailsElement = doc.select("td[align=right]:contains(Details)").get(0);
			final String partDetails = partDetailsElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();						
			
			
			//Gen/Aft
			final Element genAftElement = doc.select("td[align=right]:contains(Gen/Aft)").get(0);
			final String genAft = genAftElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();			
			
			//Stock Level
			final Element stockLevelElement = doc.select("td[align=right]:contains(Stock Level)").get(0);
			final String stockLevelTemp = stockLevelElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();
			final String stockLevel  = extractStockLevel(stockLevelTemp);

			//Retail price
			final Element retailPriceElement = doc.select("td[align=right]:contains(Retail Price)").get(0);
			float retailPriceFloat = 0;
			if(retailPriceElement != null) {
				final String retailPriceTemp = retailPriceElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();
				String retailPrice = removeText(retailPriceTemp, "(ex GST)").substring(1);			
				retailPrice = removeText(retailPrice, ",");
				//TODO: temp - HOW DO WE REPRESENT CALL? 				
				if (!retailPrice.equalsIgnoreCase("call")) {					
					retailPriceFloat =Float.parseFloat(retailPrice); 
				} else {
					//if retail is 'call' 
					//if part exists and retail has a value
					retailPriceFloat = dao.getPartRetailPrice(partNumber);
				}
				
			}
			
			//TODO: IMAGE
			final Element imgElement = doc.select("img[src^=ppics/]").first();
			final String imgName = imgElement.attr("src").trim().substring(6);
			final String imgUrl = imgElement.absUrl("src");
			final BufferedImage img = ImageIO.read(new URL(imgUrl));
			final File imgFile = new File(IMAGES_DIR + imgName); 
			ImageIO.write(img, "jpg", imgFile);
			
			String imgurl = SITE_IMG_DIR + imgName;
			
			//get wholesaleprice
			float wholesalePrice = scrapePartWholesalePrice(partNumber, linkId);
			
			//SAVE DETAILS

//			System.out.println(partName + "|" + partNumber +"|" + partDetails + "|" + genAft +"|" + stockLevel +"|" + retailPriceFloat + "|" + wholesalePrice + "|" + imgName + " " + linkId);

			part.setModelId(modelId);
			part.setPartName(partName);
			part.setPartNumber(partNumber);
			part.setDetails(partDetails);
			part.setSource(genAft);
			part.setStockLevel(Integer.parseInt(stockLevel));
			part.setPhotoName(imgName);
			part.setLinkId(linkId);			
			part.setRetailPrice(retailPriceFloat);
			part.setWholesalePrice(wholesalePrice);
			//where to get wholesaler id? string or number?
			part.setWholesalerId(1);
	
			if (dao.isPartExist(part.getPartNumber())) {
				dao.updatePart(part);
			}else {
				dao.saveParts(part);
			}
			
			this.partLink.add(new PartLink(part.getPartNumber(), part.getLinkId()));
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private float scrapePartWholesalePrice(final String partNumber, final String linkId) throws IOException {

		try {
			if (this.cookies.isEmpty()) {
				System.out.println("No session cookies. Need to login first to get wholesale price.");
				return 0;
			}

			Connection connect = Jsoup.connect(NASAPARTS_DETAILS_LINK).timeout(10*1000).data("id", linkId);
			connect.cookies(this.cookies);
			Document doc = connect.get();

			//Wholesale Price
			final Element wholesalePriceElement = doc.select("td[align=right]:contains(Wholesale Price)").get(0);
			final String wholesalePriceTemp = wholesalePriceElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();
			String wholesalePrice = removeText(wholesalePriceTemp, "(ex GST)").substring(1);			
			wholesalePrice = removeText(wholesalePrice, ",");	
			//TODO: temp - HOW DO WE REPRESENT CALL? 
			float wholesalePriceFloat = 0;
			if (!wholesalePrice.equalsIgnoreCase("call")) {
				wholesalePriceFloat = Float.parseFloat(wholesalePrice); 
			}

			return wholesalePriceFloat;

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	public void scrapeWholeSalePrice() {
		//login
		boolean login = login();
		System.out.println("Login successful : " + login);		
		if (login) {
			for (PartLink partLink : this.partLink) {
				try {
					scrapePartWholesalePrice(partLink.partId, partLink.linkId);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		boolean logout = logout();
		System.out.println("Logout successful : " + logout);
	}

	private String extractStockLevel(String stockLevelTemp) {
		if (stockLevelTemp.trim().equalsIgnoreCase("Out Of Stock")) {
			return "0";
		} else {
			String stockLevel = removeText(stockLevelTemp.trim(), " in stock");			
			return removeText(stockLevel.trim(), "+");
		}
	}
	
	/**
	 * extract the digits from text i.e. $695.00 (ex GST)
	 * @param original
	 * @return
	 */
	private String removeText(String original, String target){
		return original.trim().replace(target, "");
	}
	
	
	public static BufferedImage bufferImage(Image image) { 
		return bufferImage(image,BufferedImage.TYPE_INT_RGB); 
	}	
	
	public static BufferedImage bufferImage(Image image, int type) { 
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type); 
		Graphics2D g = bufferedImage.createGraphics(); 
		g.drawImage(image, null, null); 
		return bufferedImage; 
	}
	

	public static void main(String[] args) {
		//		final AutoPartsScraperFactory factory = new AutoPartsScraperFactory();
		//		final AutoPartsScraper scraper = factory.getScraper(AutoPartSource.NASA);		
		//		scraper.connect();
		NASAScraper s = new NASAScraper();
		s.login();
//		s.scrapeAll();
		try {
			s.scrapePartsList(57, "74");

		}catch(IOException e) {
			e.printStackTrace();
		}
		
		s.logout();
	}
}

