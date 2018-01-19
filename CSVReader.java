import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVReader {
	public static List<Character> getLabels(String filename){
		List<Character> labels = new ArrayList<>();
		List<List<StepData>> data = getData(filename);		
		for (List<StepData> step : data)
			for (StepData stepData : step)
				labels.add(stepData.label);
		return labels;
	}
	
	public static List<List<StepData>> getData(String filename){
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String sCurrentLine;
						
			List<StepData> aggregatorList = new ArrayList<>();
			
			br.readLine();
			int index;
			while ((sCurrentLine = br.readLine()) != null) {
				index = sCurrentLine.indexOf(',');
				double longitude = Double.parseDouble(sCurrentLine.substring(0, index));
				sCurrentLine = sCurrentLine.substring(index + 1);

				index = sCurrentLine.indexOf(',');
				double latitude = Double.parseDouble(sCurrentLine.substring(0, index));	
				sCurrentLine = sCurrentLine.substring(index + 1);			

				index = sCurrentLine.indexOf(',');
				int request_ts = Integer.parseInt(sCurrentLine.substring(0, index));	
				sCurrentLine = sCurrentLine.substring(index + 1);		

				index = sCurrentLine.indexOf(',');
				int trans_ts = Integer.parseInt(sCurrentLine.substring(0, index));	
				sCurrentLine = sCurrentLine.substring(index + 1);
				
				char label = sCurrentLine.charAt(0);
				
				StepData sd = new StepData(longitude, latitude, request_ts, trans_ts, label);
				
				aggregatorList.add(sd);
			}
			
			List<List<StepData>> data = new ArrayList<>();
			
			long currentStepTime = aggregatorList.get(0).request_ts;
			data.add(new ArrayList<>());
			for (int i = 0; i < aggregatorList.size(); i++) {
				if (aggregatorList.get(i).request_ts != currentStepTime) {
					data.add(new ArrayList<>());	
					currentStepTime = aggregatorList.get(i).request_ts;
				}
				data.get(data.size()-1).add(aggregatorList.get(i));
			}
						
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
