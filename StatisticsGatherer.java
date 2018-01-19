import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import multisets.CharMultiset;
import multisets.IntMultiset;

public class StatisticsGatherer {
	public static void calculateClusterPower(String filename) {
		List<Character> labels = CSVReader.getLabels(filename);
		CharMultiset counter = new CharMultiset();
		labels.forEach(x->counter.put(x));
		
		for (Character c : counter.getKeysArray()) {
			System.out.println(c + " " + counter.getAmountOf(c));
		}
	}
	
	public static void getDiffSize(String p1, String p2){
		List<Character> l1 = CSVReader.getLabels(p1);
		List<Character> l2 = CSVReader.getLabels(p2);		
		int count = 0;
		
		if (l1.size() != l2.size())
			throw new IllegalStateException();
		
		for (int i = 0; i < l1.size(); i++) {
			if(l1.get(i) != l2.get(i))
				count++;
		}
		System.out.println(count);
	}
	
	static void getTransTimeDist(List<List<StepData>> data) {
		IntMultiset counter = new IntMultiset();	
		
		for	(int i = data.size()-1; i > 0; i--)
			for (StepData futureData : data.get(i))
				for (StepData pastData : data.get(i-1))
					if (futureData.equals(pastData) && futureData.label == pastData.label){
						int d1 = (int) (futureData.request_ts - futureData.trans_ts);
						int d2 = (int) (pastData.request_ts - pastData.trans_ts);		
						counter.put((d1 - d2)/10*10);
					}
						
							
					
		for (Integer c : counter.getKeysArray()) {
			System.out.println(c + " " + counter.getAmountOf(c));
		}	
		System.out.println("=====");
	}
}
