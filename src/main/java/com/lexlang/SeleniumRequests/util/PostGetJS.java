package com.lexlang.SeleniumRequests.util;

import java.util.Map;
import java.util.Set;

/**
* @author lexlang
* @version 2019年4月24日 上午9:40:48
* 
*/
public class PostGetJS {
	
	/**
	 * 
	 * @param url 
	 * @param method
	 * @param headers
	 * @param postData
	 * @return
	 */
	public static String postGetJS(String url,String method,Map<String,String> headers,String postData){
		StringBuilder sb=new StringBuilder();
		sb.append("window.getStreamFile = null;\n");
		sb.append("function blobToDataURL(blob) {var a = new FileReader();a.readAsDataURL(blob);a.onload = function (e) {window.getStreamFile = e.target.result;}}\n");
		sb.append("var xmlhttp=new XMLHttpRequest();\n");
		sb.append("xmlhttp.open(\""+method+"\",\""+url+"\",true);\n");
		sb.append("xmlhttp.responseType = 'arraybuffer';\n");
		sb.append("xmlhttp.onload=function(oEvent){\n");
		sb.append("var blob = new Blob([xmlhttp.response]);if (blob) {blobToDataURL(blob);\n");
		sb.append("var headers = xmlhttp.getAllResponseHeaders();\n");
		sb.append("var arr = headers.trim().split(/[\\r\\n]+/);\n");
		sb.append("var hd =[];\n");
		sb.append("arr.forEach(function (line) {var headerMap = {}; var parts = line.split(': ');var header = parts.shift();var value = parts.join(': ');headerMap[header] = value;hd.push(headerMap);});\n");
		sb.append("window.getHeader=JSON.stringify(hd);\n");
		sb.append("window.getStatus=xmlhttp.status;");
		sb.append("}};\n");
		if(headers!=null){
			Set<String> keys = headers.keySet();
			for(String key:keys){
				sb.append( "xmlhttp.setRequestHeader(\""+key+"\",\""+headers.get(key)+"\");\n");
			}
		}else{
			if(method.toLowerCase().equals("post")){
				sb.append( "xmlhttp.setRequestHeader(\""+"content-type"+"\",\""+"application/x-www-form-urlencoded"+"\");\n");
			}
		}
		sb.append("xmlhttp.send('"+postData+"');\n");
		return sb.toString();
	}
}
