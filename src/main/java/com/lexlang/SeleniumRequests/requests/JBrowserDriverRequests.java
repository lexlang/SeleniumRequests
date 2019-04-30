package com.lexlang.SeleniumRequests.requests;

import java.util.logging.Level;

import com.lexlang.SeleniumRequests.proxy.ProxyPara;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.ProxyConfig;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.machinepublishers.jbrowserdriver.Settings.Builder;

/**
* @author lexlang
* @version 2019年4月30日 下午3:06:45
* 
*/
public class JBrowserDriverRequests extends SeleniumRequests {
	
	public static SeleniumRequests getSelenium(){
		return new JBrowserDriverRequests(TIME_OUT,null,false);
	}
	
	public static SeleniumRequests getSelenium(ProxyPara proxy){
		return new JBrowserDriverRequests(TIME_OUT,proxy,false);
	}
	
	public static SeleniumRequests getSelenium(int timeout){
		return new JBrowserDriverRequests(timeout,null,false);
	}
	
	public static SeleniumRequests getSelenium(int timeout,ProxyPara proxy){
		return new JBrowserDriverRequests(timeout,proxy,false);
	}
	
	public static SeleniumRequests getSelenium(ProxyPara proxy,int timeout){
		return new JBrowserDriverRequests(timeout,proxy,false);
	}
	
	public static SeleniumRequests getHeadlessSelenium(){
		return new JBrowserDriverRequests(TIME_OUT,null,true);
	}
	
	public static SeleniumRequests getHeadlessSelenium(ProxyPara proxy){
		return new JBrowserDriverRequests(TIME_OUT,proxy,true);
	}
	
	public static SeleniumRequests getHeadlessSelenium(int timeout){
		return new JBrowserDriverRequests(timeout,null,true);
	}
	
	public static SeleniumRequests getHeadlessSelenium(int timeout,ProxyPara proxy){
		return new JBrowserDriverRequests(timeout,proxy,true);
	}
	
	public static SeleniumRequests getHeadlessSelenium(ProxyPara proxy,int timeout){
		return new JBrowserDriverRequests(timeout,proxy,true);
	}
	
	public JBrowserDriverRequests(int timeout,ProxyPara proxy,boolean headless){
		//设置代理  
		Builder setting = Settings.builder()
                .timezone(Timezone.ASIA_SHANGHAI)
                .csrf()//跨域访问
                .headless(false)//是否需要界面
                .javascript(true)//js是否执行
                .loggerLevel(Level.ALL)
                .logWarnings(true)
                .logsMax(10000)
                .connectionReqTimeout(timeout);//链接超时时间

		if(proxy!=null){ //手工指定一个代理
			setProxy(setting,proxy);
		}
		
        driver = new JBrowserDriver(setting.build());
	}
	
	/**
	 * 设置代理
	 * @param setting
	 * @param proxyPara
	 */
	private void setProxy(Builder setting, ProxyPara proxyPara) {
		ProxyConfig cfg = new ProxyConfig(ProxyConfig.Type.HTTP,proxyPara.getHost(),proxyPara.getPort());
		setting.proxy(cfg);
	}
	
}
