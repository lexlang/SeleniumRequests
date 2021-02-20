package com.lexlang.SeleniumRequests.requests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.lexlang.SeleniumRequests.proxy.ProxyPara;
import com.lexlang.SeleniumRequests.util.PathCfg;

public class FirefoxRequests extends SeleniumRequests{
	
	static{
		String path=PathCfg.getIEPath();
		System.setProperty("webdriver.gecko.driver",path);
	}
	
	public static SeleniumRequests getSelenium(){
		return new FirefoxRequests(TIME_OUT,null,null,false);
	}
	
	public static SeleniumRequests getSelenium(ProxyPara proxy){
		return new FirefoxRequests(TIME_OUT,proxy,null,false);
	}
	
	public static SeleniumRequests getSelenium(int timeout){
		return new FirefoxRequests(timeout,null,null,false);
	}
	
	public static SeleniumRequests getSelenium(int timeout,ProxyPara proxy){
		return new FirefoxRequests(timeout,proxy,null,false);
	}
	
	public static SeleniumRequests getSelenium(ProxyPara proxy,int timeout){
		return new FirefoxRequests(timeout,proxy,null,false);
	}
	
	private FirefoxRequests(int timeout,ProxyPara proxy,String userAgent,boolean blankImage){
		   FirefoxOptions options = new FirefoxOptions();
	       //禁止GPU渲染
	       options.addArguments("--disable-gpu");
	       //忽略错误
	       options.addArguments("ignore-certificate-errors");
	       //禁止浏览器被自动化的提示
	       options.addArguments("--disable-infobars");
	       //找了十几天的一句代码，反爬关键：window.navigator.webdrive值=false*********************
	       options.addPreference("dom.webdriver.enabled", false);
	       if(blankImage){
	    	 //禁止加载图片
	    	   options.addPreference("permissions.default.image", 2);
	       }
	       if(userAgent!=null){
		       //设置浏览器
		       options.addPreference("general.useragent.override", userAgent);
	       }
	       if(proxy!=null){
		       //本地代理
	    	   options.addArguments("--proxy--server="+proxy.getHost()+":"+proxy.getPort()); 
	       }
		   driver = new FirefoxDriver(options);
	}
	
}
