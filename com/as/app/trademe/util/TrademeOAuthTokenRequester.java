package com.as.app.trademe.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.HMAC_SHA1;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;

/**
 * Contains methods used for obtaining OAuthToken for all trademe request
 * @author atorres
 *
 */
public class TrademeOAuthTokenRequester {

	
	private static final String REQUEST_TOKEN_URI = "https://secure.trademe.co.nz/Oauth/RequestToken";
	private static final String TRADEME_CALLBACK = "http://www.website-tm-access.co.nz/trademe-callback";
	private static final String ACCESS_TOKEN_URI = "https://secure.trademe.co.nz/Oauth/AccessToken";
	private static final String OAUTH_TOKEN_STR = "oauth_token";
//	private static final String CONSUMER_KEY_VALUE = "00DBC68B820587221343ADF60AAB574997";
//	private static final String CONSUMER_SECRET_VALUE = "BABFAFF60D466E37B8100B958F30348D62";
	
	//asretail
	//Consumer key: E26DA18E298520A7C1067F0D02DBB2A836
	 //Consumer secret: C47CF6D6BAFF06AA70B61E87B996CACF6D
	private static final String CONSUMER_KEY_VALUE = "E26DA18E298520A7C1067F0D02DBB2A836";
	private static final String CONSUMER_SECRET_VALUE = "C47CF6D6BAFF06AA70B61E87B996CACF6D";

	
	
	//access token response: oauth_token=700D797C20A508BBFBF5247DC250CCB876&oauth_token_secret=FB217D3FFEEC4F2EA5ECA91CAB6E41FAB0
//	private static final String OAUTH_TOKEN_VALUE = "700D797C20A508BBFBF5247DC250CCB876";
//	private static final String OAUTH_TOKEN_SECRET = "FB217D3FFEEC4F2EA5ECA91CAB6E41FAB0";
	//http://www.website-tm-access.co.nz/trademe-callback?oauth_token=EC87E3EAEC63D22EA222916662085EAA8D&oauth_verifier=9D5E4CE46B2B7AD2B9F25127E1942D40A2
	private static final String OAUTH_VERIFIER_VALUE = "61070B258F62DE8075BC5A5E380E5E839E";
	
	//temp
	//request token response: oauth_token=FF41B9AF4AECE71E2D20E21BEDCEBA95A1&oauth_token_secret=7D78D8A15F7C93FFC5221868CBEEDAA01D&oauth_callback_confirmed=true
	private static final String OAUTH_TOKEN_VALUE_TEMP = "FF41B9AF4AECE71E2D20E21BEDCEBA95A1";
	private static final String OAUTH_TOKEN_SECRET_VALUE_TEMP = "7D78D8A15F7C93FFC5221868CBEEDAA01D";
	
//************SANDBOX***************
	
	
//	private static final String REQUEST_TOKEN_URI = "https://secure.tmsandbox.co.nz/Oauth/RequestToken";
//	private static final String TRADEME_CALLBACK = "http://www.website-tm-access.co.nz/trademe-callback";
//	private static final String ACCESS_TOKEN_URI = "https://secure.tmsandbox.co.nz/Oauth/AccessToken";
//	private static final String OAUTH_TOKEN_STR = "oauth_token";
//	private static final String CONSUMER_KEY_VALUE = "18AEA05EC53031834E7E32F05A52EA9318";
//	private static final String CONSUMER_SECRET_VALUE = "8C3A4979DBDC01653DD99448C58CBACF6C";
//	//access token response: oauth_token=700D797C20A508BBFBF5247DC250CCB876&oauth_token_secret=FB217D3FFEEC4F2EA5ECA91CAB6E41FAB0
////	private static final String OAUTH_TOKEN_VALUE = "700D797C20A508BBFBF5247DC250CCB876";
////	private static final String OAUTH_TOKEN_SECRET = "FB217D3FFEEC4F2EA5ECA91CAB6E41FAB0";
//	//http://www.website-tm-access.co.nz/trademe-callback?oauth_token=022806797C5E149D062CD9EEDEB284F58E&oauth_verifier=7978BD425AE111734E8CDA262DAFD0B74E
//	private static final String OAUTH_VERIFIER_VALUE = "7978BD425AE111734E8CDA262DAFD0B74E";
//	
//	//temp
//	//request token response: oauth_token=022806797C5E149D062CD9EEDEB284F58E&oauth_token_secret=FB42E719ED550E394395CF342AC5BF155A&oauth_callback_confirmed=true
//	private static final String OAUTH_TOKEN_VALUE_TEMP = "022806797C5E149D062CD9EEDEB284F58E";
//	private static final String OAUTH_TOKEN_SECRET_VALUE_TEMP = "FB42E719ED550E394395CF342AC5BF155A";
	
	
	
