package emmadrew_HashMap;

public class HashMap_imp implements HashMap { 
	  HMCell[] tab;
	  int nelts;

	  HashMap_imp (int num) { 
	    this.tab = new HMCell[num];
	    for (int i = 0; i < num; i++) {
	    	tab[i] = null;
	    }
	    this.nelts = 0; 
	  }

	  public int hash (String key, int tabSize) {
	    int hval = 7;
	    for (int i = 0; i <key.length(); i++) {
	      hval = (hval * 31) + key.charAt(i);
	    }
	    hval = hval % tabSize;
	    if (hval < 0) {
	    	hval += tabSize;
	    }
	    return hval;
	  }

	@Override
	public HMCell[] getTable() {
		return this.tab;
	}

	@Override
	public Value put(String k, Value v) {
		// check for null input
		if (k == null || v == null) {
			throw new RuntimeException("k or v cannot be null");
		}
		
		// entry of new HMCell
		HMCell entry = new HMCell_imp (k, v);
		
		// hashcode & index where entry will be stored
		int index = hash(k, this.tab.length);
		// int index = hashCode % this.tab.length;
		if (this.tab[index] != null) {
			// if the key is not already there
			if (!hasKey(k)) {
				HMCell current = this.tab[index];
				int i = 0;
				while (current != null) {
					i++;
					current = current.getNext();
				}
				
				current = this.tab[index];
				for (int j = 0; j < i - 1; j++) {
					current = current.getNext();
				}
				current.setNext(entry);
				this.nelts++;
				
				if (lambda() >= 1) {
					extend();
				}
				//  key is not already in structure
			} else {
				HMCell current = this.tab[index];
				while (current != null) {
					if (current.getKey().equals(k)) {
						Value oldValue = current.getValue();
						current.setValue(entry.getValue());
						return oldValue;
					}
					current = current.getNext();
				}
			}
			// index is null
		} else {
			this.tab[index] = entry;
			this.nelts++;
			
			if (lambda() >= 1) {
				extend();
			}
		}
		return null;
	}

	@Override
	public Value get(String k) {
		if (k == null) {
			throw new RuntimeException("k cannot be null");
		}
		
		if (this.nelts == 0) {
			return null;
		}

		// generate hashcode and index where k would be stored
		int index = hash(k, this.tab.length);
		// int index = hashCode % this.tab.length;
		
		// current is the cell at given index
		HMCell current = this.tab[index];
		
		// iterate thru list at index & check if keys are equal
		while (current != null) {
			if (current.getKey().equals(k)) {
				return current.getValue();
			}
			current = current.getNext();
		}
		return null;
	}

	@Override
	public void remove(String k) {
		if (k == null) {
			throw new RuntimeException("k cannot be null");
		}
		
		if (this.nelts == 0) {
			return;
		}
		// generate hashcode and index where k will be stored
		int index = hash(k, this.tab.length);
		
		// current is the cell at given index
		HMCell current = this.tab[index];
		
		if (hasKey(k)) {
			if (current.getKey().equals(k)) {
				// first in the list, only in the list
				if (current.getNext() == null) {
					current.setKey(null);
					current.setValue(null);
					// first in list, more in list
				} else {
					HMCell temp = null;
					current.setKey(null);
					current.setValue(null);
					while (current.getNext() != null) {
							current.setValue(current.getNext().getValue());
							current.setKey(current.getNext().getKey());
							current = current.getNext();
					}
					current.setKey(null);
					current.setValue(null);
				}			
			} else {
				// it's not the first
				while (current != null && current.getNext() != null) {
					if (current.getNext().getKey().equals(k)) {
						// if it's in the middle
						if (current.getNext().getNext() != null) {
							current.setNext(current.getNext().getNext());
							// if it's at the end -- working 
						} else {
							current.setNext(null);
						}
					
					}
					current = current.getNext();
				}
			}
			this.nelts--;
		}	
	}
	
	@Override
	public boolean hasKey(String k) {
		if (k == null) {
			throw new RuntimeException("k cannnot be null");
		}
		
		if (this.nelts == 0) {
			return false;
		}
		
		// generate hashcode and index
		int index = hash(k, this.tab.length);
		// int index = hashCode % this.tab.length;
		
		// current is the list at the index
		HMCell current = this.tab[index];
		
		// check if keys are equal
		while (current != null && current.getKey() != null) {
			if (current.getKey().equals(k)) {
			return true;
			}
			current = current.getNext();
		}
		return false;
	}

	@Override
	public int size() {
		return this.nelts;
	}

	@Override
	public String minKey() {
		if (this.nelts == 0) {
			return null;
		}
		
		String[] getKeys = getKeys();
		String smallest = getKeys[0];
		for (int i = 0; i < getKeys.length; i++) {
			if (getKeys[i].compareTo(smallest) < 0) {
				smallest = getKeys[i];
			}
		}
		return smallest;
	}

	@Override
	public String maxKey() {
		if (this.nelts == 0) {
			return null;
		}
		
		String[] getKeys = getKeys();
		String largest = getKeys[0];
		for (int i = 0; i < getKeys.length; i++) {
			if (getKeys[i].compareTo(largest) > 0) {
				largest = getKeys[i];
			}
		}
		return largest;
	}

	@Override
	public String[] getKeys() {
		String[] emptyArray = new String[0];
		
		if (this.nelts == 0) {
			return emptyArray;
		}
		
		String[] array = new String[this.nelts];
		int j = 0;
		
		for (int i = 0; i < this.tab.length; i++) {
			HMCell current = this.tab[i]; // list at i
			
			while (current != null && current.getKey() != null) {
				array[j] = current.getKey();
				j++;
				current = current.getNext();
			}
		}
		return array;
	}

	@Override
	public double lambda() {
		double count = 0;
		for (int i = 0; i < this.tab.length; i++) {
			HMCell current = this.tab[i];
			while (current != null && current.getKey() != null) {
				count++;
				current = current.getNext();
			}
		}
		return (double)(count / this.tab.length);
	}

	@Override
	public double extend() {
		this.nelts = 0;
		
		HMCell[] temp = this.tab.clone();
		this.tab = new HMCell[temp.length * 2];
		
		for (int i = 0; i < temp.length; i++) {
			
			HMCell current = temp[i];
			
			while (current != null && current.getKey() != null) {
				
				put(current.getKey(), current.getValue());
				
				current = current.getNext();
			}
		}
		return lambda();
	}
}
