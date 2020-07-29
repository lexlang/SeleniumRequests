package com.lexlang.SeleniumRequests.util;

public class CheckOS {
	
	public static boolean isWinOS(){
		 boolean isWindowsOS = false;
	     String osName = System.getProperty("os.name");
	     if(osName.toLowerCase().indexOf("windows")>-1){
	       isWindowsOS = true;
	     }
	     return isWindowsOS;
	}
	
}
