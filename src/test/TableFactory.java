package test;

import hashTables.Bucketing;
import hashTables.DoubleHashing;
import hashTables.HashTable;
import hashTables.LinearProbing;
import hashTables.PseudoRandomProbing;
import hashTables.QuadraticProbing;
import hashTables.SeparateChaining;

public class TableFactory {
	
	
	@SuppressWarnings("rawtypes")
	public static HashTable getTable() {
		
		//return hash table which you want to test
		return new SeparateChaining<>();
	}
	
}
