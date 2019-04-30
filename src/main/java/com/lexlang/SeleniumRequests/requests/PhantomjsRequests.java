package com.lexlang.SeleniumRequests.requests;

import java.io.IOException;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.lexlang.SeleniumRequests.proxy.ProxyPara;
import com.lexlang.SeleniumRequests.util.PathCfg;


/**
* @author lexlang
* @version 2019年4月30日 下午4:20:47
* 
*/
public class PhantomjsRequests  extends SeleniumRequests{
	
	public static PhantomjsRequests getHeadlessSelenium(){
		return new PhantomjsRequests(TIME_OUT,null);
	}
	
	public static PhantomjsRequests getHeadlessSelenium(ProxyPara proxy){
		return new PhantomjsRequests(TIME_OUT,proxy);
	}
	
	public static PhantomjsRequests getHeadlessSelenium(int timeout){
		return new PhantomjsRequests(timeout,null);
	}
	
	public static PhantomjsRequests getHeadlessSelenium(int timeout,ProxyPara proxy){
		return new PhantomjsRequests(timeout,proxy);
	}
	
	public static PhantomjsRequests getHeadlessSelenium(ProxyPara proxy,int timeout){
		return new PhantomjsRequests(timeout,proxy);
	}
	
	public PhantomjsRequests(int timeout,ProxyPara proxyPara){
		System.setProperty("phantomjs.binary.path",PathCfg.getPhantomjsPath());// 
	    DesiredCapabilities desiredCapabilities = DesiredCapabilities.phantomjs();

		//设置UA
	    //desiredCapabilities.setCapability("phantomjs.page.customHeaders.User-Agent", UserAgent.getRandomUserAgent());

		//屏蔽日志文件
		String[] phantomArgs = new  String[] {"--webdriver-loglevel=NONE","--webdriver-logfile="};
		desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);
		
		//手工指定一个代理
		if(proxyPara!=null){ 
			String ip = proxyPara.getHost()+":"+proxyPara.getPort();
			String[] values = { "--disk-cache=yes", "--ignore-ssl-errors=true", "--proxy=" + ip };
			desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, values);
		}

	    driver = new PhantomJSDriver(desiredCapabilities); 
	    setTimeOut(driver,timeout);
	}
	
    public void close(){
    	super.close();
    	try {Runtime.getRuntime().exec("taskkill /f /im phantomjs.exe");} catch (IOException e) {}
    }
	
}
