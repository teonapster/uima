package com.apache.uima.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TWordWarehouse {
	private Map sentences = new HashMap<Integer,TSentence>();
	
	public TWordWarehouse(){
		
	}
	
	public TSentence addSentence(String sentence){
		int sentenceId = TStringTools.identizer(sentence);
		TSentence sentenceTemp = (TSentence)sentences.get(sentenceId);
		if(sentenceTemp==null){
			sentenceTemp = new TSentence(sentence,sentenceId);
			sentences.put(sentenceId, sentence);
		}
		else{
			sentenceTemp.addNewMatch();
		}	
		return sentenceTemp;
	}
}