	/**
	 * Authorize step needs to be done manually in the browser 
	 * i.e. 	//https://secure.trademe.co.nz/Oauth/Authorize?oauth_token=86EE20AFF655C34CB34873449A5F98020B
	 * The user will be asked to login and click on Allow button.
	 * The resulting url on the browser will contain the OAuth_verifier needed for the next steps in obtaining final OAuth_token
	 * i.e. http://www.website-tm-access.co.nz/trademe-callback?oauth_token=EC87E3EAEC63D22EA222916662085EAA8D&oauth_verifier=9D5E4CE46B2B7AD2B9F25127E1942D40A2
	 * @param client
	 */
//	public HashMap<String, String> authorize(final Client client, final HashMap<String, String> oauthToken) { 
//	//https://secure.trademe.co.nz/Oauth/Authorize?oauth_token=86EE20AFF655C34CB34873449A5F98020B	
//		System.out.println("authorize with oauthtoken: " + oauthToken.get(OAUTH_TOKEN_STR));
//		final WebResource webResource = client.resource("https://secure.trademe.co.nz/Oauth/Authorize?oauth_token=" + oauthToken.get(OAUTH_TOKEN_STR));
//		//final String response = webResource.queryParam(OAUTH_TOKEN, oauthToken.get(OAUTH_TOKEN)).get(String.class);
//		final String response = webResource.get(String.class);
//		System.out.println("authorize response: " + response);
//
//		return extractParamValue(response);
//		
//		
//	}
	
	
	public HashMap<String, String> getOAuthToken(final Client client) {
		final String signature = EncryptionUtil.encryptToSHA1(CONSUMER_SECRET_VALUE + "&");
		// baseline OAuth parameters for access to resource
		final OAuthParameters params = new OAuthParameters().callback(TRADEME_CALLBACK).signatureMethod(HMAC_SHA1.NAME).signature(signature).consumerKey(CONSUMER_KEY_VALUE).timestamp().nonce().version();
		// OAuth secrets to access resource
		final OAuthSecrets secrets = new OAuthSecrets().consumerSecret(CONSUMER_SECRET_VALUE);
		// if parameters and secrets remain static, filter can be added to each web resource
		final OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), params, secrets);
		
		URI uri = null;
		try {
			uri = new URI(REQUEST_TOKEN_URI);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final WebResource webResource = client.resource(uri);
		
		
		// filter added at the web resource level
		webResource.addFilter(filter);		 
	    final String response =  webResource.queryParam("scope", "MyTradeMeRead,MyTradeMeWrite,BiddingAndBuying").post(String.class, "");
	    System.out.println("request token response: " + response);

	    return extractParamValue(response);
		
	}

	public HashMap<String, String> getAcesssToken(final Client client) {
		final String oauthTokenTemp = OAUTH_TOKEN_VALUE_TEMP;
		final String signature = EncryptionUtil.encryptToSHA1(CONSUMER_SECRET_VALUE + "&" + OAUTH_TOKEN_SECRET_VALUE_TEMP);

		// baseline OAuth parameters for access to resource
		OAuthParameters params = new OAuthParameters()
			.verifier(OAUTH_VERIFIER_VALUE)
			.consumerKey(CONSUMER_KEY_VALUE)
			.token(oauthTokenTemp)
			.version()
			.timestamp()
			.nonce()
			.signatureMethod(HMAC_SHA1.NAME)
			.signature(signature);
		 
		// OAuth secrets to access resource
		OAuthSecrets secrets = new OAuthSecrets().consumerSecret(CONSUMER_SECRET_VALUE).tokenSecret(OAUTH_TOKEN_SECRET_VALUE_TEMP);
		 
		// if parameters and secrets remain static, filter can be added to each web resource
		OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), params, secrets);
		 
		WebResource webResource = null;
		URI uri = null;
		try {
			uri = new URI(ACCESS_TOKEN_URI);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webResource = client.resource(uri);
		 
		// filter added at the web resource level
		webResource.addFilter(filter);

		
		System.out.println(webResource.toString());
	    final String response =  webResource.post(String.class, "");
	    System.out.println("access token response: " + response);

	    return extractParamValue(response);
		
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
	
	
	
	public static void main(String args[]) {
		final Client client = Client.create();
		TrademeOAuthTokenRequester oauthRequester = new TrademeOAuthTokenRequester();
		
		//SET UP application at My Trade Me > My applications > Developer options 
		//to get CONSUMER KEY AND CONSUMER SECRET - assign to variables CONSUMER_KEY_VALUE and CONSUMER_SECRET_VALUE
		
		//STEP 1: get temporary OAUTH TOKEN and OAUTH TOKEN SECRET
//		final HashMap<String, String> oauthToken = oauthRequester.getOAuthToken(client);
		//result:request token response: oauth_token=943F5F61F4CAA4CB5CF5944A95309C25E9&oauth_token_secret=CE16022308C13A338320D65C543D3F6920&oauth_callback_confirmed=true
		//request token response: oauth_token=FF41B9AF4AECE71E2D20E21BEDCEBA95A1&oauth_token_secret=7D78D8A15F7C93FFC5221868CBEEDAA01D&oauth_callback_confirmed=true
		
		//STEP 2: authorize
//		oauthRequester.authorize(client, oauthToken);
		
		// Authorize step needs to be done manually in the browser 
		// 1. //https://secure.trademe.co.nz/Oauth/Authorize?oauth_token=022806797C5E149D062CD9EEDEB284F58E 
		//	 
		// 2. The user will be asked to login and click on Allow button.
		// 3. The resulting url on the browser will contain the OAuth_verifier needed for the next steps in obtaining final OAuth_token
		//  	i.e. http://www.website-tm-access.co.nz/trademe-callback?oauth_token=FF41B9AF4AECE71E2D20E21BEDCEBA95A1&oauth_verifier=61070B258F62DE8075BC5A5E380E5E839E
		// 4. Take note of oauth_verifier for the next step - assign value to constant OAUTH_VERIFIER_VALUE

		//STEP 3: get final oath token
		oauthRequester.getAcesssToken(client);	
		//access token response: oauth_token=B9A6884BDC55FBE060E0C2D984E11497D1&oauth_token_secret=E221C81930CE91EFB701D9F8B690DE40C4
	}

}
