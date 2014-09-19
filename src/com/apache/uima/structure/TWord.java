package com.apache.uima.structure;

import java.util.ArrayList;
import java.util.List;

public class TWord {
	private int id;
	private ArrayList<Integer> sentenceId = new ArrayList<Integer>();
	private String content;
	private int counter;
	
	public TWord(String word, int id,int sentenceId){
		this.content = word;
		this.id = id;
		this.sentenceId.add(sentenceId);
	}

	public String getWord(){
		return this.content;
	}
	
	public int getWordId(){
		return this.id;
	}
	
	/**
	 * Add new match of current word
	 * @param sentenceId: sentence id of current match
	 */
	public void addNewMatch(int sentenceId){
		counter++;
		this.sentenceId.add(sentenceId);
	}
	
	/**
	 * 
	 * @return sentenceId that word belongs to
	 */
	public ArrayList<Integer> getSentenceId(){
		return sentenceId;
	}
}
