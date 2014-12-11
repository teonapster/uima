package com.apache.uima.structure;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.XMLInputSource;
import org.junit.Test;

public class TLargeIntMatrixTest {
    @Test
    public void getSetMatrix() throws IOException {
    	 /*long start = System.nanoTime();
         final long used0 = usedMemory();
         TLargeIntMatrix matrix = new TLargeIntMatrix("/home/teonapster/ldm.test", 70000, 70000 );
         for (int i = 0; i < matrix.width(); i++){
        	 int total=0;
        	 long startCycle = System.currentTimeMillis();
        	 for (int j = 0; j < matrix.height(); j++){
        		 matrix.set(i, j, i);
        		 total = ((i+1)*(j+1)*100)/(matrix.width()*matrix.height());
        		 
        	 }
        	 System.out.println(total+"%"+ " Cycle took: "+(System.currentTimeMillis()-startCycle)/1000+" sec");
         }
             
         long time = System.nanoTime() - start;
         final long used = usedMemory() - used0;
         if (used == 0)
             System.err.println("You need to use -XX:-UseTLAB to see small changes in memory usage.");
         System.out.printf("Setting the diagonal took %,d ms, Heap used is %,d KB%n", time / 1000 / 1000, used / 1024);
         matrix.close();*/
    	System.out.println("sea with hash: "+ TStringTools.identizer("sea"));
    }

    private long usedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
   
}
