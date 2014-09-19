package com;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.uima.UIMAException;
import org.junit.Test;


public class TestRun {
	
	public static void main(String [] args) throws URISyntaxException, IOException
	{
		RunModes a = new RunModes();
		if(args.length>0)
		for (int i=0;i<args.length;++i){
			if(args[i].equals("-i")){
				try {
					a.initialize();
				} catch (UIMAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(args[i].equals("-a")){
				a.analyze(args[i+1],10);
			}
		}
		//analyze("sun",10);
		
	}	
	@Test
	public void testRun() throws URISyntaxException, IOException{
		RunModes a = new RunModes();
		a.analyze("test",10);
	}

	
}
