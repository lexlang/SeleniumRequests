package com.lexlang.SeleniumRequests.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
* @author lexlang
* @version 2019年4月30日 下午1:51:38
* 
*/
public class PathCfg {
	private static String winchromePath;
	private static String iePath;
	private static String phantomjsPath;
	private static String firefoxPath;
	
	static{
		Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream("path.properties"));
            pps.load(in);
            in.close();
            winchromePath=pps.getProperty("winchromePath");
            iePath=pps.getProperty("iePath");
            phantomjsPath=pps.getProperty("phantomjsPath");
            firefoxPath=pps.getProperty("firefoxPath");
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	public static String getWinChromePath(){
		return winchromePath;
	}
	
	public static String getIEPath(){
		return iePath;
	}
	
	public static String getPhantomjsPath(){
		return phantomjsPath;
	}
	
	public static String getFirefoxPath(){
		return firefoxPath;
	}
	
	
}
