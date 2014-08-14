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

	/**
	 * Add new match of current word and keep sentenceId
	 * @param sentenceId: sentence id of current match
	 */
	public void addNewWord(String word){
		int wordId = TStringTools.identizer(word);
		TWord wordTemp = (TWord)words.get(wordId);
		if(wordTemp==null){
			words.put(wordId, new TWord(word,wordId));
		}
		else{
			wordTemp.addNewMatch();
		}	
	}
	
	public void addNewMatch(){
		counter++;
	}
}
