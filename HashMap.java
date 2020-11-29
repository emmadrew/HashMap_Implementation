package emmadrew_HashMap;

public interface HashMap extends Map {
	public String maxKey(); 
	public String minKey(); 
	public String[] getKeys();
	public int hash(String key, int tabSize);
	public double lambda();
	public double extend();
	public HMCell[] getTable();
}

