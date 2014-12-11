package com;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.junit.Test;

import com.apache.uima.structure.TSimpleRowReader;
import com.apache.uima.structure.TSimpleRowWriter;

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
			if(args[i].equals("-ca")){
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	
				a.open();
				boolean run = true;
				while(run){
					System.out.println("Give a query word");
					String query = br.readLine();
					System.out.println("Give K");
					String sk= br.readLine();
					Integer k=Integer.parseInt(sk);
					a.analyze(query, k);
				}
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
			if(args[i].equals("-fc")){
				long start = System.currentTimeMillis();
				TSimpleRowReader tsrr = new TSimpleRowReader(args[i+1]);
				String outFile = args[i+1].substring(0, args[i+1].indexOf("."));
				TSimpleRowWriter tsrw = new TSimpleRowWriter(outFile+"Out.txt");
				//First row must be K number
				ArrayList<String> words = (ArrayList<String>)tsrr.getRows();
				int k=0;
				int totalSentences = a.open();
				tsrw.setRow("Total Sentences of Dataset: "+totalSentences);
				for(int j=0;j<words.size();++j){
					//avoid writing null or empty strings in output buffer
					if(words.get(j)!=null&&!words.get(j).equals("")){
						if(j==0){
							k=Integer.parseInt(words.get(j));
							j++;
						}
						
						if(!words.get(j).matches("^Map Name.*"))
							a.analyzeNexport(words.get(j).toLowerCase(),k,tsrw);
						else
							tsrw.setRow("\n"+words.get(j));
					}
				}
				tsrw.setRow("Analysis took "+(System.currentTimeMillis()-start)/1000.0+" sec");
				tsrw.close();
				
			}
		}
		//analyze("sun",10);
		
	}	@Test
	public void testRun() throws URISyntaxException, IOException {
		RunModes a = new RunModes();
		a.analyze("test", 10);
	}

}
