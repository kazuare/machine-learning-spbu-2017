package multisets;


import java.util.Hashtable;
import java.util.Map;

public class IntMultiset {
	private Map<Integer,Integer> data;
	
	public IntMultiset(){
		data = new Hashtable<Integer, Integer>();
	}
	
	public void put(Integer key){
		if(data.containsKey(key)){
			Integer toPut = data.get(key).intValue() + 1;
			data.put(key, toPut);
		}else{
			data.put(key, new Integer(1));
		}		
	}
	
	public int getAmountOf(Integer key){
		Integer retrieved = data.get(key);
		int amount = 0;
		if(retrieved != null)
			amount = retrieved.intValue();
		return amount;
	}
	
	public Integer[] getKeysArray(){
		Integer[] arr = new Integer[data.keySet().size()];
		
		int i = 0;
		for(Integer key: data.keySet()){
			arr[i] = key;
			i++;
		}
		
		return arr;
	}
	
}
