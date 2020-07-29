package com.lexlang.SeleniumRequests.requests;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.lexlang.SeleniumRequests.proxy.ProxyPara;
import com.lexlang.SeleniumRequests.responses.Response;
import com.lexlang.SeleniumRequests.util.CheckOS;
import com.lexlang.SeleniumRequests.util.PostGetJS;
import com.lexlang.SeleniumRequests.util.UrlUtils;



/**
* @author lexlang
* @version 2018年11月27日 下午1:39:53
* 
*/
public abstract class SeleniumRequests {
	public static final int TIME_OUT=60;//默认超时时间
	public final static int MONITOR_ELEMENT_PRESENT_TIMES=6;
	public final static int INTERVAL_TIME=1000;//渲染时间
	public static Calendar nextDay;
	
	static{//手动添加cookie时需要时间
		nextDay = Calendar.getInstance();
		nextDay.setTime(new Date());
		nextDay.add(Calendar.DAY_OF_MONTH, +1);
	}
	
	public WebDriver  driver;
	
	/**
	 * 设置超时时间
	 * @param driver
	 * @param timeOut
	 */
	public void setTimeOut(WebDriver driver,int timeOut){
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);//页面加载超时
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);//js渲染超时
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);//识别对象超时
	}
	

	/**
	 * 添加cookie值
	 * @param cookies 复制cookie值
	 * @param domain   .baidu.com
	 */
	public void addCookie(String cookies,String domain){
		StringBuilder sb=new StringBuilder();
		String[] arr=domain.split("\\.");
		for(int i=1;i<arr.length;i++){
			sb.append("."+arr[i]);
		}
		domain=sb.toString();
		
		if(cookies.contains(";")){
			String[] cs=cookies.split(";");
			for(String cookie:cs){
				addSetCookie(cookie.trim(),domain);
			}
		}else{
			addSetCookie(cookies.trim(),domain);
		}
	}
	
	private void addSetCookie(String cookie,String domain){
		if(cookie.contains("=")){
			String name=cookie.substring(0,cookie.indexOf("="));
			String value=cookie.substring(cookie.indexOf("=")+1, cookie.length());
			driver.manage().addCookie(new Cookie(name, value, domain, "/", getNextDay()));
		}
	}
	
    private static Date getNextDay() {
        return nextDay.getTime();
    }
	
	
	/**
	 * 不同Selenium浏览器之间cookie互换
	 * @param cs
	 */
	public void addSetCookie(Set<Cookie> cs){
		clearAllCookies();
		for(Cookie cookie:cs){
			driver.manage().addCookie(cookie);
		}
	}
	
	/**
	 * 提取所有的cookie值
	 * @return
	 */
	public String getCookie(){
		Set<Cookie> cs = driver.manage().getCookies();
		StringBuilder sb=new StringBuilder();
		for(Cookie c:cs){
			if(sb.length()==0){
				sb.append(c.getName()+"="+c.getValue());
			}else{
				sb.append(";"+c.getName()+"="+c.getValue());
			}
		}
		return sb.toString();
	}
	
	/**
	 * 设置代理
	 * @param cap
	 * @param proxyPara
	 */
	public void setProxy(DesiredCapabilities cap,ProxyPara proxyPara){
		String proxyIpAndPort= proxyPara.getHost()+":"+proxyPara.getPort();//"192.168.5.228"+":8080";  
		org.openqa.selenium.Proxy proxy=new org.openqa.selenium.Proxy();  
        proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);  
        cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);  
        cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);  
        System.setProperty("http.nonProxyHosts", "localhost");  
        cap.setCapability(CapabilityType.PROXY, proxy);
	}
	
	public Response get(String url){
		driver.get(url);
		return new Response(getCurrentSource(),getCurrentUrl());
	}
	
	/**
	 * 简单get请求Ajax
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public Response getAjax(String url) throws Exception{
		return getUseHeaderAjax(url,null);
	}
	

	
	/**
	 * get请求使用消息头Ajax
	 * @param rul
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public Response getUseHeaderAjax(String url,Map<String,String> headers) throws Exception{
		String ajax = PostGetJS.postGetJS(url, "GET", headers, "");
		executeJavaScript(ajax);
		String content=executeJavaScript("var result=window.getContent;return result;").toString();
		return new Response(content,url);
	}
	
	/**
	 * 简单post请求 Ajax
	 * @param url
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Response postAjax(String url,String data) throws Exception{
		return postUseHeaderAjax(url,data,null);
	}
	
	/**
	 * post请求使用消息头 Ajax
	 * @param url
	 * @param data
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public Response postUseHeaderAjax(String url,String data,Map<String,String> headers) throws Exception{
		String ajax = PostGetJS.postGetJS(url, "POST", headers, data);
		executeJavaScript(ajax);
		String content=executeJavaScript("var result=window.getContent;return result;").toString();
		return new Response(content,url);
	}
	

	/**
	 * 获得验证码图片
	 * @param cssSelector css选择器  img.geetest_item_img 
	 * @return
	 * @throws IOException
	 */
	public BufferedImage getBufferedImage(String cssSelector) {
		WebElement webElement =  driver.findElement(By.cssSelector(cssSelector));
        BufferedImage image = null;
        try{image=createElementImage(driver,webElement);}catch(IOException ex){}
        return image;
	}
	
	/**
	 * 指定元素截图
	 * @param driver
	 * @param webElement
	 * @return
	 * @throws IOException
	 */
    private  BufferedImage createElementImage(WebDriver driver, WebElement webElement) throws IOException {
        // 获得webElement的位置和大小。
        Point location = webElement.getLocation();
        Dimension size = webElement.getSize();
        // 创建全屏截图。
        BufferedImage originalImage =screenShot();
        // 截取webElement所在位置的子图。
        BufferedImage croppedImage = originalImage.getSubimage(
                location.getX(),
                location.getY(),
                size.getWidth(),
                size.getHeight());
        return croppedImage;
    }
	
	
	/**
	 * 获得js执行器，相当于打开浏览器的调试窗口
	 * @return
	 */
	public JavascriptExecutor getJSE(){
		 JavascriptExecutor jse = (JavascriptExecutor) driver ;
		 return jse;
	}
	
	/**
	 * 执行js代码
	 * @param javascript
	 */
	public Object executeJavaScript(String javascript){
		return getJSE().executeScript(javascript);
	}
	
	/**
	 * 当前截图
	 * @return
	 * @throws IOException
	 */
	public BufferedImage screenShot() throws IOException{
        BufferedImage originalImage =ImageIO.read(new ByteArrayInputStream(takeScreenshot()));
        return originalImage;
	}
	
    private byte[] takeScreenshot() throws IOException {
    	if(getName().equals("JBrowserSeleniumRequests")){
            WebDriver augmentedDriver = new Augmenter().augment(driver);
            return ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
    	}else{
        	File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        	byte[] bFile = Files.readAllBytes(srcFile.toPath());
            return bFile;
    	}
    }
	

    /**
     * 当前链接
     * @return
     */
    public String getCurrentUrl(){
    	return driver.getCurrentUrl();
    }
    
    public WebDriver getDriver(){
    	return driver;
    }
    
    /**
     * 源码数据
     * @return
     */
	public String getCurrentSource(){
		return getDriver().getPageSource();
	}
    
	/**
	 * 获得文档
	 * @return
	 */
	public Document getDocument(){
		return Jsoup.parse(UrlUtils.fixAllRelativeHrefs(getCurrentSource(), getCurrentUrl()));
	}
	
	
	/**
	 * 等待某个元素出现
	 * @param cssSelector
	 */
	public void doNextUntilSomeOneElementShowUp(String cssSelector){
		for(int doTimes=0;doTimes<MONITOR_ELEMENT_PRESENT_TIMES;doTimes++){
			interval();
			if(isElementPresent(cssSelector)){
				return;
			}
		}
		System.out.println(getFixAllRelativeHrefsContent());
		throw new RuntimeException("超过："+MONITOR_ELEMENT_PRESENT_TIMES*INTERVAL_TIME/1000+"秒,没有找到css选择为："+cssSelector+"元素");
	}
	
	/**
	 * 休眠
	 */
	private void interval(){
		try {Thread.sleep(INTERVAL_TIME);} catch (InterruptedException e) {}
	}
	
	/**
	 * 是否存在某个元素
	 * @param cssSelector
	 * @return
	 */
	private boolean isElementPresent(String cssSelector) { 
		try { 
			  if(cssSelector.startsWith("//")){
				  if(checkXpath(cssSelector,Jsoup.parse(getFixAllRelativeHrefsContent()))){
					  return true;
				  }else{
					  return false;
				  }
			  }else{
				  Document doc = Jsoup.parse(getFixAllRelativeHrefsContent());
				  Elements links = doc.select(cssSelector);
				  if(links.size()>0) {
					  return true;
				  }else {
					  return false;
				  }
			  }
		} catch (NoSuchElementException e) { 
		      return false; 
		} 
	}
    
	/**
	 * 检测selenium是否包含xpath的元素
	 * @param xpath
	 * @param doc
	 * @return
	 */
	private boolean checkXpath(String xpath,Document doc){
		String elementName = xpath.replace("//", "").split("\\[")[0];
		String text=xpath.replace("]", "").replace("'", "").split("=")[1];
		Elements links = doc.select(elementName);
		for(Element link:links){
			if(link.text().trim().equals(text)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 点击页面对应文字得标签
	 * @param xpath //a[text()='下一页'] 或者   a#btnSearch
	 * @throws IOException 
	 */
	public void clickSection(String xpathOrCssSelector){
		doNextUntilSomeOneElementShowUp(xpathOrCssSelector);
		if(xpathOrCssSelector.startsWith("//")){
			getDriver().findElement(By.xpath(xpathOrCssSelector)).click();
		}else{
			getDriver().findElement(By.cssSelector(xpathOrCssSelector)).click();
		}
	}
	
	/**
	 * 对应的选择器填值
	 * @param xpathOrCssSelector
	 * @param sendKeys
	 */
	public void sendKeysToInput(String xpathOrCssSelector,String sendKeys){
		WebElement inputUser = null;
		if(xpathOrCssSelector.startsWith("//")){
			inputUser = getDriver().findElement(By.xpath(xpathOrCssSelector));
		}else{
			inputUser = getDriver().findElement(By.cssSelector(xpathOrCssSelector));
		}
		inputUser.click();
		inputUser.sendKeys(sendKeys);
	}
	
	
    /**
     * 关闭浏览器
     */
    public void close(){
    	if(CheckOS.isWinOS()){
    		driver.quit();
    	}else{
    		driver.close();
    	}
    }
    
	/**
	 * 
	 * @return 子类的类名
	 */
	public String getName(){
		return getClass().getSimpleName();
	}
	
	/**
	 * 补全链接的页面
	 * @return
	 */
	public String getFixAllRelativeHrefsContent(){
		return UrlUtils.fixAllRelativeHrefs(getCurrentSource(), getCurrentUrl()).replace("href=\"\"", "href=\"");
	}
	
	/**
	 * 清除所有cookie值
	 */
	public void clearAllCookies(){
		driver.manage().deleteAllCookies();
	}
	
	/**
	 * 切换iframe
	 * @param cssSelector
	 */
	public void switchToFrame(int order){
		driver.switchTo().frame(order);
	}
	
	/**
	 * 切换frame窗口
	 * @param nameOrId
	 */
	public void switchToFrame(String nameOrId){
		driver.switchTo().frame(nameOrId);
	}
	
	/**
	 * 父类窗口
	 */
	public void switchToParent(){
		driver.switchTo().defaultContent();
	}
	
	/**
	 * 切换页面,不关闭其他页面
	 * @param match 切换链接符合这个正则
	 */
	public void switchToWindow(String match){
		switchToWindow(match,false);
	}
	
	/**
	 * 切换页面,关闭其他页面
	 * @param match 切换链接符合这个正则
	 */
	public void switchToWindowCloseOthers(String match){
		switchToWindow(match,true);
	}
	
	/**
	 * 获得批量的元素
	 * @param cssSelector
	 * @return
	 */
	public List<WebElement> getWebElements(String cssSelector){
		doNextUntilSomeOneElementShowUp(cssSelector);
		List<WebElement> links = null;
		if(cssSelector.startsWith("//")){
			links =getDriver().findElements(By.xpath(cssSelector));
		}else{
			links =getDriver().findElements(By.cssSelector(cssSelector));
		}
		return links;
	}
	
	
	/**
	 * 切换窗口
	 * @param match
	 * @param flagClose
	 */
	private void switchToWindow(String match,boolean flagClose){
        Set<String> handles = driver.getWindowHandles();// 获取所有窗口句柄  
        Iterator<String> it = handles.iterator();  
        String matchWin=null;
        while (it.hasNext()) { 
            try {  
            	String current=it.next();
                WebDriver window = driver.switchTo().window(current);// 切换到新窗口  
                if(! window.getCurrentUrl().toString().matches(match)){
                	if(flagClose){
                		window.close();
                	}
                }else{
                	matchWin=current;
                }
            }catch(Exception ex){ex.printStackTrace();}
        }
        if(matchWin!=null){
        	driver.switchTo().window(matchWin);
        }else{
        	//throw new RuntimeException("没有找到符合："+match+"的链接窗口");
        }
	}
	
	/**
	 * 向下滑动浏览器
	 */
	public void scrollDown(int distance){
		executeJavaScript("window.scrollBy(0, "+distance+")");
	}
	
	/**
	 * 滑动滑块
	 * @param moveElement
	 * @param tracks
	 * @throws InterruptedException
	 */
	public void move(WebElement moveElement, Map<String,ArrayList<Integer>> tracks) throws InterruptedException {
		Actions action=new Actions(getDriver());
		action.clickAndHold(moveElement).perform();
		Thread.sleep(500);
		ArrayList<Integer> forward_tracks=tracks.get("forward_tracks");
		for(int i=0;i<forward_tracks.size();i++){
			action.moveByOffset(forward_tracks.get(i), 0).perform();
		}
		
		Thread.sleep(500);
		
		ArrayList<Integer> back_tracks=tracks.get("back_tracks");
		
		for(int i=0;i<back_tracks.size();i++){
			action.moveByOffset(back_tracks.get(i), 0).perform();
		}
		
		action.moveByOffset(-3, 0).perform();
		action.moveByOffset(3, 0).perform();
		Thread.sleep(300);
		action.release().perform();
	}
	
	/**
	 * 点击元素上的坐标点
	 * @param webElement
	 * @param x
	 * @param y
	 * @throws InterruptedException
	 */
	public void click(WebElement webElement,int x,int y) throws InterruptedException{
        Actions action = new Actions(getDriver());
        action.moveToElement(webElement, x, y);
        action.click().perform();
        randomInterval();
        action.release();
	}
	
    private void sleep(int sleepTime){
        try {Thread.sleep(sleepTime);} catch (InterruptedException e) {}
    }
    
    private void randomInterval(){
        Random rand = new Random();
        long randSleep = Math.round(INTERVAL_TIME*2*Math.sin(rand.nextDouble()));//使用sina随机点选一个时间
        try {Thread.sleep(randSleep);} catch (InterruptedException e) {}
    }
    
}
