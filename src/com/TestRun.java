package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.uimafit.util.JCasUtil.select;

import org.apache.uima.TokenAnnotation;
import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.XMLInputSource;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.xml.sax.SAXException;
import org.apache.uima.examples.tagger.HMMTagger;


public class TestRun {

	
	public static void main(String[] args) throws UIMAException, IOException  {
		//TestRun.descriptorGenerate();
		
		//Try simple with uima
		File specFile = new File("/home/Data/workspace/StructedInfo/src/com/SentenceWordAnnotator.xml");
			XMLInputSource in = new XMLInputSource(specFile);
			ResourceSpecifier specifier = UIMAFramework.getXMLParser().
			    parseResourceSpecifier(in);
			// for import by name... set the datapath in the ResourceManager
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(specifier);
			JCas cas = ae.newJCas();
			cas.setDocumentText("This is my document. Also this is my sister. By the way try it yourself? Are you sure? I definitely believe in our world");
			ae.process(cas);
			
			/*for (TokenAnnotation annotation : select(cas, TokenAnnotation.class)) {
                System.out.println(annotation.getCoveredText() + "\tannotation = "
                                + annotation.getTokenType());
                annotation.getFeatureValueAsString(annotation.getfea)
                for (HMMTagger wordAnnotation : select(cas, HMMTagger.class)) {
                	List features = wordAnnotation.getType().getFeatures();
                	Feature feature = (Feature) features.get(4);
                    System.out.println(wordAnnotation.getCoveredText() + "\tpos = "
                                    + wordAnnotation.getFeatureValue(feature));
    			}
			}*/
			
			
        
        
        
	}

}
