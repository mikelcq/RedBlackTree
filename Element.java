package RedBlackTree;


public class Element {

	private int _key;
	private String _value;
	
	public Element (int k, String v){
		_key = k;
		_value = v;
	}
	
	public int getKey(){
		return _key;
	}
	
	public void setKey(int k){
		_key = k;
	}
	
	public String getValue(){
		return _value;
	}
	
	public void setValue(String v){
		_value = v;
	}
	
	@Override
	public String toString(){
		return "("+_key+" , "+ _value + ")";
	}
	
}
