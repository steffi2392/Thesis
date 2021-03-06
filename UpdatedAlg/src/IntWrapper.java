public class IntWrapper {
	private int value; 
	
	public IntWrapper(int value) {
		this.value = value; 
	}
	
	public void increment() {
		value++; 
	}
	
	public Integer asInteger() {
		return new Integer(value);
	}
	
	public String toString() {
		return String.valueOf(value);
	}
}