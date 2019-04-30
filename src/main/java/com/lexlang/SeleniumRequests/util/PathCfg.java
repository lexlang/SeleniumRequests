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
	private static String chromePath;
	private static String iePath;
	private static String phantomjsPath;
	
	static{
		Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream("path.properties"));
            pps.load(in);
            in.close();
            chromePath=pps.getProperty("chromePath");
            iePath=pps.getProperty("iePath");
            phantomjsPath=pps.getProperty("phantomjsPath");
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	public static String getChromePath(){
		return chromePath;
	}
	
	public static String getIEPath(){
		return iePath;
	}
	
	public static String getPhantomjsPath(){
		return phantomjsPath;
	}
	
}
