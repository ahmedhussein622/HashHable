package hashTables;

import java.util.ArrayList;




public class SeparateChaining <K, V> implements HashTable<K, V>{
	
	private double loadFactor = 3.0;
	private int entriesNumber;
	private int slotsNumber;
	private MyArray<ArrayList<Entry<K, V>>> slots;
	
	private int collisions;
	
	public SeparateChaining () {
		this(10);
	}
	
	public SeparateChaining (int initialSize) {
		if(initialSize < 10)
			initialSize = 10;
		entriesNumber = 0;
		slotsNumber = initialSize;
		slots = new MyArray<>(initialSize);
		
		for(int i = 0; i < slotsNumber; i++) 
			slots.put(i, new ArrayList<Entry<K, V>>());
		
		collisions = 0;
	}
	
	
	@Override
	public void put(K key, V value) {
		
		
		if(key == null || value == null)
			throw new NullPointerException();
		
		int r[] = getLocation(key);
		
		if(r != null) {// the key is already in the table just replace the old values with the new one
			slots.get(r[0]).get(r[1]).value = value;
			return ;// there is nothing more to do here
		}
		
		int index = hashFunction(key);
		// this key doesn't exist and we need to add add it
		if(slots.get(index).size() != 0)
			collisions++;
		slots.get(index).add(new Entry<K,V>(key, value));
		entriesNumber++;
		
		//the load factor is too big and we need to re-hash the table
		if(((float)entriesNumber/slotsNumber) > loadFactor) {
			rehash(slotsNumber*2);
		}
	
	}

	
	
	@Override
	public V get(K key) {
		if(key == null)
			throw new NullPointerException();
		
		int r[] = getLocation(key);//search for the key
		
		if(r != null)//key was found 
			return slots.get(r[0]).get(r[1]).value;
		
		//couldn't find the key
		return null;
	}

	
	
	@Override
	public void delete(K key) {
		if(key == null)
			throw new NullPointerException();
		
		int r[] = getLocation(key);
		if(r == null)//key not found , nothing more to do.
			return;
		
		//key is found so let's remove it
		slots.get(r[0]).remove(r[1]);
		entriesNumber--;
	}
	
	

	@Override
	public boolean contains(K key) {
		
		return get(key) != null;
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
		
		ArrayList<K> result =  new ArrayList<K>();
		ArrayList<Entry<K, V>> bucket;
		
		//go throw the table and add all the keys found
		int i,j;
		for(i = 0; i < slotsNumber; i++) {
			bucket = slots.get(i);
			for(j = 0; j < bucket.size(); j++)
				result.add(bucket.get(j).key);
		}
		
		return result;
	}
	
	
	
	
	
////////////////////////////////////////////////////////////////////////////////////////	
	public void rehash(int newSlotsNumber) {
		
		//new table
		SeparateChaining<K, V> biggerTable = new SeparateChaining<>(newSlotsNumber);
		
		//fill the new table with the current entries in the old one
		int i,j;
		ArrayList<Entry<K, V>> bucket;
		Entry<K, V> entry;
		
		for(i = 0; i < slotsNumber; i++) {// loop throw all buckets
			bucket = slots.get(i);
			for(j = 0; j < bucket.size(); j++) {// loop throw values in each bucket
				entry = bucket.get(j);
				biggerTable.put(entry.key, entry.value);
			}
		}
		
		slots = biggerTable.slots;
		slotsNumber = biggerTable.slotsNumber;
		
	}// end of rehash
	
	
	/**
	 * find a location of a key in the table
	 * @param key the key to search for
	 * @return array with index 0 is the slot 
	 * number and index 1 the index in the bucket
	 * of that slot, null if not found
	 */
	
	private int[] getLocation(K key) {
		
		int index = hashFunction(key);
		
		int x = key.hashCode();
		ArrayList<Entry<K, V>> bucket = slots.get(index);//the bucket of the slot
		
		int i;
		for(i = 0 ; i < bucket.size(); i++) {
			if(bucket.get(i).hashCode == x)
				break;
		}
		
		if(i == bucket.size())//key not found
			return null;
		
		int r[] = new int[2];
		r[0] = index;
		r[1] = i;
		return r;
	}
	
	
	private int hashFunction(K key) {
		return Math.abs(key.hashCode()%slotsNumber);
	}
	
////////////////////////////////////////////////////////////////////////////////////////
	
	public String analysis() {
		return "collision : "+collisions+", table size : "+slotsNumber+" ,overflow : "+0;
	}
	
	public int getCollisions() {
		return collisions;
	}
	public int getTableSize() {
		return slotsNumber;
	}
	public int getOverFlow() {
		return 0;
	}
	
}//end of HashMabSeparateChaining
