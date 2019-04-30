package com.lexlang.SeleniumRequests.requests;

import java.io.IOException;
import java.util.Arrays;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.lexlang.SeleniumRequests.proxy.ProxyPara;
import com.lexlang.SeleniumRequests.util.PathCfg;

/**
* @author lexlang
* @version 2019年4月30日 下午2:56:51
* 
*/
public class ChromeRequests extends SeleniumRequests {
	
	static{
		String path=PathCfg.getChromePath();
		System.setProperty("webdriver.chrome.driver",path);
	}
	
	public static ChromeRequests getSelenium(){
		return new ChromeRequests(TIME_OUT,null,false);
	}
	
	public static ChromeRequests getSelenium(ProxyPara proxy){
		return new ChromeRequests(TIME_OUT,proxy,false);
	}
	
	public static ChromeRequests getSelenium(int timeout){
		return new ChromeRequests(timeout,null,false);
	}
	
	public static ChromeRequests getSelenium(int timeout,ProxyPara proxy){
		return new ChromeRequests(timeout,proxy,false);
	}
	
	public static ChromeRequests getSelenium(ProxyPara proxy,int timeout){
		return new ChromeRequests(timeout,proxy,false);
	}
	
	public static ChromeRequests getHeadlessSelenium(){
		return new ChromeRequests(TIME_OUT,null,true);
	}
	
	public static ChromeRequests getHeadlessSelenium(ProxyPara proxy){
		return new ChromeRequests(TIME_OUT,proxy,true);
	}
	
	public static ChromeRequests getHeadlessSelenium(int timeout){
		return new ChromeRequests(timeout,null,true);
	}
	
	public static ChromeRequests getHeadlessSelenium(int timeout,ProxyPara proxy){
		return new ChromeRequests(timeout,proxy,true);
	}
	
	public static ChromeRequests getHeadlessSelenium(ProxyPara proxy,int timeout){
		return new ChromeRequests(timeout,proxy,true);
	}
	
	private ChromeRequests(int timeout,ProxyPara proxy,boolean headless){
	    DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
	    
	    if(headless){
		    ChromeOptions options = new ChromeOptions();
	        options.addArguments("--headless");
	        options.addArguments("--disable-gpu");
	        options.addArguments("--no-sandbox");
	        options.addArguments("--start-maximized");
	        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
	    }

	    //屏蔽测试代码
	    String[] switches = {"--ignore-certificate-errors"};
	    desiredCapabilities.setCapability("chrome.switches", Arrays.asList(switches));
	    desiredCapabilities.setCapability (CapabilityType.ACCEPT_SSL_CERTS, true);

		if(proxy!=null){ //手工指定一个代理
			setProxy(desiredCapabilities,proxy);
		}
		
	    driver = new ChromeDriver(desiredCapabilities); 
	    setTimeOut(driver,timeout);
	}
	
    public void close(){
    	super.close();
    	try {Runtime.getRuntime().exec("taskkill /f /im chrome.exe");} catch (IOException e) {}
    	try {Runtime.getRuntime().exec("taskkill /f /im chromedriver.exe");} catch (IOException e) {}
    }
	
}
