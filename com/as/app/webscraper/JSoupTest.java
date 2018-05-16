package com.as.app.webscraper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupTest {
	
	public static void main(String args[]) {

		try {
			
			Document doc=Jsoup.connect("http://nasaparts.co.nz/plist.asp").data("id", "108").get();
			
			Elements camPics = doc.select("img[src=images/campic2.gif]");
			Iterator<Element> iterator = camPics.iterator();
			
			do {
				Element campic = iterator.next();
				
				Element parentRow = campic.parent().parent().parent();								
				System.out.println(parentRow.tagName());
				Elements columns = parentRow.getElementsByTag("td");
				System.out.println(columns);
			} while (iterator.hasNext());
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
