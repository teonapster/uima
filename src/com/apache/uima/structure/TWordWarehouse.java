package com.apache.uima.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TWordWarehouse {
	private Map sentences = new HashMap<Integer,TSentence>();
	private Map words = new HashMap<Integer,TWord>();
	private Map indexer = new HashMap<Integer,Integer>();
	private int adjMatrix[][];
	private int totalWords;
	private int totalSentences;
	
	public TWordWarehouse(){
		totalWords=0;
	}
	
	public void addSentence(String sentence,int sentenceId){
		TSentence sentenceTemp = (TSentence)sentences.get(sentenceId);
		if(sentenceTemp==null){
			sentenceTemp = new TSentence(sentence,sentenceId);
			sentences.put(sentenceId, sentence);
			totalSentences++;
		}
		else{
			sentenceTemp.addNewMatch();
		}
	}
	
	
	/**
	 * Add new match of current word and keep sentenceId
	 * @param sentenceId: sentence id of current match
	 */
	public void addNewWord(String word, int sentenceId){
		word = word.toLowerCase();
		int wordId = TStringTools.identizer(word);
		TWord wordTemp = (TWord)words.get(wordId);
		if(wordTemp==null){
			words.put(wordId, new TWord(word,wordId,sentenceId));
			increaseTotalWords();
		}
		else{
			wordTemp.addNewMatch(sentenceId);
		}	
	}
	
	
	/**
	 * Increase total word counter!!!!
	 */
	public void increaseTotalWords(){
		totalWords++;
	}
	
	
	public void initializeAdjMatrix(){
		adjMatrix = new int[words.entrySet().size()][words.entrySet().size()];
		int adjX=0;
		int adjX0=0;
		int adjY=0;
		System.out.print("\t");
		for (Iterator<TWord> wordIter0 =words.entrySet().iterator(); wordIter0.hasNext(); adjX0++){
			Map.Entry pairs1 = (Map.Entry)wordIter0.next();
			TWord wordd = (TWord)pairs1.getValue();
			System.out.print(wordd.getWord()+"\t");
		}
		System.out.print("\n");
		for (Iterator<TWord> wordIter =words.entrySet().iterator(); wordIter.hasNext(); adjX++){
			Map.Entry pairs = (Map.Entry)wordIter.next();
			TWord word = (TWord)pairs.getValue();
			System.out.print(word.getWord()+"\t");
			indexer.put(word.getWordId(), adjX);
			for (Iterator<TWord> wordIter2 =words.entrySet().iterator(); adjY<words.entrySet().size(); adjY++){
				//Diagonial Matrix needed. We don't need same word to be returned!
				Map.Entry pairsInner = (Map.Entry)wordIter2.next();
				if(adjX!=adjY){
					
					TWord wordInner = (TWord)pairsInner.getValue();
					int overlaps = findOverlaps(word,wordInner);
					adjMatrix[adjX][adjY]=overlaps;
					
				}
				else 
					adjMatrix[adjX][adjY]=0;
				System.out.print(adjMatrix[adjX][adjY]+"\t");
			}
			System.out.print("\n");
			adjY=0; // reset adjY in order to make loop to read next row's columns
		}
	}
	
	public void printAdjMatrix(){
		for(int i=0;i<totalWords;++i){
			for(int j=0;j<totalWords;++j)
				System.out.print(adjMatrix[i][j]+" ");
			System.out.print("\n");
		}
	}
	/**
	 * Find sentenceId overlaps between two words. In other words findout 
	 * common sentences that two words are participate
	 * @param w1 word1
	 * @param w2 word2
	 * @return total overlaps (join)
	 */
	private int findOverlaps(TWord w1,TWord w2){
		ArrayList<Integer> w1SentId = w1.getSentenceId();
		ArrayList<Integer> w2SentId = w2.getSentenceId();
		int overlaps = 0;
		
		for(int i=0;i<w1SentId.size();i++){
			if(w2SentId.contains(w1SentId.get(i)))
					overlaps++;
		}
		return overlaps;
	}
	
	
	/**
	 * Find k best word matches for word queryWord. Use a simple TOP K query 
	 * @param queryWord: word to search
	 * @param k: total words to return
	 * @return s[]: Words found
	 */
	public String[] analyse(String queryWord,int k){
		String[] wordsFound = new String[k];
		int[] wordsMax= new int[k];
		int[] colIndexes= new int[k];
		int queryRow = (Integer)indexer.get(TStringTools.identizer(queryWord));
		sortIndexer(adjMatrix[queryRow],wordsMax,colIndexes);
		
		for(int i=0;i<k;++i){
			int wordId = (Integer)getKeyByValue(indexer,colIndexes[i]);
			TWord word = (TWord) words.get(wordId);
			System.out.println(i+1+" word "+word.getWord());
		}
		return wordsFound;
	}
	
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}

	
	private void sortIndexer(int[] commons, int[] wordsMax,int[] colIndexes) {
		for(int i=0;i<commons.length;++i){
			pushToIndexer(wordsMax,commons[i],colIndexes,i);
		}
		System.out.print("");
	}
	
	
	//desc sorting e.g [5,4,3,2,1]
	private void pushToIndexer( int[] wordsMax,int common,int [] colIndexes,int wordCol){
		for(int j=0;j<wordsMax.length;++j){
			if(common>=wordsMax[j]){
				shiftDigits(wordsMax,j+1);
				wordsMax[j]= common;
				shiftDigits(colIndexes,j+1);
				colIndexes[j]=wordCol;
				break;
			}
				
		}
	}
	
	private void shiftDigits(int[] wordsMax,int index){
		if(index+1<wordsMax.length){
			shiftDigits(wordsMax,index+1);
			wordsMax[index]=wordsMax[index-1];
		}
	}
}
