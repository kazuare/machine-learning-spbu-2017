import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LabelFilter {
	public static List<Character> getAnswers(String unprocessed, String processed){
		List<Character> answers = new ArrayList<>();
		List<Character> unprocessedLabels = CSVReader.getLabels(unprocessed);
		List<Character> processedLabels = CSVReader.getLabels(processed);
		
		if (processedLabels.size() != unprocessedLabels.size())
			throw new IllegalStateException();
		
		for (int i = 0; i < unprocessedLabels.size(); i++) {
			if (unprocessedLabels.get(i) == '?') {
				if (processedLabels.get(i) == '?' || processedLabels.get(i) == '-') {
					throw new IllegalStateException();					
				}
				answers.add(processedLabels.get(i));
			} else if (unprocessedLabels.get(i) != '-') {
				if (unprocessedLabels.get(i) != processedLabels.get(i))
					System.out.println("label mismatch with original data: i: " + i + " p: " + processedLabels.get(i) + " up: " + unprocessedLabels.get(i));
			}
		}
		return answers;
	}
}
