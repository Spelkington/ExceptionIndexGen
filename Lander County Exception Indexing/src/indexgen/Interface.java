package indexgen;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Interface {

	public static void main (String[] args) {
		
		List<Keyword> freqData = null;
		List<Keyword> stemData = null;
		
		try {
			freqData = new FrequencyData(200).getList();
			stemData = Core.guessFromString(Utility.fileToString(new File("resources/input_data/20170914.txt")));
		} catch (Exception e) {
			System.out.print(e);
		}
		
		stemData = Utility.filterKeywordList(stemData, freqData);
		
		List<String> terms = Utility.expandTerms(stemData);
		
		try {
			Utility.writeListToFile("C://Users/spelk/Desktop/OutputSample.txt", terms);
		} catch (IOException e) {
			System.out.println(e);
		}
			
		return;
		
	}
	
}
