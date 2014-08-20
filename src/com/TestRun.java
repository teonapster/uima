package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import com.apache.uima.structure.TWordWarehouse;


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
			JCas cas = ae.newJCas();
			cas.setDocumentText("Telnet is a network protocol used on the Internet or local area networks to provide a bidirectional interactive text-oriented communication facility using a virtual terminal connection. User data is interspersed in-band with Telnet control information in an 8-bit byte oriented data connection over the Transmission Control Protocol (TCP). "
+ "Telnet was developed in 1968 beginning with RFC 15, extended in RFC 854, and standardized as Internet Engineering Task Force (IETF) Internet Standard STD 8, one of the first Internet standards."
+ "Historically, Telnet provided access to a command-line interface (usually, of an operating system) on a remote host. Most network equipment and operating systems with a TCP/IP stack support a Telnet service for remote configuration (including systems based on Windows NT). However, because of serious security issues when using Telnet over an open network such as the Internet, its use for this purpose has waned significantly[citation needed] in favor of SSH."
+ "The term telnet may also refer to the software that implements the client part of the protocol. Telnet client applications are available for virtually all computer platforms. Telnet is also used as a verb. To telnet means to establish a connection with the Telnet protocol, either with command line client or with a programmatic interface. For example, a common directive might be: To change your password, telnet to the server, log in and run the passwd command. Most often, a user will be telnetting to a Unix-like server system or a network device (such as a router) and obtaining a login prompt to a command line text interface or a character-based full-screen manager.");
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
