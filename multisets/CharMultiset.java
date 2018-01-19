package multisets;


import java.util.Hashtable;
import java.util.Map;

public class CharMultiset {
	private Map<Character,Integer> data;
	
	public CharMultiset(){
		data = new Hashtable<Character, Integer>();
	}
	
	public void put(Character key){
		if(data.containsKey(key)){
			Integer toPut = data.get(key).intValue() + 1;
			data.put(key, toPut);
		}else{
			data.put(key, new Integer(1));
		}		
	}
	
	public int getAmountOf(Character key){
		Integer retrieved = data.get(key);
		int amount = 0;
		if(retrieved != null)
			amount = retrieved.intValue();
		return amount;
	}
	
	public Character[] getKeysArray(){
		Character[] arr = new Character[data.keySet().size()];
		
		int i = 0;
		for(Character key: data.keySet()){
			arr[i] = key;
			i++;
		}
		
		return arr;
	}
	
}
