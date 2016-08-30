import java.util.Hashtable;

import hashTables.Bucketing;
import hashTables.DoubleHashing;
import hashTables.Entry;
import hashTables.HashTable;
import hashTables.LinearProbing;
import hashTables.PseudoRandomProbing;
import hashTables.QuadraticProbing;
import hashTables.SeparateChaining;


public class Main {
	
	public static Integer sampleKeys[];
	public static String sampleValues[];
	public static final int sampleSize = 1000000;
	public static int tableSize;
	
	public static void setUpBeforeClass() {
		
		sampleKeys = new Integer[sampleSize];
		sampleValues = new String[sampleSize];
		
		int maxInt = 10000000;
		int x;
		for(int i = 0; i < sampleSize; i+= 2) {
			x = (int)(Math.random()*maxInt);
			sampleKeys[i] = x;
			sampleValues[i] = x+"";
		}
		
		for(int i = 1; i < sampleSize; i+= 2) {
			x = -1 * (int)(Math.random()*maxInt);
			sampleKeys[i] = x;
			sampleValues[i] = x+"";
		}
		
		Hashtable<Integer, String> table = new Hashtable<Integer, String>();
		for(int i = 0; i < sampleSize; i++)
			table.put(sampleKeys[i], sampleValues[i]);
		tableSize = table.size();
	}
	
	
	public static void main(String[] args) {
		
		long avRunTime = 0;
		long avCollision = 0;
		long avTableSize = 0;
		long avOverflow = 0;
		
		long befor,after;
		
		int runs = 20;
		for(int j = 0; j < runs; j++) {
			System.out.print("running : "+j);
			setUpBeforeClass();
			befor = System.currentTimeMillis();
			
	////////////////////////////////////////////////////////////////////////////////////		
			
			Bucketing<Integer,String> table = new Bucketing<Integer, String>();
			for(int i = 0; i < sampleSize; i++)
				table.put(sampleKeys[i], sampleValues[i]);
			
	//////////////////////////////////////////////////////////////////////////////////////		
			
			after = System.currentTimeMillis();
			
			avRunTime += after-befor;
			avCollision += table.getCollisions();
			avTableSize += table.getTableSize();
			avOverflow += table.getOverFlow();
			
			System.out.println(" .. done ");
		}
		
		avRunTime /= runs;
		avCollision /= runs;
		avTableSize /= runs;
		avOverflow /= runs;
		
		
		System.out.println("average run time   : "+avRunTime);
		System.out.println("average colision   : "+avCollision);
		System.out.println("average table size : "+avTableSize);
		System.out.println("average overflow   : "+avOverflow);
		
	}
	
	
	
}
