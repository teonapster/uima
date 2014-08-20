package com.apache.uima.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TSentence {
	private int id;
	private String content;
	private int counter;
	private Map words = new HashMap<Integer,TWord>();
	
	public TSentence(String sentence,int id){
		this.content = sentence;
		this.id = id;
		
	}
	
	public void addNewMatch(){
		counter++;
	}
}
