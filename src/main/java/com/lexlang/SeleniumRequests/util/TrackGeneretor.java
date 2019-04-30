package com.lexlang.SeleniumRequests.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
* @author lexlang
* @version 2019年4月30日 下午2:38:56
* 
*/
public class TrackGeneretor {
	
	/**
	 * 生成轨迹
	 * @param distance
	 * @return
	 */
	public static Map<String,ArrayList<Integer>> get_tracks(int distance){
		distance+=20;
		double v=0;
		double t=0.2;
		ArrayList<Integer> forward_tracks=new ArrayList<Integer>();
		
		double current=0;
		double mid=distance*3/5;
		while(current<distance){
			int a=0;
			if(current<mid){
				a=2;
			}else{
				a=-3;
			}
			
			double s=v*t+0.5*a*(t*t);
			v=v+a*t;
			current+=s;
			forward_tracks.add((int) Math.round(s));
		}
		ArrayList<Integer> back_tracks=new ArrayList<Integer>();
		back_tracks.add(-3);back_tracks.add(-3);back_tracks.add(-2);
		back_tracks.add(-2);back_tracks.add(-2);back_tracks.add(-2);
		back_tracks.add(-2);back_tracks.add(-1);back_tracks.add(-1);
		back_tracks.add(-1);
		Map<String,ArrayList<Integer>> tracks=new HashMap<String,ArrayList<Integer>>();
		tracks.put("forward_tracks", forward_tracks);
		tracks.put("back_tracks", back_tracks);
		return tracks;
	}
	
	
}
