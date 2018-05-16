package com.as.app.util;

import java.util.ArrayList;
import java.util.List;

import com.as.app.beans.ListingStatus;

public class StringUtil {

	public static <T> String toDelimitedString(List<T> arr, char delimiter) {
		StringBuilder sb = new StringBuilder();

		for(T s: arr) {
		   sb.append(s).append(delimiter);
		}

		sb.deleteCharAt(sb.length()-1); //delete last delimiter

		return sb.toString();
	}
	
	public static <T> ArrayList<String> encloseStringsWith(List<T> arr, String encloser) {
		ArrayList<String> newStrings = new ArrayList<String>();
		for(T s: arr) {
			newStrings.add(encloser + s + encloser);
			
		}
		
		return newStrings;
	}
	
	public static String removeSubstring(String mainStr, String substr)  {
		
		String[] split = mainStr.split(substr);
		
		String resultStr = split[0] + (split.length > 1 ? split[1] : "" );
		
		return resultStr;
		
	}
	
	
}
