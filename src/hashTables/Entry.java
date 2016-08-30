package hashTables;

public class Entry <K, V> {
	
	K key;
	V value;
	
	int hashCode;
	boolean tombStone;
	
	public Entry(K key, V value) {
		this.key = key;
		this.value = value;
		tombStone = false;
		hashCode = key.hashCode();
	}
	
	@Override
	public String toString() {
		return "("+key+","+value+","+hashCode+")";
	}
}
