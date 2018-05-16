package com.as.app.trademe.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import com.as.app.beans.Car;
import com.as.app.beans.Model;
import com.as.app.beans.Part;
import com.as.app.price.util.ActivePart;
import com.as.app.price.util.TrademePriceCalculator;
import com.as.app.trademe.beans.Category;
import com.as.app.trademe.beans.Items;
import com.as.app.trademe.beans.ListingDuration;
import com.as.app.trademe.beans.ListingRequest;
import com.as.app.trademe.beans.ListingResponse;
import com.as.app.trademe.beans.Listings;
import com.as.app.trademe.beans.PaymentMethod;
import com.as.app.trademe.beans.PhotoResponse;
import com.as.app.trademe.beans.PhotoUploadRequest;
import com.as.app.trademe.beans.Pickup;
import com.as.app.trademe.beans.ShippingOption;
import com.as.app.trademe.beans.ShippingType;
import com.as.app.trademe.beans.SoldItems;
import com.as.app.trademe.beans.UnsoldItems;
import com.as.app.trademe.beans.Watchlist;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.HMAC_SHA1;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;

public class TrademeApi {
	//** TRADEME KEYS
//	private static final String CONSUMER_KEY_VALUE = "00DBC68B820587221343ADF60AAB574997";
//	private static final String CONSUMER_SECRET_VALUE = "BABFAFF60D466E37B8100B958F30348D62";
	
	//access token response: oauth_token=700D797C20A508BBFBF5247DC250CCB876&oauth_token_secret=FB217D3FFEEC4F2EA5ECA91CAB6E41FAB0
//	private static final String OAUTH_TOKEN_VALUE = "04C81CC7402923C869E6F622353A093891";
//	private static final String OAUTH_TOKEN_SECRET = "8EAC94B9A7AB89C9D83CF3C64506DE9754";
	

	
	//*SANDBOX KEYS
//	private static final String CONSUMER_KEY_VALUE = "18AEA05EC53031834E7E32F05A52EA9318";
//	private static final String CONSUMER_SECRET_VALUE = "8C3A4979DBDC01653DD99448C58CBACF6C";
//
//	//access token response: oauth_token=7D43ED4A32E6D1674F72910D209994E2D0&oauth_token_secret=6FBAF5FD29DC689AFE716DD220508173CC
//	private static final String OAUTH_TOKEN_VALUE = "7D43ED4A32E6D1674F72910D209994E2D0";
//	private static final String OAUTH_TOKEN_SECRET = "6FBAF5FD29DC689AFE716DD220508173CC";
	
	
	private String DOMAIN;// = "tmsandbox"; //trademe
	private String UPLOAD_PHOTO_URI = "v1/Photos.xml";
	private String LIST_ITEM = "v1/Selling.xml";
	private String RETRIEVE_LISTED_ITEMS = "v1/MyTradeMe/SellingItems/All.xml";
	private String RETRIEVE_LISTED_ITEMS_UNANSWERED_QUESTIONS = "v1/MyTradeMe/SellingItems/UnansweredQuestions.xml";
	private String DURATION = "Last48Hours";
	private String RETRIEVE_SOLD_ITEMS_24HRS = "v1/MyTradeMe/SoldItems/" + DURATION + ".xml";
	private String RETRIEVE_UNSOLD_ITEMS_24HRS = "v1/MyTradeMe/UnsoldItems/" + DURATION + ".xml";
	private String RETRIEVE_WATCHLIST_ALL = "v1/MyTradeMe/Watchlist/All.xml";
	private String RETRIEVE_WATCHLIST_RESERVE_MET = "v1/MyTradeMe/Watchlist/ReserveMet.xml";
	private String RETRIEVE_LOST_ITEMS_24HRS = "v1/MyTradeMe/Lost/Last24Hrs.xml";
	private String RETRIEVE_LOST_ITEMS_7DAYS = "v1/MyTradeMe/Lost/Last7Days.xml";
	private String RETRIEVE_LOST_ITEMS_ALL = "v1/MyTradeMe/Lost/All.xml";
	
//	private static final String IMAGES_DIR = "images/";
	private static final String IMAGES_DIR = "C:\\dev\\temp\\";
	private Client client;
	private TrademeConfig config;
	
