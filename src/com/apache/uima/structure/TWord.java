package com.apache.uima.structure;

import java.util.ArrayList;
import java.util.List;

public class TWord {
	private int id;
	private String content;
	private int counter;
	
	public TWord(String word, int id){
		this.content = word;
		this.id = id;
		
	}

	/**
	 * Add new match of current word
	 * @param sentenceId: sentence id of current match
	 */
	public void addNewMatch(){
		counter++;
	}
}
