package com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

public class SentenceAnnotator extends JCasAnnotator_ImplBase{
	
	private Pattern sentencePattern = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)");
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		//Get document text
		String docText = aJCas.getDocumentText();
		
		//Search for sentences 
		Matcher matcher = sentencePattern.matcher(docText);
		int pos = 0;
		while(matcher.find(pos)){
			//foudn one sentence annotation
			Sentence annotation = new Sentence(aJCas);
			annotation.setBegin(matcher.start());
			annotation.setEnd(matcher.end());
			annotation.setSentence("Sentence");
			annotation.addToIndexes();
			pos = matcher.end();
		}
	}

}
