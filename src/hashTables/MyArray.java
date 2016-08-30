package hashTables;

public class MyArray<E> {
	
	private Object[] array;
	int size; 
    public MyArray(int size){
    	array = new Object[size]; 
    	this.size = size;
    }
    
    public void put(int index,E element) {
    	array[index] = element;
    }
    
    @SuppressWarnings("unchecked")
	public E get(int index) {
    	return (E)array[index];
    }
    
    public int size() {
    	return size;
    }
	
    @Override
    public String toString() {
    	String result = "[";
    	for(int i = 0 ;i < size-1; i++)
    		result += array[i]+""+i+" "+",";
    	result += array[size-1]+"]";
    	return result;
    }
}
