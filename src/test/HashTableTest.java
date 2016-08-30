package test;

import static org.junit.Assert.*;
import org.junit.Test;
import hashTables.*;

public class HashTableTest {

	
	@Test
	public void testOne() {
		
		//put hash table to test in here
		HashTable<Integer, String> myTable = TableFactory.getTable();
		
		assertEquals(myTable.isEmpty(), true);
		//add some values and check they are correct
		
		for(int i = 0; i < 10; i++) {
			myTable.put(i, ""+i);
		}
		
		assertEquals(myTable.isEmpty(), false);
		
		Iterable<Integer> mine = myTable.keys();
		int count = 0;
		for (Integer integer : mine) {
			try{
				assertEquals(integer,(Integer)integer.parseInt(myTable.get(integer)));
			}catch(Exception e){e.printStackTrace();}
			count++;
		}
		assertEquals(count, 10);
		assertEquals(myTable.size(), 10);
		
	////////////////////////////////////////////////////////////////////////////////////////
		
		//add the same values again and check that the size is still constant
		for(int i = 0; i < 10; i++) 
			myTable.put(i, ""+i);
			
		
		mine = myTable.keys();
		count = 0;
		for (Integer integer : mine) {
			assertEquals(integer,(Integer)integer.parseInt(myTable.get(integer)));
			count++;
		}
		assertEquals(count, 10);
		assertEquals(myTable.size(), 10);
	//////////////////////////////////////////////////////////////////////////////////////
		
		//now lets add lot of entries
		int size = 2000;
		for(int i = 0; i <= size; i++) 
			myTable.put(i, ""+i);
		
		mine = myTable.keys();
		count = 0;
		for (Integer integer : mine) {
			
			try{
				assertEquals(integer,(Integer)integer.parseInt(myTable.get(integer)));
			}catch(Exception e){e.printStackTrace();break;}
			count++;
		}
		
		assertEquals(count, size+1);
		assertEquals(myTable.size(), size+1);
		assertEquals(myTable.isEmpty(), false);
		
		//test deleting, delete every thin in the table
		for(int i = 0; i <= size; i++) {
			myTable.delete(i);
		}
		
		mine = myTable.keys();
		count = 0;
		for (Integer integer : mine) {
			count++;
			assertEquals(integer,(Integer)integer.parseInt(myTable.get(integer)));
		}
		
		assertEquals(count, 0);
		assertEquals(myTable.isEmpty(), true);
		
		//more adding and deletion and getting and contain's
		size = 50000;
		for(int i = size; i >= 0; i--)
			myTable.put(i, i+"");
		
		for(int i = 0; i <= size; i+= 2) {
			assertEquals(myTable.contains(i), true);
			myTable.delete(i);
			assertEquals(myTable.contains(i), false);
		}
		
		for (Integer integer : myTable.keys()) {
			myTable.put(integer, (integer*3+32)+"");
		}
		
		for (Integer integer : myTable.keys()) {
			assertEquals((Integer)(integer*3+32),(Integer)integer.parseInt(myTable.get(integer)));
			myTable.delete(integer);
		}
		assertEquals(myTable.size(), 0);
		for(int i = 0; i < 10; i++) 
			myTable.put(i, ""+i);
		for(int i = 0; i < 10; i++)
			assertEquals(myTable.contains(i), true);
		assertEquals(myTable.size(), 10);
	}
	
	@Test
	public void testTwo() {
		
		HashTable<String, String> myTable = TableFactory.getTable();
		assertEquals(myTable.isEmpty(), true);
		for(int i = 0 ; i < 10; i++)
			myTable.put(""+i, ""+(i*34+5));
		assertEquals(myTable.isEmpty(), false);
		
		int count = 0;
		for ( String s: myTable.keys()) {
			count++;
			try {
				assertEquals(Integer.parseInt(s)*34+5, Integer.parseInt(myTable.get(s)));
			}catch(Exception e){e.printStackTrace();break;}
			
		}
		assertEquals(count, 10);
		
		//try inserting the same values and check that the size didn't change 
		for(int i = 0 ; i < 10; i++)
			myTable.put(""+i, ""+(i*34+5));
		
		count = 0;
		for ( String s: myTable.keys()) {
			count++;
			try {
				assertEquals(Integer.parseInt(s)*34+5, Integer.parseInt(myTable.get(s)));
			}catch(Exception e){e.printStackTrace();break;}
			
		}
		assertEquals(count, 10);
		assertEquals(myTable.size(), 10);
		//////////////////////////////////////////////////////////////////////////////////
		//let's add lots of entries
		int size = 2000;
		
		for(int i = 0 ;i < size; i++)
			myTable.put(""+i, ""+(-i*55+23));
		assertEquals(myTable.size(), size);
		count = 0;
		for ( String s: myTable.keys()) {
			count++;
			try {
				assertEquals(-Integer.parseInt(s)*55+23, Integer.parseInt(myTable.get(s)));
			}catch(Exception e){e.printStackTrace();break;}
			
		}
		assertEquals(count, size);
		assertEquals(myTable.size(), size);
		
		for(int i = 0; i < size; i += 2)
			myTable.delete(""+i);
		
		assertEquals(myTable.size(), size/2);
		
		count = 0;
		int x;
		for (String s : myTable.keys()) {
			count++;
			x = Integer.parseInt(s);
			assertEquals(x % 2, 1);
			try {
				assertEquals(-x*55+23, Integer.parseInt(myTable.get(s)));
			}catch(Exception e){e.printStackTrace();break;}
		}
		assertEquals(count, size/2);
		
		//let's remove everything and check the table is empty
		for (String s : myTable.keys()) {
			myTable.delete(s);
		}
		assertEquals(true, myTable.isEmpty());
		assertEquals(0, myTable.size());
		
	}//end of test two

	

	
}//end of test
