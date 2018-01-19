import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String ...strings){
		List<List<StepData>> data = CSVReader.getData("C:\\Users\\test\\Desktop\\dobr\\transport_data.csv");

		data = markWithSpawnZones(data);
		data = markEqualWithFutureLabels(data);	
		
		data = markWithNNInductionWithExclusionOfDeterminedInBaseAndDeterminedFlagPlacing(data, '0');		
		CSVWriter.writeProcessedData(data, 0);	
		
		//draw();
		
		List<Character> answers = LabelFilter.getAnswers("C:\\Users\\test\\Desktop\\dobr\\transport_data.csv", "C:/Users/test/Desktop/dobr/transport_data_processed.csv");
		 
		CSVWriter.writeAnswers(answers);

		StatisticsGatherer.calculateClusterPower("C:\\Users\\test\\Desktop\\dobr\\transport_data_processed.csv");
		
	}
	
	static void draw() {
		try {
			Process process = Runtime.getRuntime().exec("python C:/Users/test/Desktop/dobr/draw.py");
			BufferedReader stdError = new BufferedReader(new 
	                 InputStreamReader(process.getErrorStream()));
			String r = null;
			while ((r = stdError.readLine()) != null) {
				System.out.println(r);
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static boolean isInRectangle(StepData stepData, double x1, double y1, double x2, double y2) {
		return 	x1 <= stepData.longitude &&
				y1 <= stepData.latitude &&
				x2 >= stepData.longitude &&
				y2 >= stepData.latitude;				
	}
	
	static List<List<StepData>> clearLabels(List<List<StepData>> data) {
		for	(List<StepData> step : data)
			for (StepData stepData : step) 
				stepData.label = '?';
		return data;
	}
	
	static List<List<StepData>> markEqualWithFutureLabels(List<List<StepData>> data) {
		int numberOfInvokations = 0;
		for	(int i = data.size()-1; i > 0; i--)
			for (StepData futureData : data.get(i))
				for (StepData pastData : data.get(i-1))
					if (futureData.equals(pastData) && futureData.label != '-' && futureData.label != '?')
						if (pastData.label == '-' || pastData.label == '?'){
							pastData.label = futureData.label;
							pastData.isAlreadyDetermined = true;
							numberOfInvokations++;
						}	
		System.out.println("markEqualWithFutureLabels was invoked " + numberOfInvokations + " times");			
		return data;
	}
	
	static List<List<StepData>> markWithSpawnZones(List<List<StepData>> data) {
		for	(List<StepData> step : data)
			for (StepData stepData : step) { 
				if (isInRectangle(stepData, 30.29, 59.92, 30.3154, 59.9365))
					stepData.label = '0';
				else if (isInRectangle(stepData, 30.20, 59.92, 30.2877, 60.0) ||
					isInRectangle(stepData, 30, 59.9375, 30.3062, 59.9425))
					stepData.label = '1';
				else if (isInRectangle(stepData, 30.3927, 59.92, 30.42, 60.0) || 
					isInRectangle(stepData, 30.285, 59.9424, 30.31, 60.0)) {
					stepData.label = '2';
				}
			}
		return data;
	}
	
	static List<List<StepData>> markWithNNInduction(List<List<StepData>> data, char defaultLabel) {
		for (StepData stepData : data.get(0))  
			if (stepData.label == '?' || stepData.label == '-')
				stepData.label = defaultLabel;
				
		for	(int i = 1; i < data.size(); i++)
			for (StepData stepData : data.get(i))  
				if (stepData.label == '-' || stepData.label == '?')
					stepData.label = findNN(stepData, data.get(i-1)).label;
			
		return data;
	}

	
	static List<List<StepData>> markWithNNInductionWithExclusionOfDeterminedInBaseAndDeterminedFlagPlacing(List<List<StepData>> data, char defaultLabel) {
		for (StepData stepData : data.get(0))  
			if (stepData.label == '?' || stepData.label == '-')
				stepData.label = defaultLabel;
		
		for	(int i = 1; i < data.size(); i++) {
			for (StepData futureData : data.get(i))  
				for (StepData pastData : data.get(i-1)) 
					if (pastData.equals(futureData)) {
						pastData.isAlreadyDetermined = true;
						futureData.label = pastData.label;
					}
			
			List<StepData> searchDomain = findUnderterminedPoints(data.get(i-1));
			for (StepData stepData : data.get(i))  
				if (stepData.label == '-' || stepData.label == '?') 
					try{
						stepData.label = findNN(stepData, searchDomain).label;
					}catch(Exception e){
						stepData.label = defaultLabel;
						System.out.println("No candidates left at index: " + i);
					}
		}	
		return data;
	}
	
	static List<List<StepData>> markWithNNInductionWithExclusionOfDeterminedInBase(List<List<StepData>> data, char defaultLabel) {
		for (StepData stepData : data.get(0))  
			if (stepData.label == '?' || stepData.label == '-')
				stepData.label = defaultLabel;
				
		int normalNNDiff = 0;
		
		for	(int i = 1; i < data.size(); i++) {
			List<StepData> searchDomain = findUnderterminedPoints(data.get(i-1));
			for (StepData stepData : data.get(i))  
				if (stepData.label == '-' || stepData.label == '?') {
					stepData.label = findNN(stepData, searchDomain).label;
					if (!findNN(stepData, searchDomain).equals(findNN(stepData, data.get(i-1))))
						normalNNDiff++;
				}
		}	
		System.out.println("difference with normal nn: " + normalNNDiff);
		return data;
	}

	static List<StepData> findUnderterminedPoints(List<StepData> searchDomain){
		List<StepData> past = new ArrayList<>();
		for (StepData stepData : searchDomain) 
			if (!stepData.isAlreadyDetermined)
				past.add(stepData);
		
		return past;
	}
	
	static List<List<StepData>> markWithNNFPInduction(List<List<StepData>> data, char defaultLabel) {
		for (StepData stepData : data.get(0))  
			if (stepData.label == '?' || stepData.label == '-')
				stepData.label = defaultLabel;
				
		for	(int i = 1; i < data.size(); i++) 
			for (StepData stepData : data.get(i)) {
				List<StepData> searchDomain = findPointsFromPast(stepData, data.get(i-1));
				if (searchDomain.size() == 0)
					throw new IllegalStateException();
				if (stepData.label == '-' || stepData.label == '?')
					stepData.label = findNN(stepData, searchDomain).label;
			}
		
		return data;
	}
	
	static List<StepData> findPointsFromPast(StepData target, List<StepData> searchDomain){
		List<StepData> past = new ArrayList<>();
		for (StepData stepData : searchDomain) 
			if (target.trans_ts >= stepData.trans_ts) 
				past.add(stepData);
		
		return past;
	}
	
	static StepData findNN(StepData target, List<StepData> searchDomain){
		StepData NN = searchDomain.get(0);
		for (StepData stepData : searchDomain) 
			if (distance(stepData, target) < distance(NN, target)) 
				NN = stepData;
		
		return NN;
	}
	
	static double distance(StepData a, StepData b) {
		double dlat = a.latitude - b.latitude;
		double dlon = a.longitude - b.longitude;
		return Math.sqrt(dlat*dlat + dlon*dlon);
	}
}
