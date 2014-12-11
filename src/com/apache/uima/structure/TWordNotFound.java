package com.apache.uima.structure;

public class TWordNotFound extends Exception{
	//Parameterless Constructor
    public TWordNotFound() {}

    //Constructor that accepts a message
    public TWordNotFound(String word)
    {
       super(word+" not found in our warehouse. Sorry try another one");
    }
}
