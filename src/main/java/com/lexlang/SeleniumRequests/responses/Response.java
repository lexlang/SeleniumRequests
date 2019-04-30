package com.lexlang.SeleniumRequests.responses;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lexlang.SeleniumRequests.util.UrlUtils;

/**
* @author lexlang
* @version 2019年4月29日 下午4:47:35
* 
*/
public class Response {
	
	private String content;
	private String currentUrl;
	
	public Response(String content,String currentUrl){
		this.content=content;
	}
	
	public String getContent(){
		return content;
	}
	
	/**
	 * 获得json map对象
	 * @return
	 */
	public JSONObject getJsonObject(){
		return JSONObject.parseObject(getContent());
	}
	
	/**
	 * 获得json list对象
	 * @return
	 */
	public JSONArray getJsonArray(){
		return JSONArray.parseArray(getContent());
	}
	
	/**
	 * 补全链接的文本
	 * @return
	 */
	public String getFixHrefContent(){
		return UrlUtils.fixAllRelativeHrefs(getContent(), currentUrl);
	}
	
    /**
     * 获取页面的document
     * @return
     */
    public Document getFixHrefDocument(){
    	return Jsoup.parse(getFixHrefContent());
    }
	
    /**
     * 获取页面的document
     * @return
     */
    public Document getDocument(){
    	return Jsoup.parse(UrlUtils.fixAllRelativeHrefs(getContent().replace("&nbsp;", " "),currentUrl));
    }
    
}
