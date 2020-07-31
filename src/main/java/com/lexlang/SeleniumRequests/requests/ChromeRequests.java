package com.lexlang.SeleniumRequests.requests;

import java.io.IOException;
import java.util.Arrays;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.lexlang.SeleniumRequests.proxy.ProxyPara;
import com.lexlang.SeleniumRequests.util.CheckOS;
import com.lexlang.SeleniumRequests.util.PathCfg;

/**
* @author lexlang
* @version 2019年4月30日 下午2:56:51
* 
*/
public class ChromeRequests extends SeleniumRequests {
	
	static{
		if(CheckOS.isWinOS()){
			String path=PathCfg.getWinChromePath();
			System.setProperty("webdriver.chrome.driver",path);
		}else{
		    //linux安装环境
			//chrome安装位置
	        System.setProperty("webdriver.chrome.bin", "/opt/google/chrome/chrome");
	        //chromederiver存放位置
	        System.setProperty("webdriver.chrome.driver","/usr/bin/chromedriver");
		}
	}
	

	
	public static SeleniumRequests getSelenium(){
		return new ChromeRequests(TIME_OUT,null,false);
	}
	
	public static SeleniumRequests getSelenium(ProxyPara proxy){
		return new ChromeRequests(TIME_OUT,proxy,false);
	}
	
	public static SeleniumRequests getSelenium(int timeout){
		return new ChromeRequests(timeout,null,false);
	}
	
	public static SeleniumRequests getSelenium(int timeout,ProxyPara proxy){
		return new ChromeRequests(timeout,proxy,false);
	}
	
	public static SeleniumRequests getSelenium(ProxyPara proxy,int timeout){
		return new ChromeRequests(timeout,proxy,false);
	}
	
	public static SeleniumRequests getHeadlessSelenium(){
		return new ChromeRequests(TIME_OUT,null,true);
	}
	
	public static SeleniumRequests getHeadlessSelenium(ProxyPara proxy){
		return new ChromeRequests(TIME_OUT,proxy,true);
	}
	
	public static SeleniumRequests getHeadlessSelenium(int timeout){
		return new ChromeRequests(timeout,null,true);
	}
	
	public static SeleniumRequests getHeadlessSelenium(int timeout,ProxyPara proxy){
		return new ChromeRequests(timeout,proxy,true);
	}
	
	public static SeleniumRequests getHeadlessSelenium(ProxyPara proxy,int timeout){
		return new ChromeRequests(timeout,proxy,true);
	}
	
	private ChromeRequests(int timeout,ProxyPara proxy,boolean headless){
		
		if(CheckOS.isWinOS()){
		    DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		    
		    if(headless){
			    ChromeOptions options = new ChromeOptions();
		        options.addArguments("--headless");
		        options.addArguments("--disable-gpu");
		        options.addArguments("--no-sandbox");
		        options.addArguments("--start-maximized");
		        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
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
		}else{
			ChromeOptions options = new ChromeOptions();
			options.addArguments("headless");
			options.addArguments("no-sandbox");
			driver = new ChromeDriver(options);
		}
			    
	    setTimeOut(driver,timeout);
	}
	
    public void close(){
    	super.close();
    	if(CheckOS.isWinOS()){
    		try {Runtime.getRuntime().exec("taskkill /f /im chrome.exe");} catch (IOException e) {}
    	}else{
    		try {
    			String[] cmd = { "/bin/sh", "-c", "ps aux |grep chrome|awk '{print $2}'|xargs -i kill {}"};
    			Process p = Runtime.getRuntime().exec(cmd);
				p.waitFor();
    		} catch (IOException e) {}
    		catch (InterruptedException ex) {}
		
    	}
    	//
    	//try {Runtime.getRuntime().exec("taskkill /f /im chromedriver.exe");} catch (IOException e) {}
    }
	
}