	public TrademeApi(String domain) {
		this.config = new TrademeConfig(domain);
		this.client = Client.create();
		this.DOMAIN = this.config.getDomain();
	}
	
	
	
	public SoldItems getSoldItems(int page) {
		URI uri = null;
		try {
			uri = new URI(DOMAIN + RETRIEVE_SOLD_ITEMS_24HRS);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final WebResource webResource = createAuthorizationHeader(uri);		 
	    
	    final ClientResponse response =  webResource.queryParam("page", String.valueOf(page)).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
//	    System.out.println("get sold items response: " + response);	    
		
		final SoldItems entity = response.getEntity(SoldItems.class);
		
//		System.out.println(entity.getTotalCount());
	    
		return entity;
	}

	public UnsoldItems getUnsoldItems(int page) {
		URI uri = null;
		try {
			uri = new URI(DOMAIN + RETRIEVE_UNSOLD_ITEMS_24HRS);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final WebResource webResource = createAuthorizationHeader(uri);		 
	    
	    final ClientResponse response =  webResource.queryParam("page", String.valueOf(page)).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
	    
	  //  System.out.println("get unsold items response: " + response);	    
		
		final UnsoldItems entity = response.getEntity(UnsoldItems.class);
		
	//	System.out.println(entity.getTotalCount());
	    
		return entity;
	}
	
	public Items getListedItems(int page) {

		URI uri = null;
		try {
			uri = new URI(DOMAIN + RETRIEVE_LISTED_ITEMS);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final WebResource webResource = createAuthorizationHeader(uri);		 
	    
//	    final ClientResponse response =  webResource.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
	    
	    final ClientResponse response =  webResource.queryParam("page", String.valueOf(page)).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
//	    System.out.println("get listed items response: " + response);	    
		
		final Items entity = response.getEntity(Items.class);
		
//		System.out.println(entity.getTotalCount());
	    
		return entity;

	}
	
	public Items getListedItemsWithUnansweredQuestions(int page) {

		URI uri = null;
		try {
			uri = new URI(DOMAIN + RETRIEVE_LISTED_ITEMS_UNANSWERED_QUESTIONS);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final WebResource webResource = createAuthorizationHeader(uri);		 
	    
//	    final ClientResponse response =  webResource.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
	    
	    final ClientResponse response =  webResource.queryParam("page", String.valueOf(page)).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
//	    System.out.println("get listed items response: " + response);	    
		
		final Items entity = response.getEntity(Items.class);
		
//		System.out.println(entity.getTotalCount());
	    
		return entity;

	}

	
	public PhotoUploadRequest generatePhotoUploadRequest(String imgName) {
		PhotoUploadRequest photoPostData = new PhotoUploadRequest();
		photoPostData.setWaterMarked(false);
		photoPostData.setUsernameAdded(false);
		
		photoPostData.setFileName(imgName);
		photoPostData.setFileType("JPG");
		
		//TODO: where to get image?
		final File imgFile = new File(IMAGES_DIR + imgName); 
		final String imgStr = convertImageToBase64(imgFile);		
		photoPostData.setPhotoData(imgStr);
//		System.out.println("photodata: " + imgStr);
		
		return photoPostData;
	
	}
	
	public ListingRequest generateListRequest(final Car car, final Part part, final int photoId, TrademePriceCalculator calc, String partNameToUse, ListingDuration duration) {

		String categoryNissanExterior = "0001-0877-2780-2784";
		String categoryMitsExterior = "0001-0877-2773-2777";
		String categoryToyotaExterior = "0001-0877-2794-2798";
		
		String category = "";
		
		if(car.getMake().trim().equalsIgnoreCase("nissan")) {
			category = categoryNissanExterior;
		} else if (car.getMake().trim().equalsIgnoreCase("mitsubishi")) {
			category = categoryMitsExterior;
		} else if (car.getMake().trim().equalsIgnoreCase("toyota")) {
			category = categoryToyotaExterior;
		}
		
		// only the first word in the model name will be used in the title (title limit is only 50 characters
//		String[] split = car.getModel().split(" ");

		Model model = car.getModel();
		
//		String partName = "";
		
		boolean hasGroup = null != part.getPartGroup() && !part.getPartGroup().getGroupName().trim().isEmpty();
//		if (hasGroup) {
//			partName = part.getPartGroup().getGroupName();
//		} else {
//			partName = part.getPartName();
//		}
		String subname = model.getSubName().isEmpty() ? "" : model.getSubName();
		String title = partNameToUse + " - " + car.getMake() + " " + model.getName() + " " + subname;
		if (title.length() > 50){
			title = title.substring(0, 49);
		}
		
		
		
		/****
		 Part: 		Headlight, LH 26-41
Make: 		Toyota
Model/Year:	Hiace ZL/R100 Series1990 (1990-2003)
Source:		Aftermarket
Delivery:	FREE thoughout New Zealand   (GREAT price!)

Please note that there are often some variations in the style of these parts for each model over time - please refer to the included photograph to determine whether it matches your requirements. 
		 */
		String paragraph1 = "Part: " + partNameToUse;
		
		if (null != part.getDetails() && !part.getDetails().isEmpty()) {
			paragraph1 += "  (" +part.getDetails() + ")";
		}
		
		
		String paragraph2 = "Make: " + car.getMake();
		String paragraph3 = "Model/Year: " + model.getName() + " " + model.getSubName() + ", " + car.getYear();
		String paragraph4 = "Type: " + part.getSource();
		//String paragraph5 = "Delivery: FREE thoughout New Zealand   (GREAT price!) ";
		String genericParagraph = "\nPlease note that there are often some variations in the style of these parts for each model over time - please refer to the included photograph to determine whether it matches your requirements.";
		String otherPartsParagraph = "\nWe have many other types of parts available for this and other models of vehicle. Let us know what you need and we can create a listing for you with the appropriate item(s):\n " +  
		    " * Radiators \n" +
		    " * Lights \n" +
		    " * Bumper Covers \n" +
		    " * Mirrors \n" +
		    " * Panels \n" +
		    " * Car Mats \n" +
		    " * Washer Bottle \n" +
		    "etc. ";
		
		ArrayList<String> desc = new ArrayList<String> ();
		desc.add(paragraph1);
		desc.add(paragraph2);
		desc.add(paragraph3);
		desc.add(paragraph4);
		if (hasGroup && partNameToUse.equals(part.getPartGroup().getGroupName())) {
			String paragraph5 = "\nPlease specify whether you would like the right or the left side after winning the auction.";
			desc.add(paragraph5);
		}
		desc.add(genericParagraph);
		desc.add(otherPartsParagraph);
		
//		float sellingPrice = calculateItemPrice(part.getWholesalePrice(), part.getRetailPrice());
		
		ListingRequest listReq = new ListingRequest();
		listReq.setCategory(category);
		listReq.setTitle(title);

		listReq.setDescription(desc);
		
		listReq.setStartPrice(calc.getAppPrice());
		listReq.setReservePrice(calc.getAppPrice());
		listReq.setBuyNowPrice(calc.getAppPrice());
		
		listReq.setDuration(duration);
//		listReq.setDuration(ListingDuration.Seven);
		listReq.setPhotoIds(new ArrayList<Integer>(Arrays.asList(new Integer(photoId))));		
		listReq.setBrandNew(true);
		listReq.setPickUp(Pickup.Forbid);
		
		
		/*TODO:
		 * Auckland 7
		 * North Island: 10
		 * South Island - 15
		 */

		ArrayList<ShippingOption> shippingOptions = new ArrayList<ShippingOption>();
		ShippingOption shippingOption = new ShippingOption(ShippingType.Custom);
		shippingOption.setMethod("Auckland");
		shippingOption.setPrice(8);
		shippingOption.setShippingId(1);		
		shippingOptions.add(shippingOption);		
		
		ShippingOption shippingOption1 = new ShippingOption(ShippingType.Custom);
		shippingOption1.setMethod("North Island");
		shippingOption1.setPrice(10);
		shippingOption1.setShippingId(2);		
		shippingOptions.add(shippingOption1);

		ShippingOption shippingOption2 = new ShippingOption(ShippingType.Custom);
		shippingOption2.setMethod("South Island");
		shippingOption2.setPrice(15);
		shippingOption2.setShippingId(2);		
		shippingOptions.add(shippingOption2);

/*		
		ShippingOption shippingOption = new ShippingOption(ShippingType.Custom);
		shippingOption.setMethod("Auckland");
		shippingOption.setPrice(15);
		shippingOption.setShippingId(1);		
		shippingOptions.add(shippingOption);		
		
		ShippingOption shippingOption1 = new ShippingOption(ShippingType.Custom);
		shippingOption1.setMethod("North Island");
		shippingOption1.setPrice(20);
		shippingOption1.setShippingId(2);		
		shippingOptions.add(shippingOption1);

		ShippingOption shippingOption2 = new ShippingOption(ShippingType.Custom);
		shippingOption2.setMethod("South Island");
		shippingOption2.setPrice(30);
		shippingOption2.setShippingId(2);		
		shippingOptions.add(shippingOption2);
*/
		
		listReq.setShippingOptions(shippingOptions);
		
		listReq.setPaymentMethods(new ArrayList<PaymentMethod>(Arrays.asList(PaymentMethod.BankDeposit)));
		listReq.setSendPaymentInstructions(true);

		System.out.println(part.getPartNumber()+ " > TITLE: " + title + ", RETAIL: " + part.getRetailPrice() + ", SELLING: " + calc.getAppPrice());
		
		return listReq;
	}

	public long listAnItem(final ListingRequest request) {
		URI uri = null;
		try {
			uri = new URI(DOMAIN + LIST_ITEM);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final WebResource webResource = createAuthorizationHeader(uri);
		
//	System.out.println("webResource: " + webResource.toString());

	    final ClientResponse response =  webResource.accept(MediaType.APPLICATION_XML).type(MediaType.APPLICATION_XML).post(ClientResponse.class, request);

		//System.out.println("list an item response: " + response);
	    
		
		final ListingResponse entity = response.getEntity(ListingResponse.class);
		
//		System.out.println(entity.getListingId());
//		System.out.println(entity.getDescription());
//		System.out.println(entity.isSuccess());
		
		if(entity.isSuccess()){
			return entity.getListingId();
		} else {
			System.out.println("Listing error: " + entity.getDescription());
			return -1;
		}
	}
	
	public PhotoResponse uploadPhoto(PhotoUploadRequest photoData) {
		
		URI uri = null;
		try {
			uri = new URI(DOMAIN + UPLOAD_PHOTO_URI);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final WebResource webResource = createAuthorizationHeader(uri);
		
//		System.out.println("webResource: " + webResource.toString());

	    final ClientResponse response =  webResource.accept(MediaType.APPLICATION_XML).type(MediaType.APPLICATION_XML).post(ClientResponse.class, photoData);

//		System.out.println("upload photo response: " + response);
	    
		
		final PhotoResponse entity = response.getEntity(PhotoResponse.class);
		
//		System.out.println(entity.getPhotoId());
//		System.out.println(entity.getStatus());
//		System.out.println(entity.getDescription());
		
	    return entity;
	}
	
	public Watchlist getWatchlist(String filter, int page) {
		String getWatchlistUri = "";
		if (filter.equalsIgnoreCase("All")) {
			getWatchlistUri = RETRIEVE_WATCHLIST_ALL;
		} else if (filter.equalsIgnoreCase("ReserveMet")){
			getWatchlistUri = RETRIEVE_WATCHLIST_RESERVE_MET;
		}
		
		URI uri = null;
		try {
			uri = new URI(DOMAIN + getWatchlistUri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final WebResource webResource = createAuthorizationHeader(uri);		 
	    final ClientResponse response =  webResource.queryParam("page", String.valueOf(page)).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
	    System.out.println("get lost items response: " + response);	    
		
		final Watchlist entity = response.getEntity(Watchlist.class);
		
		System.out.println(entity.getTotalCount());
	    
		return entity;

	}
	
	public Listings getLostItems(String filter, int page) {
		String getLostItemstUri = "";
		if (filter.equalsIgnoreCase("24Hrs")) {
			getLostItemstUri = RETRIEVE_LOST_ITEMS_24HRS;
		} else if (filter.equalsIgnoreCase("7Days")){
			getLostItemstUri = RETRIEVE_LOST_ITEMS_7DAYS;
		} else if (filter.equalsIgnoreCase("All")){
			getLostItemstUri = RETRIEVE_LOST_ITEMS_ALL;
		}
		
		URI uri = null;
		try {
			uri = new URI(DOMAIN + getLostItemstUri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final WebResource webResource = createAuthorizationHeader(uri);		 
	    final ClientResponse response =  webResource.queryParam("page", String.valueOf(page)).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
//	    System.out.println("get lost items response: " + response);	    
		
		final Listings entity = response.getEntity(Listings.class);
		
//		System.out.println(entity.getTotalCount() + " page:" + entity.getPage());
	    
		return entity;

	}
	
	private WebResource createAuthorizationHeader(URI uri) {		
		final String signature = EncryptionUtil.encryptToSHA1(config.getConsumerSecretValue() + "&" + config.getOauthTokenSecret());
		// baseline OAuth parameters for access to resource
		final OAuthParameters params = new OAuthParameters()
			.consumerKey(config.getConsumerKeyValue())
			.token(config.getOauthTokenValue())
			.version()
			.timestamp()
			.nonce()
			.signatureMethod(HMAC_SHA1.NAME)
			.signature(signature);
		 
		// OAuth secrets to access resource
		OAuthSecrets secrets = new OAuthSecrets().consumerSecret(config.getConsumerSecretValue()).tokenSecret(config.getOauthTokenSecret());
		 
		// if parameters and secrets remain static, filter can be added to each web resource
		OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), params, secrets);
		 		 
		WebResource webResource = client.resource(uri);
		// filter added at the web resource level
		webResource.addFilter(filter);
		return webResource;
	}	

	private HashMap<String, String> extractParamValue(String response) {
		HashMap<String, String> paramValueMap = new HashMap<String, String>(); 
		String[] parameters = response.split("&");
		for (String param : parameters) {
			String[] paramValuePair = param.split("=");
			paramValueMap.put(paramValuePair[0], paramValuePair[1]); break;
		}
		
		return paramValueMap;
	}
	
	public void getCategories() {
		
		Client client = Client.create();
		
		WebResource webResource = client.resource("http://api.trademe.co.nz/v1/Categories/0001-0026-1255-.xml");
		String response = webResource.get(String.class);
		System.out.println(response);
		
		//test		
		String t = "<Category xmlns=\"http://api.trademe.co.nz/v1\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><Name>Classic &amp; vintage</Name><Number>0001-0026-1255-2913-</Number><Path>tt</Path></Category>";
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(Category.class);
			XMLInputFactory xif = XMLInputFactory.newFactory();        
			XMLStreamReader xsr = xif.createXMLStreamReader(new ByteArrayInputStream(response.getBytes()));
			xsr = new StreamReaderDelegate(xsr);// {
				
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Category category = (Category) unmarshaller.unmarshal(xsr);
			System.out.println(category.getName() + " " + category.getNumber()  + " " + category.getPath()  + " " + category.getSubcategories() );
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	private String convertImageToBase64(File imgFile) {
		String encodedImage = "";

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedImage img = ImageIO.read(imgFile);    
			ImageIO.write(img, "jpg", baos);
			baos.flush();
			encodedImage = new String(Base64.encode(baos.toByteArray()));
			baos.close(); // should be inside a finally block			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		return encodedImage;
	}
}
