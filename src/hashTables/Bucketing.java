package hashTables;

import java.util.ArrayList;

import sun.org.mozilla.javascript.ast.ReturnStatement;




public class Bucketing<K, V> implements HashTable<K, V>{

	private MyArray<Entry<K, V>> buckets;
	private MyArray<Entry<K, V>> overflow;
	
	private double loadFactor = .75;
	private int entriesNumber;
	private int bucketsNumber;
	private int bucketSize;
	private int tableSize;
	private int overflowSize;
	
	private int collisions;
	
	
	public <K, V> Bucketing() {
		this(5,10,2*5);
	}
	
	public <K, V> Bucketing(int bucketSize,int initialBucketsNumber, int overflowSize) {
		
		if(bucketSize < 5)
			bucketSize = 5;
		if(overflowSize < 10)
			overflowSize = 10;
		if(initialBucketsNumber < 10)
			initialBucketsNumber = 10;
		
		this.bucketSize  = bucketSize;
		this.bucketsNumber = initialBucketsNumber;
		this.overflowSize = overflowSize;
		this.tableSize = this.bucketSize*this.bucketsNumber;
		entriesNumber = 0;
		collisions = 0;
		buckets = new MyArray<>(tableSize);
		overflow = new MyArray<>(overflowSize);
		
	}
	
	
	@Override
	public void put(K key, V value) {
		
		int r[] = getLocation(key);//search for the key
		
		if(r[0] == IN_BUCKETS){
			buckets.get(r[1]).value = value;
			return;
		}
		if(r[0] == IN_OVERFLOW){
			overflow.get(r[1]).value = value;
			return;
		}
		
		boolean done = false;
		
		//check the home slot first 
		int index = hashFunction(key);
		if(buckets.get(index) == null || buckets.get(index).tombStone) {
			buckets.put(index, new Entry<K, V>(key, value));
			done = true;
		}
		
		collisions++;
		if(!done){
			//let's fined a place for the new entry as it doesn't exist in our table
			//first check the buckets for an empty slot including tombeStones
			int start = index - index % bucketSize;//starting index of the bucket where the key should be
			int end  = start+bucketSize;
			
			for(int i = start;i < end; i++) {
				if(i == index)
					continue;
				
				if(buckets.get(i) == null || buckets.get(i).tombStone) {
					//empty slot is found 
					buckets.put(i,new Entry<K, V>(key, value));
					done = true;
					break;
				}
				collisions++;
			}
		}
		
		//if you are here then there is no free slot in the buckets
		//so we check the overflow
		if(!done){
			
			for(int i = 0; i < overflowSize && !done; i++) {
				if(overflow.get(i) == null || overflow.get(i).tombStone) {
					//empty slot is found 
					overflow.put(i,new Entry<K, V>(key, value));
					done = true;
				}
			}
		}
		
		if(!done)
			throw new OutOfMemoryError("not enough memory");
		
		entriesNumber++;
		if((double)entriesNumber/tableSize > loadFactor)
			rehash(2);
		
	}

	@Override
	public V get(K key) {
		
		//search for the key
		int r[] = getLocation(key);
		if(r[0] == NOT_IN_TABLE)
			return null;
		else if(r[0] == IN_BUCKETS)
			return buckets.get(r[1]).value;
		else
			return overflow.get(r[1]).value;
	}

	
	@Override
	public void delete(K key) {
		
		int r[] = getLocation(key);
		
		if(r[0] == NOT_IN_TABLE)
			return;
		
		else if(r[0] == IN_BUCKETS)
			buckets.get(r[1]).tombStone = true;
		else
			overflow.get(r[1]).tombStone = true;
		entriesNumber--;
	}

	@Override
	public boolean contains(K key) {
		//search for key in table
		return getLocation(key)[0] != NOT_IN_TABLE;
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
			if(buckets.get(i) != null && !buckets.get(i).tombStone) {
				result.add(buckets.get(i).key);
		}
		
		for(int i = 0; i < overflowSize; i++) {
			if(overflow.get(i) != null && !overflow.get(i).tombStone) 
				result.add(overflow.get(i).key);
		}
		
		return result;
	}
	
	
	//////////////////////////////////////////////////////////////////////////
	
