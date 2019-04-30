package com.lexlang.SeleniumRequests.requests;

import java.io.IOException;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.lexlang.SeleniumRequests.proxy.ProxyPara;
import com.lexlang.SeleniumRequests.util.PathCfg;

/**
* @author lexlang
* @version 2019年4月30日 下午3:02:17
* 
*/
public class IERequests extends SeleniumRequests {
	
	static{
		String path=PathCfg.getIEPath();
		System.setProperty("webdriver.ie.driver",path);
	}
	
	public static SeleniumRequests getSelenium(){
		return new IERequests(TIME_OUT,null);
	}
	
	public static SeleniumRequests getSelenium(ProxyPara proxy){
		return new IERequests(TIME_OUT,proxy);
	}
	
	public static SeleniumRequests getSelenium(int timeout){
		return new IERequests(timeout,null);
	}
	
	public static SeleniumRequests getSelenium(ProxyPara proxy,int timeout){
		return new IERequests(timeout,proxy);
	}
	
	public static SeleniumRequests getSelenium(int timeout,ProxyPara proxy){
		return new IERequests(timeout,proxy);
	}
	
	private IERequests(int timeout,ProxyPara proxy){
	    DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
        dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        dc.setCapability("ignoreProtectedModeSettings", true);
        
		if(proxy!=null){ //手工指定一个代理
			setProxy(dc,proxy);
		}
		//把加载关闭配置加载到IE浏览器
        driver = new InternetExplorerDriver(dc);
	}
		
    public void close(){
    	super.close();
    	try {Runtime.getRuntime().exec("taskkill /f /im IEDriverServer.exe");} catch (IOException e) {}
    }
    
}
