package hashTables;

public class QuadraticProbing<K, V> extends ParentHashTable<K, V> {
	

	
	public <K, V> QuadraticProbing() {
		this(10,10);
	}

	
	
	public <K, V> QuadraticProbing(int tableSize, int overflowSize) {
		
		super(tableSize, overflowSize);
		
	}


////////////////////////////////////////////////////////////////////////////////////////////
	
	
			
	@Override
	protected void rehash(int factor) {
		QuadraticProbing<K, V> biggerTable = new QuadraticProbing<>(tableSize*factor,overflowSize);
		copyHashEntriesFrom(biggerTable);
		copyTable(biggerTable);
	}
	
	@Override
	protected int probe(int key, int i) {
		return (key+i*i)%tableSize;
	}
	
	
}