	private static final int NOT_IN_TABLE = 0;
	private static final int IN_BUCKETS  = 1;
	private static final int IN_OVERFLOW = 2;
	
	
	/**
	 * return location of key in table, there is three possibilities
	 * 1 - the key is in the buckets , in this situation searchResult[0] = IN_BUCKETS
	 * 2 - the key is in the overflow, in this situation searchResult[0] = IN_OVERFLOW
	 * 3 - the key isn't in the table and searchResult[0] = NOT_IN_TABLE
	 * searchResult[1] = is the index in the array
	 * @return notInTable,inBuckets,inOverflow in index 0. index 1 is index in array
	 */
	private int[] getLocation(K searchKey) {
		
		int key = searchKey.hashCode();
		int searchResult[] = new int[2];
		
		
		//search in buckets first
		int index = hashFunction(searchKey);
		
		if(buckets.get(index) == null) {//it was never inserted
			searchResult[0] = NOT_IN_TABLE;
			return searchResult;
		}
		
		//very lucky, the key is found in it's home location
		else if(buckets.get(index).hashCode == key && !buckets.get(index).tombStone){
			searchResult[0] = IN_BUCKETS;
			searchResult[1] = index;
			return searchResult;
		}
		
		//search the bucket for the key may be it's there
		
		int start = index - index % bucketSize;//starting index of the bucket where the key should be
		int end  = start+bucketSize;
		for(index = start; index < end; index++) {
			if(buckets.get(index) == null) {//key is not in table
				searchResult[0] = NOT_IN_TABLE;
				return searchResult;
			}
			else if(!buckets.get(index).tombStone && buckets.get(index).hashCode == key ){
				//we are lucky to find the key
				searchResult[0] = IN_BUCKETS;
				searchResult[1] = index;
				return searchResult;
			}
		}//end of for
	
		
	    
		//it's not in the buckets maybe it's in the overflow area
		Entry<K, V> entry;
		for(int i = 0 ; i < overflowSize; i++) {
			entry = overflow.get(i);
			if(entry == null){
				searchResult[0] = NOT_IN_TABLE;
				break;
			}
			
			if(entry.tombStone == false && entry.hashCode == key) {
				searchResult[0] = IN_OVERFLOW;
				searchResult[1] = i;
				break;
			}
		}
		
	    
	    
		return searchResult;
	}

	
	/**
	 * increase buckets size and overflow size by factor
	 * @param factor: increasing factor
	 */
	private void rehash(int factor) {
		
		//create new hash table and fill it with current entries 
		Bucketing<K, V> biggerTable = new Bucketing<>(bucketSize*2,bucketsNumber*2,overflowSize*2);
		for(int i = 0; i < tableSize; i++) {
			if(buckets.get(i) != null && !buckets.get(i).tombStone) {
				biggerTable.put(buckets.get(i).key, buckets.get(i).value);
			}
		}
		for(int i = 0; i < overflowSize; i++) {
			if(overflow.get(i) != null && !overflow.get(i).tombStone) {
				biggerTable.put(overflow.get(i).key, overflow.get(i).value);
			}
		}
		
		buckets = biggerTable.buckets;
		overflow = biggerTable.overflow;
		
		bucketsNumber = biggerTable.bucketsNumber;
		overflowSize  = biggerTable.overflowSize;
		tableSize = biggerTable.tableSize;
		bucketSize = biggerTable.bucketSize;
		
	}//end of rehash
	
	
	private int hashFunction(K key) {
		return Math.abs(key.hashCode()%tableSize);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////	
	public void print() {
		System.out.println("numbe of entries : "+entriesNumber);
		System.out.println("table size       : "+tableSize);
		System.out.println("load factor      : "+(double)entriesNumber/tableSize);
		System.out.println(buckets);
		System.out.println(overflow);
	}
	
	public String analysis() {
		return "collision : "+collisions+", table size : "+tableSize+" ,overflow : "+overflowSize;
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
	
	
}//end of Bucketing
