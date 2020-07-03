package com.anvilshop.bot.util;

public class MathUtil {
	public static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  catch(Exception e) {
			  return false;
		  }
		}
}