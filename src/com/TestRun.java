package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
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

import com.apache.uima.structure.TMassiveReader;
import com.apache.uima.structure.TWordWarehouse;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;


public class TestRun {

	
	public static void main(String[] args) throws UIMAException, IOException, URISyntaxException  {
		//TestRun.descriptorGenerate();
		
		//Try simple with uima
		//TODO Change static path
		
		File specFile = new File(TestRun.class.getResource("/com/SentenceWordAnnotator.xml").toURI());
			XMLInputSource in = new XMLInputSource(specFile);
			ResourceSpecifier specifier = UIMAFramework.getXMLParser().
			    parseResourceSpecifier(in);
			// for import by name... set the datapath in the ResourceManager
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(specifier);
			TMassiveReader tmr = new TMassiveReader(ae);
			tmr.readMassiveFile();
			TWordWarehouse tww = TWordWarehouse.getInstance();
			//tww.initializeAdjMatrix();
			//tww.saveWarehouse();
			tww.openWarehouse();
		    tww.analyse("music", 5);
	}

}
