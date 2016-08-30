package hashTables;

import java.math.BigInteger;

public class DoubleHashing<K, V> extends ParentHashTable<K, V> {
	

	
	public <K, V> DoubleHashing() {
		this(10,10);
	}

	
	public <K, V> DoubleHashing(int tableSize, int overflowSize) {
		super(tableSize, overflowSize);
	}

	
	
	@Override
	protected void rehash(int factor) {
		DoubleHashing<K, V> biggerTable = new DoubleHashing<>(tableSize*factor,overflowSize);
		copyHashEntriesFrom(biggerTable);
		copyTable(biggerTable);
	}
	
	
	/**
	 * use double hashing, tableSize is prime
	 * so any integer smaller than it will be
	 * relatively prime to it.
	 */
	@Override
	protected int probe(int key, int i) {
		int h = key%(tableSize-1)+1;
		return (h*i+key)%tableSize;
	}
	
	
	/**
	 * make sure that the tableSize is prime
	 * it sets the taleSize to the next prime
	 * bigger than the given initial size
	 */
	@Override
	protected void setTablSize(int size) {
		BigInteger x = new BigInteger(size+"");
		x = x.nextProbablePrime();
		tableSize = Integer.parseInt(x.toString());
	}
	
	
	
	
}
