package com.apache.uima.structure;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.TestRun;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;



public class TWordWarehouse {
	private static volatile TWordWarehouse instance = null; 
	//static private Map sentences = new HashMap<Integer,TSentence>();
	static private Map words = new HashMap<Integer,TWord>();   //TODO SERIALIZE
	static private Map indexer = new HashMap<Integer,Integer>(); //TODO SERIALIZE
	static private TLargeIntMatrix matrix;
	static private int totalWords;
	static private int totalSentences;
	private final ClassLoader resourcer = this.getClass().getClassLoader();
	
	public TWordWarehouse(){
		//totalWords=0;
	}
	
	/**
	 * Use singleton pattern. There is just one warehouse
	 * @return instance of warehouse
	 */
	public static TWordWarehouse getInstance(){
		if (instance == null) {
            synchronized (TWordWarehouse.class) {
                // Double check
                if (instance == null) {
                    instance = new TWordWarehouse();
                }
            }
        }
        return instance;
	}
	/**
	 * @deprecated if we keep all sentences our memory will raise dangerous
	 * @param sentence
	 * @param sentenceId
	 
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
*/	
	
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
	
	/**
	 * Build Adjacency matrix for total words
	 * @throws IOException 
	 */
	public void initializeAdjMatrix() throws IOException{
		int arraySize = words.entrySet().size();
		//adjMatrix = new int[words.entrySet().size()][words.entrySet().size()];
		matrix = new TLargeIntMatrix("resources/dataset.big", arraySize, arraySize);
		int adjX=0;
		int adjX0=0;
		int adjY=0;
		long startTime = System.currentTimeMillis();
		System.out.print("\t");
		for (Iterator<TWord> wordIter0 =words.entrySet().iterator(); wordIter0.hasNext(); adjX0++){
			Map.Entry pairs1 = (Map.Entry)wordIter0.next();
			TWord wordd = (TWord)pairs1.getValue();
			if(adjX>arraySize-1)
			System.out.print(wordd.getWord()+"\t");
		}
		long timeElapsed = System.currentTimeMillis()-startTime;
		
		System.out.print("\n");
		for (Iterator<TWord> wordIter =words.entrySet().iterator(); wordIter.hasNext(); adjX++){
			startTime = System.currentTimeMillis();
			Map.Entry pairs = (Map.Entry)wordIter.next();
			TWord word = (TWord)pairs.getValue();
			System.out.print(word.getWord()+"\t");
			indexer.put(word.getWordId(), adjX);
			if(word.getWord().length()>2)
			for (Iterator<TWord> wordIter2 =words.entrySet().iterator(); wordIter2.hasNext(); adjY++){
				//Diagonial Matrix needed. We don't need same word to be returned!
				Map.Entry pairsInner = (Map.Entry)wordIter2.next();
				if(adjX!=adjY){
					
					TWord wordInner = (TWord)pairsInner.getValue();
					int overlaps = findOverlaps(word,wordInner);
					//unwanted matrix sets increase memory consumption!!!!
					if(overlaps>0)
						matrix.set(adjX,adjY,overlaps);
					
					if(overlaps>10)
						System.out.print("");
					
				}
				//IF enable else matrix.set then complexity goes to O(n)
				//else 
					//matrix.set(adjX,adjY,0);
				//System.out.print(matrix.get(adjX,adjY)+" ");
				if(adjY>arraySize-1)
					System.out.print("t");
			}
			timeElapsed = System.currentTimeMillis()-startTime;
			System.out.print("\n");
			adjY=0; // reset adjY in order to make loop to read next row's columns
			
		}
		//printAdjMatrix();
	}
	
	public void saveWarehouse() throws URISyntaxException, IOException{
		File f = new File("resources/words.json");
		File f2 = new File("resources/indexer.json");
		OutputStream os = new FileOutputStream(f);
		OutputStream os2 = new FileOutputStream(f2);
		
		JsonWriter jw = new JsonWriter(os);
		jw.write(words);
		jw.close();
		
		jw=new JsonWriter(os2);
		jw.write(indexer);
		jw.close();
	}
	
	public void openWarehouse() throws IOException{
		File f = new File("./resources/words.json");
		File f2 = new File("resources/indexer.json");
			//Parse word json
			JsonReader jr = new JsonReader(new FileInputStream(f));
			words = (HashMap<Integer,TWord>)jr.readObject();
			jr.close();
			
			//Parse indexer json (map wordId with Column)
			jr = new JsonReader(new FileInputStream(f2));
			indexer = (HashMap<Integer,Integer>)jr.readObject();
			jr.close();
			
			matrix = new TLargeIntMatrix("resources/dataset.big", words.size(),words.size());
		
	}
	
	public void printAdjMatrix(){
		for(int i=0;i<totalWords;++i){
			int row[] = matrix.getRow(i);
			for(int j=0;j<totalWords;++j)
				System.out.print(row[j]+" ");
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
		for(int i=0;i<w2SentId.size();i++){
			if(w1SentId.contains(w2SentId.get(i)))
					overlaps++;
		}
		return overlaps;
	}
	
	
	/**
	 * Find k best word matches for word queryWord. Use a simple TOP K query 
	 * @param queryWord: word to search
	 * @param k: total words to return
	 * @return s[]: Words found
	 * @throws TWordNotFound 
	 */
	public String[] analyse(String queryWord,int k) throws TWordNotFound{
		long startAnalyze = System.currentTimeMillis();
		String[] wordsFound = new String[k];
		int[] wordsMaxQueue= new int[k];
		int[] colIndexes= new int[k];
		
		//If word does not exist in our indexer, through null exception
		int id = TStringTools.identizer(queryWord.toLowerCase());
		Object tmpRow;
		
		//Findout if query word exist in our Warehouse. If not throw
		tmpRow = indexer.get(id);
			if(tmpRow == null)
				throw new TWordNotFound(queryWord);
		
		int queryRow = (Integer)tmpRow;
//		int rowSum = 0;
		int row [] = matrix.getRow(queryRow);
		sortIndexer(row,wordsMaxQueue,colIndexes);
		System.out.println("");
//		for (int i=0;i<row.length;i++){
//			//System.out.print(row[i]+ " ");
//			//rowSum+=row[i];
//		}
		System.out.println("Search for "+queryWord);
		for(int i=0;i<k;++i){
			int wordId = (Integer)getKeyByValue(indexer,colIndexes[i]);
			TWord word = (TWord) words.get(wordId);
			System.out.println(" word "+String.valueOf(i+1)+": "+word.getWord()+" matches: "+
					matrix.get(queryRow,colIndexes[i])+" freq: "+word.getFrequency());
		}
		System.out.println("Analyze took "+((System.currentTimeMillis()-startAnalyze)/1000.0) + " ");
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

	
	private void sortIndexer(int[] row, int[] wordsMaxQueue,int[] colIndexes) {
		for(int i=0;i<row.length;++i){
			for(int j=0;j<wordsMaxQueue.length;++j){
				if(row[i]>=wordsMaxQueue[j]){
					shiftDigits(wordsMaxQueue,j+1);
					wordsMaxQueue[j]= row[i];
					shiftDigits(colIndexes,j+1);
					colIndexes[j]=i;
					break;
				}
					
			}
		}
		System.out.print("");
	}
	
	
	private void shiftDigits(int[] wordsMax,int index){
		if(index<wordsMax.length){
			shiftDigits(wordsMax,index+1);
			wordsMax[index]=wordsMax[index-1];
		}
		
	}
}
