package hashTables;

import java.util.ArrayList;
/**
 * used to implement some common operations
 * between separateChaning,QuadraticProbing and PseudoRandom
 * hashing technique.
 * @author ahmed
 *
 * @param <K> key
 * @param <V> values
 */
public class ParentHashTable<K, V> implements HashTable<K, V>  {

	
	protected MyArray<Entry<K, V>> table;
	protected MyArray<Entry<K, V>> overflow;
	
	protected double loadFactor = .75;
	protected int entriesNumber;
	protected int tableSize;
	protected int overflowSize;
	
	private int collisions;//count number of collisions
	
	public <K, V> ParentHashTable() {
		this(10,10);
	}

	
	
	public <K, V> ParentHashTable(int tableSize, int overflowSize) {
		
		setTablSize(tableSize);
		setOverflowSize(overflowSize);
		
		
		entriesNumber = 0;
		collisions = 0;
		
		table = new MyArray<>(this.tableSize);
		overflow = new MyArray<>(this.overflowSize);
		
	}
	
	
	/**
	 * check if the key is already in the table
	 * if so then it just changes it value.
	 * if the key isn't in table the it find free location for it
	 * using probe function (which will be overridden in child class) and
	 * hash function (which will also be overridden)
	 * if there is no enough space in the main table it goes to overflow
	 * if there is no space at all OutOfMemoryError exception will be throw
	 */
	@Override
	public void put(K key, V value) {
		
		//search of the key if it's already in the table
		int location[] = getLocation(key);
		if(location[0] == IN_TABLE) {
			table.get(location[1]).value = value;
			return;
		}
		if(location[0] == IN_OVERFLOW) {
			overflow.get(location[1]).value = value;
			return;
		}
		
		//if you are here then the key wasn't in the table
		//try to find a place for the key in the table
		//use linear probing
		int homeLocation = hashFunction(key);//hashing function
		int currentLocation;
		Entry<K, V> entry;
		for(int i = 0 ; i < tableSize; i++) {
			currentLocation = probe(homeLocation, i);
			entry = table.get(currentLocation);
			if(entry == null || entry.tombStone) {// a free space has been found
				table.put(currentLocation, new Entry<K, V>(key, value));
				entriesNumber++;
				if(getLoadFactor() >= loadFactor)
					rehash(2);
				return;//Mission accomplished
			}
			collisions++;//count collisions
		}
		
		//there is no space in table
		//go to overflow 
		for(int i = 0; i < overflowSize; i++) {
			entry = overflow.get(i);
			if(entry == null || entry.tombStone) {
				overflow.put(i, new Entry<K, V>(key, value));
				entriesNumber++;
				if(getLoadFactor() >= loadFactor)
					rehash(2);
				return;//Mission accomplished
			}
		}
		
		throw new OutOfMemoryError("no enough memory!");
	}// end of put

	
	

	@Override
	public V get(K key) {
		
		//search for the key
		int r[] = getLocation(key);
		if(r[0] == DOESNT_EXIST)
			return null;
		else if(r[0] == IN_TABLE)
			return table.get(r[1]).value;
		else
			return overflow.get(r[1]).value;
	}

	
	@Override
	public void delete(K key) {
		
		int r[] = getLocation(key);
		
		if(r[0] == DOESNT_EXIST)
			return;
		
		else if(r[0] == IN_TABLE)
			table.get(r[1]).tombStone = true;
		else
			overflow.get(r[1]).tombStone = true;
		entriesNumber--;
	}

	@Override
	public boolean contains(K key) {
		//search for key in table
		return getLocation(key)[0] != DOESNT_EXIST;
	}

	@Override
	public boolean isEmpty() {
		return entriesNumber == 0;
	}

	@Override
	public int size() {
		return entriesNumber;
	}

	@Override
	public Iterable<K> keys() {
		
		
		ArrayList<K> result = new ArrayList<>();
		for(int i = 0; i < tableSize; i++) 
			if(table.get(i) != null && !table.get(i).tombStone) {
				result.add(table.get(i).key);
		}
		
		for(int i = 0; i < overflowSize; i++) {
			if(overflow.get(i) != null && !overflow.get(i).tombStone) 
				result.add(overflow.get(i).key);
		}
		
		return result;
	}
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////
	protected int hashFunction(K key) {
		return Math.abs(key.hashCode()%tableSize);
	}
	
