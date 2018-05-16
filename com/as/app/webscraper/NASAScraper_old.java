package com.as.app.webscraper;
//
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.net.URL;
//import java.util.Iterator;
//
//import javax.imageio.ImageIO;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import com.as.app.beans.Car;
//import com.as.app.beans.Model;
//import com.as.app.beans.Part;
//import com.as.app.dao.CarPartsDao;
//
public class NASAScraper_old {
//	
//	private static final String NASA_PART_DETAILS_LINK = "http://nasaparts.co.nz/pdet.asp";
//	private static final String IMAGES_DIR = "images/";
//	private final static String SAVE_DIR = "data/";
//	
//	private enum NASAAvailableMakes {
//		
//		NISSAN("Nissan", "http://nasaparts.co.nz/nparts.asp"), //these links may change when logged in to account
//		TOYOTA("Toyota", "http://nasaparts.co.nz/tparts.asp");
//		
//		private final String link;
//		private final String makeName;
//		NASAAvailableMakes(String makeName, String link) {
//			this.link = link;	
//			this.makeName = makeName;
//		}
//		
//		private String link() {
//			return link;
//		}
//		private String makeName() {
//			return makeName;
//		}
//		
//	}
//
////	@Override
//	public Document connect() {
//		Document doc = null;
//		
//		doc = Jsoup.parse("http://nasaparts.co.nz");
//				
//		return doc;
//		
//	}
//
////	@Override
//	public String scrapeAll() {
//		
//		final CarPartsDao dao = new CarPartsDao();
//		dao.clearParts();
//		long time = System.currentTimeMillis();
//		for (NASAAvailableMakes make : NASAAvailableMakes.values()){
//				Document doc;
//				try {					
//					doc = Jsoup.connect(make.link).get();
//					FileWriter fstream = new FileWriter(SAVE_DIR + "NASA_" + make.makeName() +"_" + time + ".txt");
//					BufferedWriter out = new BufferedWriter(fstream);
//
//					
//					System.out.println(make.makeName());
////					final int carMakeId = dao.getMakeId(make.makeName());
//					//get model
//					scrapeModels(make.makeName(), doc, out);
//
//					//SAVE/CLOSE FILE
//					out.close();
//					fstream.close();
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}		
//				
//		}
//
//		return null;
//	}
//
//
//	private void scrapeModels(final String make, final Document doc, BufferedWriter out) throws IOException {
//		//<a href="plist.asp?id=117"><b>ALTEZZA SXE10</b></a>
//		Elements partsByModel = doc.select("a[href^=plist.asp?]:has(b)");
//		Iterator<Element> iterator = partsByModel.iterator();
//		CarPartsDao dao = new CarPartsDao();
//		
//		while(iterator.hasNext()) {
//			Element modelInfo = iterator.next();			
//			Element modelElement = modelInfo.child(0);	
//
//			String linkToParts = modelInfo.attr("href");
//			//				System.out.println(linkToParts);
//			String modelName = modelElement.text();
//			String year = modelInfo.nextSibling().nextSibling().toString();			
//			String id = linkToParts.substring(13);
//
//			//SAVE MODEL AND YEAR   should have a sign to know that it is a model-year
//			System.out.println("MODEL-YEAR: " + modelName + "|" + year);				
//			out.write("MODEL-YEAR: " + modelName + "|" + year +"\n");
//			
//			//TODO: database get index of car model (save new model if not in database) and pass to scrapePartsList(); - this method should return an array of parts details..
//			Model model = Model.splitModelName(modelName);
//			final Car car = new Car(make, model, year.trim());
//			int modelId = dao.getModelId(car);
//			if (modelId == -1) {				
//				modelId = dao.saveModel(car);
//			}
//				
//			scrapePartsList(modelId, id.trim(), out);
//			
//			
//		};
//
//	}
//
//
//	
//	
//	
//	private void scrapePartsList(final int modelId, final String id, BufferedWriter out) {
//		try {
//			Document doc = Jsoup.connect("http://nasaparts.co.nz/plist.asp").data("id", id).get();
//			Elements camPics = doc.select("img[src=images/campic2.gif]");
//			Iterator<Element> iterator = camPics.iterator();
//			
//			while (iterator.hasNext()) {
//				Element campic = iterator.next();
//				//TODO: for each row get id
//				Element anchorElement = campic.parent();
//				if (anchorElement.tagName().equalsIgnoreCase("a"))  {
//					String linkToDetails = anchorElement.attr("href");
//					String partId = linkToDetails.substring(12);
//					scrapePartDetails(modelId, partId.trim(), out);
//				}
//			
//			};
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void scrapePartDetails(final int modelId, final String linkId, BufferedWriter out) {
//		
//		try {
//			Document doc = Jsoup.connect(NASA_PART_DETAILS_LINK).data("id", linkId).get();
//			
//			final CarPartsDao dao = new CarPartsDao();
//			//Part Number
//			final Element partNumberElement = doc.select("td[align=right]:contains(Part Number)").get(0);
//			final String partNumber = partNumberElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();
//	
//			
//			//Part Name
//			final Element partNameElement = doc.select("td[align=right]:contains(Part Name)").get(0);
//			final String partName = partNameElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();			
//
//			
//			//Gen/Aft
//			final Element genAftElement = doc.select("td[align=right]:contains(Gen/Aft)").get(0);
//			final String genAft = genAftElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();			
//			
//			//Stock Level
//			final Element stockLevelElement = doc.select("td[align=right]:contains(Stock Level)").get(0);
//			final String stockLevelTemp = stockLevelElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();
//			final String stockLevel  = extractStockLevel(stockLevelTemp);
//
//			//Retail price
//			final Element retailPriceElement = doc.select("td[align=right]:contains(Retail Price)").get(0);
//			final String retailPriceTemp = retailPriceElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text();
////			String retailPrice = "";
////			if (!retailPriceTemp.trim().equalsIgnoreCase("call")) {
//			String retailPrice = removeText(retailPriceTemp, "(ex GST)").substring(1);			
////			}					
//
//			//TODO: Wholesale Price?
//			
//			//TODO: IMAGE
//			final Element imgElement = doc.select("img[src^=ppics/]").first();
//			final String imgName = imgElement.attr("src").trim().substring(6);
//			final String imgUrl = imgElement.absUrl("src");
//			final BufferedImage img = ImageIO.read(new URL(imgUrl));
//			final File imgFile = new File(IMAGES_DIR + imgName); 
//			ImageIO.write(img, "jpg", imgFile);
//			
//			//SAVE DETAILS
//			System.out.println(partName + "|" + partNumber +"|" + genAft +"|" + stockLevel +"|" + retailPrice + "|" + imgName);
//			out.write(partName + "|" + partNumber +"|" + genAft +"|" + stockLevel +"|" + retailPrice + "|" + imgName + "\n");
//			//carpartsDao.savePartsByModel(modelId, parts);
//			Part part = new Part();
//			part.setModelId(modelId);
//			part.setPartName(partName);
//			part.setPartNumber(partNumber);
//			part.setSource(genAft);
//			part.setStockLevel(Integer.parseInt(stockLevel));
//			part.setPhotoName(imgName);
//			part.setLinkId(linkId);
//			
//			//TODO: temp - HOW DO WE REPRESENT CALL? 
//			float retailPriceFloat = 0;
//			if (!retailPrice.equalsIgnoreCase("call")) {
//				retailPriceFloat =Float.parseFloat(retailPrice); 
//			}
//			part.setRetailPrice(retailPriceFloat);
//			
//			part.setWholesalePrice(retailPriceFloat);
//
//			//where to get wholesaler id? string or number?
//			part.setWholesalerId(1);
//	
//			
//			dao.saveParts(part);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private String extractStockLevel(String stockLevelTemp) {
//		if (stockLevelTemp.trim().equalsIgnoreCase("Out Of Stock")) {
//			return "0";
//		} else {
//			String stockLevel = removeText(stockLevelTemp.trim(), " in stock");			
//			return removeText(stockLevel.trim(), "+");
//		}
//	}
//	
//	/**
//	 * extract the digits from text i.e. $695.00 (ex GST)
//	 * @param original
//	 * @return
//	 */
//	private String removeText(String original, String target){
//		return original.trim().replace(target, "");
//	}
//	
//	
//	public static BufferedImage bufferImage(Image image) { 
//		return bufferImage(image,BufferedImage.TYPE_INT_RGB); 
//	}	
//	
//	public static BufferedImage bufferImage(Image image, int type) { 
//		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type); 
//		Graphics2D g = bufferedImage.createGraphics(); 
//		g.drawImage(image, null, null); 
//		return bufferedImage; 
//	}
//	
}

