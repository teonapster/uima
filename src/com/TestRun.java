package com;

import java.awt.List;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.junit.Test;

import com.apache.uima.structure.TSimpleRowReader;



public class TestRun {
	
	public static void main(String [] args) throws URISyntaxException, IOException
	{
		RunModes a = new RunModes();
		if(args.length>0)
		for (int i=0;i<args.length;++i){
			//Initialization mode
			if(args[i].equals("-i")){
				try {
					a.initialize();
					//i++;
				} catch (UIMAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			//Analyze mode
			if(args[i].equals("-a")){
				a.open();
				a.analyze(args[i+1],10);
				//i+=2;
			}
			if(args[i].equals("-f")){
				TSimpleRowReader tsrr = new TSimpleRowReader(args[i+1]);
				//First row must be K number
				ArrayList<String> words = (ArrayList<String>)tsrr.getRows();
				int k=0;
				a.open();
				for(int j=0;j<words.size();++j){
					if(j==0){
						k=Integer.parseInt(words.get(j));
						j++;
					}
					a.analyze(words.get(j).toLowerCase(),k);
				}
				
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
