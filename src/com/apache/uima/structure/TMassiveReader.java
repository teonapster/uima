package com.apache.uima.structure;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class TMassiveReader {
	private AnalysisEngine ae;
	private TBuffer buffer;
	private final ClassLoader resourcer = this.getClass().getClassLoader();
	
	public TMassiveReader(AnalysisEngine ae){
		this.ae = ae;
		this.buffer = TBuffer.getInstance();
	}
	
	public AnalysisEngine getAE(){
		return ae;
	}
	
	public void readMassiveFile() throws URISyntaxException, IOException, 
						ResourceInitializationException, AnalysisEngineProcessException{
		File f = new File("resources/text2.xml");
		LineIterator it = FileUtils.lineIterator(f, "UTF-8");
		try {
			
		    while (it.hasNext()) {
		        String line = it.nextLine();
		        if(!buffer.push(line)){
					JCas cas = ae.newJCas();
			        cas.setDocumentText(buffer.pop());
			        ae.process(cas);
					buffer.push(line);
		        }
		        	
		        
		        // do something with line
		    }
		} finally {
		    LineIterator.closeQuietly(it);
		}
	}
}
