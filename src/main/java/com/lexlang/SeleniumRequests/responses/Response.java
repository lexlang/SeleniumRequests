package com.lexlang.SeleniumRequests.responses;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
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
	
	private List<NameValuePair> headers=new ArrayList<NameValuePair>();
	private String content;
	private String currentUrl;
	
	public Response(String content,String currentUrl,List<NameValuePair> headers){
		this.content=content.replace("data:application/octet-stream;base64,", "");
		this.currentUrl=currentUrl;
		this.headers=headers;
	}
	
	/**
	 * 获得消息头
	 * @return
	 */
    public List<NameValuePair> getHeaders(){
    	return headers;
    }
	
    /**
     * 
     * @param key
     * @return
     */
    public String getHeaderValue(String key){
    	List<NameValuePair> list = getHeaders();
    	for(int i=0;i<list.size();i++){
    		NameValuePair keyValue = list.get(i);
    		if(keyValue.getName().toLowerCase().equals(key.toLowerCase())){
    			return keyValue.getValue();
    		}
    	}
    	return null;
    }
    
	public Response(String content){
		this.content=Base64.encodeBase64String(content.getBytes());
	}
	
	/**
	 * 获得response流
	 * @return
	 */
	public InputStream getInputStream(){
		return new ByteArrayInputStream(Base64.decodeBase64(content));
	}
	
	public String getContent(String decode) throws UnsupportedEncodingException{
		return new String(Base64.decodeBase64(content),decode);
	}
	
	/**
	 * 获得response文本
	 * @return
	 */
	public String getContent(){
		try {
			String[] decs={"utf-8","gbk","gb2312"};
			for(String dec:decs){
				String con=new String(Base64.decodeBase64(content),dec);
				if(! isMessyCode(con.replaceAll("[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "")
						.replaceAll("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "").replaceAll("[a-zA-Z0-9]", "")
						.replace(" ","").replace("\r", "").replace("\n", "").replace("\t", ""))){
					return con;
				}
			}
			return new String(Base64.decodeBase64(content),"utf-8");
		} catch (IOException e) {System.out.println("流转字符串发生错误");}
		return "";
	}
	
	  /** 
     * 判断字符串是否是乱码 
     * 
     * @param strName 字符串 
     * @return 是否是乱码 
     */  
    private static boolean isMessyCode(String strName) {  
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");  
        Matcher m = p.matcher(strName);  
        String after = m.replaceAll("");  
        String temp = after.replaceAll("\\p{P}", "");  
        char[] ch = temp.trim().toCharArray();  
        float chLength = ch.length;  
        float count = 0;  
        for (int i = 0; i < ch.length; i++) {  
            char c = ch[i];  
            if (!Character.isLetterOrDigit(c)) {  
                if (!isChinese(c)) {  
                    count = count + 1;  
                }  
            }  
        }  
        float result = count / chLength;  
        if (result > 0.2) {  
            return true;  
        } else {  
            return false;  
        }  
   
    } 
	
    private static boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
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
    	return Jsoup.parse(getContent());
    }
    
}
