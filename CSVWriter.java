import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVWriter {
	public static void writeProcessedData(List<List<StepData>> data, int skip){
		try {
			PrintWriter writer = new PrintWriter("C:/Users/test/Desktop/dobr/transport_data_processed.csv", "UTF-8");
						
			boolean first = true;
			for (List<StepData> step: data) {
				if (first) {
					first = false;
					writer.print("longitude,latitude,request_ts,trans_ts,label");
				}
				for (StepData stepData: step) {
					if (skip-- > 0) 
						continue;
					writer.println();
					writer.print(stepData.longitude+",");
					writer.print(stepData.latitude+",");
					writer.print(stepData.request_ts+",");
					writer.print(stepData.trans_ts+",");
					writer.print(stepData.label);
				}
			}			
			
			writer.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeAnswers(List<Character> labels){
		if (labels.size() != 12000)
			throw new IllegalStateException();
		
		try {
			PrintWriter writer = new PrintWriter("C:/Users/test/Desktop/dobr/answers.txt", "UTF-8");
						
			for (Character label : labels){
				if (label == '-' || label == '?')
					throw new IllegalStateException();
				writer.println(label);
			}
			
			writer.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
