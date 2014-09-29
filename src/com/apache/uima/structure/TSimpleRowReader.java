package com.apache.uima.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TSimpleRowReader {

	public List<String> rows = new ArrayList<String>();
	
	
	public TSimpleRowReader(String filepath) throws IOException{
		InputStream    fis;
		BufferedReader br;
		String         line;
        
		fis = new FileInputStream(filepath);
		br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		while ((line = br.readLine()) != null) {
		   rows.add(line);
		}

		// Done with the file
		br.close();
		br = null;
		fis = null;
	}
	
	public List<String> getRows(){
		return rows;
	}
}
