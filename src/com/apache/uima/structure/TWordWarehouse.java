package com.apache.uima.structure;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
	static private Map indexedWord= new HashMap<Integer,Map<String,Integer>>(); //TODO SERIALIZE
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
		//matrix = new TLargeIntMatrix("resources/dataset.big", arraySize, arraySize);
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
			//indexer.put(word.getWordId(), adjX); //what'a hell i was thinking here .... use a hash map wordid->list
			indexedWord.put(word.getWordId(), new HashMap<String,Integer>());
			if(word.getWord().length()>2)
			for (Iterator<TWord> wordIter2 =words.entrySet().iterator(); wordIter2.hasNext(); adjY++){
				//Diagonial Matrix needed. We don't need same word to be returned!
				Map.Entry pairsInner = (Map.Entry)wordIter2.next();
				if(adjX!=adjY){
					
					TWord wordInner = (TWord)pairsInner.getValue();
					int overlaps = findOverlaps(word,wordInner);
					
					//unwanted matrix sets increase memory consumption!!!!
					if(overlaps>0)
						((Map) indexedWord.get(word.getWordId())).put(wordInner.getWord(),overlaps);
					
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
			//matrix.close();
		}
		//printAdjMatrix();
	}
	
	public void saveWarehouse() throws URISyntaxException, IOException{
		File f = new File("resources/words.json");
		File f2 = new File("resources/indexer.dat");
		OutputStream os = new FileOutputStream(f);
		OutputStream os2 = new FileOutputStream(f2);
		
		JsonWriter jw = new JsonWriter(os);
		jw.write(words);
		jw.close();
		
		
		  
		 FileOutputStream fos = new FileOutputStream(f2);  
		 ObjectOutputStream oos = new ObjectOutputStream(fos);          
		 oos.writeObject(indexedWord);
		 oos.close();
		
//		jw=new JsonWriter(os2);
//		jw.write(indexer);
//		jw.close();
	}
	
	public void openWarehouse() throws IOException, ClassNotFoundException{
		File f = new File("./resources/words.json");
		File f2 = new File("resources/indexer.dat");
			//Parse word json
			JsonReader jr = new JsonReader(new FileInputStream(f));
			words = (HashMap<Integer,TWord>)jr.readObject();
			jr.close();
			
			
			FileInputStream fis = new FileInputStream(f2);
			ObjectInputStream ois = new ObjectInputStream(fis);
			indexedWord = (Map<Integer,Map<String,Integer>>)ois.readObject();
			ois.close();
			//Parse indexer json (map wordId with Column)
//			jr = new JsonReader(new FileInputStream(f2));
//			indexer = (HashMap<Integer,Integer>)jr.readObject();
//			jr.close();
			
			//matrix = new TLargeIntMatrix("resources/dataset.big", words.size(),words.size());
			//Open matrix here
		
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
	 * @throws IOException 
	 */
	public String[] analyse(String queryWord,int k,TSimpleRowWriter tsrw) throws TWordNotFound, IOException{
		long startAnalyze = System.currentTimeMillis();
		String[] wordsFound = new String[k];
		int[] wordsMaxQueue= new int[k];
		int[] colIndexes= new int[k];
		
		
		
		
		//If word does not exist in our indexer, through null exception
		int id = TStringTools.identizer(queryWord.toLowerCase());
		Object tmpRow;
		
		Map<String,Integer> mappedRow = (Map<String,Integer>)indexedWord.get(id); // Map query word with matches cardinality 
		mappedRow = sortByValue(mappedRow);
		System.out.println("\nSearch for "+queryWord);
		if(tsrw!=null){
			TWord queryAsTWord = (TWord) words.get(id);
			tsrw.setRow("Search for "+queryWord+" (freq: "+queryAsTWord.getFrequency()+")");
		}
		
		int i=1;
		for (Iterator<Map.Entry<String,Integer>> wordIter =mappedRow.entrySet().iterator(); i<=k;){
			Map.Entry pairs = (Map.Entry)wordIter.next();
			String word = (String) pairs.getKey();
			Integer matches = (Integer) pairs.getValue();
			System.out.println(" word "+String.valueOf(i)+": "+word+" matches: "+matches);
			if(tsrw!=null)
				tsrw.setRow(" word "+String.valueOf(i)+": "+word+" matches: "+matches);
			i++;
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
	
	private static Map sortByValue(Map map) {
	     List list = new LinkedList(map.entrySet());
	     Collections.sort(list, new Comparator() {
	          public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	              .compareTo(((Map.Entry) (o1)).getValue());
	          }
	     });

	    Map result = new LinkedHashMap();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	} 
	
	private void sortIndexer(Integer[] row, int[] wordsMaxQueue,int[] colIndexes) {
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
	
	static <K,V extends Comparable<? super V>> 
	    List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {
		
		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
		
		Collections.sort(sortedEntries, 
		    new Comparator<Entry<K,V>>() {
		        @Override
		        public int compare(Entry<K,V> e1, Entry<K,V> e2) {
		            return e2.getValue().compareTo(e1.getValue());
		        }
		    }
		);
		
		return sortedEntries;
	}
	
	private void shiftDigits(int[] wordsMax,int index){
		if(index<wordsMax.length){
			shiftDigits(wordsMax,index+1);
			wordsMax[index]=wordsMax[index-1];
		}
		
	}
	
	public void closeWarehouse() throws IOException{
		matrix.close();
	}

	public void increaseTotalSentences() {
		totalSentences++;		
	}
	
	public int getSentenceNum(){
		return totalSentences;
	}
}
