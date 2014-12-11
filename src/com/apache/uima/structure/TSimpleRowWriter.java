package com.apache.uima.structure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TSimpleRowWriter {
		private List<String> rows = new ArrayList<String>();
		private FileOutputStream    fis;
		private BufferedWriter br;
		private String         line;
		
		public TSimpleRowWriter(String filepath) throws IOException{
			fis = new FileOutputStream(filepath);
			OutputStreamWriter osw = new OutputStreamWriter(fis, Charset.forName("UTF-8"));
			br = new BufferedWriter(osw);
			
		}
		
		public void setRows(List<String> lines) throws IOException{
			for(int i=0;i<lines.size();++i){
				br.write(lines.get(i));
			}
			close();
		}
		
		public void setRow(String line) throws IOException{
				br.write(line+"\n");
		}
		
		public void close() throws IOException{
			// Done with the file
			br.close();
			br = null;
			fis = null;
		}
	}
