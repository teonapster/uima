package com.apache.uima.structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

public class LinkedListTest {
    @Test
    public void getSetMatrix() throws IOException {
    	Runtime runtime = Runtime.getRuntime();
    	 final long used0 = runtime.totalMemory()-runtime.freeMemory();
    	LinkedList ll = new LinkedList();
    	 //ArrayList<Double> ll = new ArrayList<Double>();
    	 long maxSize = 70000*70000;
    	 for (int i = 0; i < 70000; i++){
        	 double total=0;
        	 long startCycle = System.currentTimeMillis();
        	 for (int j = 0; j < 70000; j++){
        		 ll.add(i);
        		 total = ((i+1.0)*(j+1.0)*100.0)/(double)(70000*70000);
        		 
        	 }
        	 System.out.println(total+"%"+ " Cycle took: "+(System.currentTimeMillis()-startCycle)/1000.0+" sec" +" "+4L);
         }
    	 long used = runtime.totalMemory()-runtime.freeMemory();
    	 used = used-used0; 
    	 System.out.printf("Heap used is %,d KB%n", used / 1024+" "+4L);
    } 
    
    private long usedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
