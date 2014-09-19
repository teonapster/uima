package com;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.XMLInputSource;

import com.apache.uima.structure.TMassiveReader;
import com.apache.uima.structure.TWordNotFound;
import com.apache.uima.structure.TWordWarehouse;

public class RunModes{
	
	public RunModes(){
	}
	
	public void initialize()throws UIMAException, IOException, URISyntaxException {
		File specFile = new File("resources/SentenceWordAnnotator.xml");
		XMLInputSource in = new XMLInputSource(specFile);
		ResourceSpecifier specifier = UIMAFramework.getXMLParser().
		parseResourceSpecifier(in);
		// for import by name... set the datapath in the ResourceManager
		AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(specifier);
		TMassiveReader tmr = new TMassiveReader(ae);
		tmr.readMassiveFile();
		TWordWarehouse tww = TWordWarehouse.getInstance();
		tww.initializeAdjMatrix();
		tww.saveWarehouse();
	}
	
	public  void analyze(String queryWord, int k){
		TWordWarehouse tww = TWordWarehouse.getInstance();
		try {
			tww.openWarehouse();
		} catch (IOException e) {
			System.out.println("Please initialize knowledge base first");
			return;
		}
		try {
			tww.analyse(queryWord,k);
		} catch (TWordNotFound e) {
			System.out.println(e.getMessage());
		}
	}
}
