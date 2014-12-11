package com.apache.uima.structure;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TLargeIntMatrix implements Closeable,Serializable {
	private static final int MAPPING_SIZE = 1 << 30;
	private static final int NUM_BYTES = 4;
    private final RandomAccessFile raf;
    private final int width;
    private final int height;
    private int xIndex,yIndex;
    private final ArrayList<MappedByteBuffer> mappings = new ArrayList<MappedByteBuffer>(); //TODO SERIALIZE

    public TLargeIntMatrix(String filename, int width, int height) throws IOException {
    	 this.raf = new RandomAccessFile(filename, "rw");
         try {
             this.width = width;
             this.height = height;
             long size = 4L * width * height;
             long offset =0;
             for (offset = 0; offset < size; offset += MAPPING_SIZE) {
                 long size2 = Math.min(size - offset, MAPPING_SIZE);
                 mappings.add(raf.getChannel().map(FileChannel.MapMode.READ_WRITE, offset, size2));
             }
             System.out.println("pages: "+mappings.size());
         } catch (IOException e) {
             raf.close();
             throw e;
         }
    }

    public ArrayList<MappedByteBuffer> getMappings(){
    	return mappings;
    }
    protected long position(int x, int y) {
        return (long) y * width + x;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int[] getRow(int row){
    	int [] array = new int[this.height];
        assert row >= 0 && row < height;
        
        for(int i=0; i<array.length;++i){
        	 long p = position(row,i) * NUM_BYTES;
             int mapN = (int) (p / MAPPING_SIZE);
             int offN = (int) (p % MAPPING_SIZE);
             array[i]=mappings.get(mapN).getInt(offN);
        }
        
        return array;
    }
    
    public int[] getCol(int col){
    	int [] array = new int[this.width];
        assert col >= 0 && col < width;
       
        for(int i=0; i<array.length;++i){
        	 long p = position(i,col) * NUM_BYTES;
             int mapN = (int) (p / MAPPING_SIZE);
             int offN = (int) (p % MAPPING_SIZE);
             array[i]=mappings.get(mapN).getInt(offN);
        }
        return array;
    }
    
    
    public int get(int x, int y) {
        assert x >= 0 && x < width;
        assert y >= 0 && y < height;
        long p = position(x, y) * NUM_BYTES;
        int mapN = (int) (p / MAPPING_SIZE);
        int offN = (int) (p % MAPPING_SIZE);
        return mappings.get(mapN).getInt(offN);
    }

    /**
     * TODO
     * @param num
     */
    public void add(int num){
    	assert xIndex >= 0 && xIndex < width;
        assert yIndex >= 0 && yIndex < height;
    	xIndex++;
    	yIndex++;
    	
    }
    public void set(int x, int y, int d) {
        assert x >= 0 && x < width;
        assert y >= 0 && y < height;
        long p = position(x, y) * NUM_BYTES;
        int mapN = (int) (p / MAPPING_SIZE);
        int offN = (int) (p % MAPPING_SIZE);
        MappedByteBuffer mbb = mappings.get(mapN);
        try{
        	mbb.putInt(offN, d);
        }
        catch(java.lang.InternalError ie){
        	System.out.println("Error in "+mapN+"th page");
        }
        
    }

    public void close() throws IOException {
        for (MappedByteBuffer mapping : (ArrayList<MappedByteBuffer>)mappings)
            clean(mapping);
        raf.close();
    }

    private void clean(MappedByteBuffer mapping) {
        if (mapping == null) return;
        Cleaner cleaner = ((DirectBuffer) mapping).cleaner();
        if (cleaner != null) cleaner.clean();
    }
}


