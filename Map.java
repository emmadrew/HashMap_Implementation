package emmadrew_HashMap;

public interface Map {
	public Value put (String k, Value v); 
	public Value get (String k);            
	public void remove (String k);          
	public boolean hasKey(String k);        
	public int size();
}
