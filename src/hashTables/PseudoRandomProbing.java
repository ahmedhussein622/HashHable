package hashTables;

public class PseudoRandomProbing<K, V> extends ParentHashTable<K, V> {
	
	
	private int perm[];//random permutation

	public <K, V> PseudoRandomProbing() {
		this(10,10);
	}

	
	public <K, V> PseudoRandomProbing(int tableSize, int overflowSize) {
		super(tableSize, overflowSize);
		perm = new int[tableSize];
		fillPerm();
	}

	
	@Override
	protected void copyTable(ParentHashTable<K, V> t) {
		super.copyTable(t);
		this.perm = ((PseudoRandomProbing<K, V>)t).perm;//copy the new perm from new hashtable
	}
	
	@Override
	protected void rehash(int factor) {
		PseudoRandomProbing<K, V> biggerTable = new PseudoRandomProbing<>(tableSize*factor,overflowSize);
		copyHashEntriesFrom(biggerTable);
		copyTable(biggerTable);
	}
	
	
	@Override
	protected int probe(int key, int i) {
		return (key+perm[i])%tableSize;
	}
	
	/**
	 * generate random numbers and put them in perm[]
	 * perm[0] = 0 , range of number is [0:tableSize-1]
	 */
	private void fillPerm() {
		
		boolean used[] = new boolean[tableSize];
		
		perm[0] = 0;
		int random;
		for(int i = 1 ; i < tableSize; i++) {
			
			do{
				random = 1+(int)(Math.random()*(tableSize-1));
			}while(used[random]);
			used[random] = true;
			perm[i] = random;
		}
		
	}

}
