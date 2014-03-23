package com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

public class WordAnnotator extends JCasAnnotator_ImplBase{
	
	private Pattern wordPattern = Pattern.compile("\\S+");
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		//Get document text
		String docText = aJCas.getDocumentText();
		
		//Search for sentences 
		Matcher matcher = wordPattern.matcher(docText);
		int pos = 0;
		while(matcher.find(pos)){
			//foudn one sentence annotation
			Word annotation = new Word(aJCas);
			annotation.setBegin(matcher.start());
			annotation.setEnd(matcher.end());
			annotation.setWord("Word");
			annotation.addToIndexes();
			pos = matcher.end();
		}
	}

}