	protected int probe(int key, int i) {
		return 0;
	}
	
	/**
	 * Inserts the entries of this table to newTable
	 * this function will be used in rehashing 
	 * @param a
	 */
	protected void copyHashEntriesFrom(HashTable<K, V> newTable) {
		Entry<K, V> entry;
		for(int i = 0; i < tableSize; i++) {
			entry = table.get(i);
			if(entry != null && !entry.tombStone)
				newTable.put(entry.key, entry.value);
		}
		for(int i = 0; i < overflowSize; i++) {
			entry = overflow.get(i);
			if(entry != null && !entry.tombStone)
				newTable.put(entry.key, entry.value);
		}
	}
	
	/**
	 * copy attributes of table c to this table
	 * this function will be used in rehashing
	 * @param t
	 */
	protected void copyTable(ParentHashTable<K, V> c) {
		this.table = c.table;
		this.overflow = c.overflow;
		this.entriesNumber = c.entriesNumber;
		this.tableSize = c.tableSize;
		this.overflowSize = c.overflowSize;
	}
	
	
	/**
	 * to overridden by children classes
	 * @param factor
	 */
	protected void rehash(int factor) {
		
	}
	
	protected double getLoadFactor() {
		return (double)entriesNumber/tableSize;
	}
	
	private static final int DOESNT_EXIST = 0;//it wasn't inserted
	private static final int IN_TABLE  = 1;
	private static final int IN_OVERFLOW = 2;
	
	
	/**
	 * search in table and overflow using probe function
	 * and hash function 
	 * @param searchKey key to search by
	 * @return searchResult[0] contains : DOESNT_EXIST or IN_TABLE or IN_OVERFLOW
	 * indicating where does the key exist , searchResult[1] is the index
	 */
	protected int[] getLocation(K searchKey) {
	
		int searchResult[] = new int[2];
		searchResult[0] = DOESNT_EXIST;
		
		int key = hashFunction(searchKey);
		int code = searchKey.hashCode();
		int currentIndex;
		Entry<K, V> entry;
		
		//use linear probing to find the key if it exists
		for(int i = 0 ;i < tableSize; i++) {
			currentIndex = probe(key, i);
			entry = table.get(currentIndex);
			if(entry == null)// nothing was inserted in this location and so the 
				return searchResult;// key wasn't inserted  before
			//check if the key is in the current location
			else if(!entry.tombStone && entry.hashCode == code) {
				searchResult[0] = IN_TABLE;
				searchResult[1] = currentIndex;
				return searchResult;
			}
				
		}// end of for
				
		//key wasn't found in bucket array, check the overflow area
		for(int i = 0 ; i < overflowSize; i++) {
			entry = overflow.get(i);
			if(entry == null)// nothing was inserted in this location and so the 
				return searchResult;// key wasn't inserted  before
			else if(!entry.tombStone && entry.hashCode == key) {
				searchResult[0] = IN_OVERFLOW;
				searchResult[1] = i;
			}
		}
		
		return searchResult;
	}
	
	
////////////////////////////////////////////////////////////////////////////////////////////
	//make sure table size and overflow size is suitable
	protected void setTablSize(int size) {
		if(size < 10)
			size = 10;
		
		this.tableSize = size;
	}
	protected void setOverflowSize(int size) {
		if(size < 10)
			size = 10;
		this.overflowSize = size;
	}
//////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString() {
		String r = "\n";
		r += "table size : "+tableSize+"\n";
		r += "overflow size : "+overflowSize+"\n";
		r += "entries number : "+entriesNumber+"\n";
		r += "number of Collision : "+collisions+"\n";
		r += table+"\n";
		r += overflow+"\n\n";
		return r;
	}
	
	public int getCollisions() {
		return collisions;
	}
	public int getTableSize() {
		return tableSize;
	}
	public int getOverFlow() {
		return overflowSize;
	}
	
	public String analysis() {
		return "collision : "+collisions+", table size : "+tableSize+" ,overflow : "+overflowSize;
	}
	
}
