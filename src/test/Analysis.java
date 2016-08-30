package test;

import static org.junit.Assert.*;

import hashTables.Bucketing;
import hashTables.DoubleHashing;
import hashTables.LinearProbing;
import hashTables.PseudoRandomProbing;
import hashTables.QuadraticProbing;
import hashTables.SeparateChaining;

import java.util.Hashtable;

import org.junit.BeforeClass;
import org.junit.Test;

public class Analysis {
	
	
	public static Integer sampleKeys[];
	public static String sampleValues[];
	public static final int sampleSize = 1000000;
	public static int tableSize;
	
	@BeforeClass
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
	
	
	@Test
	public void javaHashTable() {
		Hashtable<Integer, String> table = new Hashtable<Integer, String>();
		for(int i = 0; i < sampleSize; i++)
			table.put(sampleKeys[i], sampleValues[i]);
		assertEquals(table.size(),tableSize);
	}
	
	
	@Test
	public void LinearProbingTest() {
		LinearProbing<Integer,String> table = new LinearProbing<Integer, String>();
		for(int i = 0; i < sampleSize; i++)
			table.put(sampleKeys[i], sampleValues[i]);
		assertEquals(table.size(),tableSize);
		System.out.println("linear probing    "+table.analysis());
	}
	
	@Test
	public void PseudoRandomTest() {
		PseudoRandomProbing<Integer,String> table = new PseudoRandomProbing<Integer, String>();
		for(int i = 0; i < sampleSize; i++)
			table.put(sampleKeys[i], sampleValues[i]);
		assertEquals(table.size(),tableSize);
		System.out.println("pseudoRandom      "+table.analysis());
	}
	
	@Test
	public void QuadraticProbingTest() {
		QuadraticProbing<Integer,String> table = new QuadraticProbing<Integer, String>();
		for(int i = 0; i < sampleSize; i++)
			table.put(sampleKeys[i], sampleValues[i]);
		assertEquals(table.size(),tableSize);
		System.out.println("quadraticProbing  "+table.analysis());
	}
	
	@Test
	public void doulbeHashingTest() {
		DoubleHashing<Integer,String> table = new DoubleHashing<Integer, String>();
		for(int i = 0; i < sampleSize; i++)
			table.put(sampleKeys[i], sampleValues[i]);
		assertEquals(table.size(),tableSize);
		System.out.println("doubleHashing     "+table.analysis());
	}
	
	@Test
	public void separateChainingTest() {
		SeparateChaining<Integer,String> table = new SeparateChaining<Integer, String>();
		for(int i = 0; i < sampleSize; i++)
			table.put(sampleKeys[i], sampleValues[i]);
		assertEquals(table.size(),tableSize);
		System.out.println("separateChaining  "+table.analysis());
	}
	
	@Test
	public void BucketingTest() {
		Bucketing<Integer,String> table = new Bucketing<Integer, String>();
		for(int i = 0; i < sampleSize; i++)
			table.put(sampleKeys[i], sampleValues[i]);
		assertEquals(table.size(),tableSize);
		System.out.println("bucketing         "+table.analysis());
	}

}
