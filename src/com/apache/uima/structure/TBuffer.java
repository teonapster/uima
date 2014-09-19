package com.apache.uima.structure;

public class TBuffer {
	private static volatile TBuffer instance = null;
	private String text=" ";
	private boolean isFull= false;
	
	public TBuffer(){
		
	}
	
	public static TBuffer getInstance(){
		if(instance==null)
			synchronized(TBuffer.class){
				//Double check
				if(instance==null)
					instance= new TBuffer();
			}
		return instance;
	}
	
	public boolean push(String t){
		//If String is full erase it
		if(isFull){
			text=" ";
			isFull=false;
		}
		
		if(text.length()+t.length()<Integer.MAX_VALUE/1000000){
			text+=t;
			return true;
		}
		else
			return false;
	}
	
	public String pop(){
		System.out.println("String picked with size"+text.length());
		isFull=true;
		return text;
	}
}
