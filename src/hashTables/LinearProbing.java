package hashTables;

import java.util.ArrayList;

public class LinearProbing<K, V> extends ParentHashTable<K, V>{

	
	
	protected int stepSize;
	
	
	public <K, V> LinearProbing() {
		this(10,10);
	}

	
	
	public <K, V> LinearProbing(int tableSize, int overflowSize) {
		
		super(tableSize, overflowSize);
		stepSize = 1;
		for(int i = 2; i < tableSize; i++) {
			if (gcd(tableSize, i) == 1){
				stepSize = i;
				break;
			}
		}
		
	}

	

////////////////////////////////////////////////////////////////////////////////////////////
	
	
		
	@Override
	protected void copyTable(ParentHashTable<K, V> t) {
		super.copyTable(t);
		this.stepSize = ((LinearProbing<K, V>)t).stepSize;
	}
	
	@Override
	protected void rehash(int factor) {
		LinearProbing<K, V> biggerTable = new LinearProbing<>(tableSize*factor,overflowSize);
		copyHashEntriesFrom(biggerTable);
		copyTable(biggerTable);
	}
	
	
	@Override
	protected int probe(int key, int i) {
		return (key+i*stepSize)%tableSize;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////
	private int gcd(int a, int b) {
		if(b == 0)
			return a;
		return gcd(b,a%b);
	}
	
	
}// end of LinearProbing
