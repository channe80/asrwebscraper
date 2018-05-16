package com.as.app.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.as.app.checker.StockLevelChecker;
import com.as.app.lister.ASItemLister;
import com.as.app.webscraper.ASWebScraper;
import com.sun.jersey.api.client.ClientResponse.Status;
@Path("/hello")
public class ASAutoPartsResource {

	  @Context
	  UriInfo uriInfo;
	  @Context
	  Request request;
	  String id;
	  
	  public ASAutoPartsResource(UriInfo uriInfo, Request request, String id) {
	    this.uriInfo = uriInfo;
	    this.request = request;
	    this.id = id;
	  }

	  @GET
	  @Produces(MediaType.TEXT_PLAIN)
	  public String sayPlainTextHello() {
	    return "Hello Jersey";
	  }	  

//	  @PUT
//	  @Consumes(MediaType.TEXT_PLAIN)
//	  public Response scrape(String wholesaler) {
//		  ASWebScraper scraper = new ASWebScraper();
//		  scraper.scrapeAll(wholesaler);
//
//		  Response res = Response.status(Status.ACCEPTED).build();
//		  
//		  return res;
//	  }
//	  
//	  @PUT
//	  @Consumes(MediaType.TEXT_PLAIN)
//	  public Response list(String site) {
//		  ASItemLister lister = new ASItemLister();
//			lister.listActiveItems(site);
//			
//			Response res = Response.status(Status.ACCEPTED).build();
//		  
//		  return res;
//	  }
//	  
//	  @PUT
//	  @Consumes(MediaType.TEXT_PLAIN)
//	  public Response check() {
//		  StockLevelChecker checker = new StockLevelChecker();
//			checker.runChecker();
//		  
//
//		  Response res = Response.status(Status.ACCEPTED).build();
//		  
//		  return res;
//	  }

}
