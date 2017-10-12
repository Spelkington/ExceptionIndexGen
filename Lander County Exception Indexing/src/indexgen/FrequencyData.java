package indexgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * FrequencyData stores a collection of the top x frequent keywords in the English language.
 * 
 * @author Spencer Elkington
 */
public class FrequencyData{

	private final File DATA_FILE = new File("resources/frequency_data/EnglishFrequencyList.txt");
	private List<Keyword> keywordList = new LinkedList<Keyword>();
	
	public FrequencyData(int loadTo) throws FileNotFoundException, IOException {
		
		Scanner dataScan = new Scanner(DATA_FILE);
		StringBuilder stemInput = new StringBuilder();
		
		while (dataScan.hasNextLine() && keywordList.size() <= loadTo) {
			
			//Append the word from the frequency list.
			stemInput.append(dataScan.nextLine().split("\t")[1]);
			stemInput.append("\n");
			
		}
		
		dataScan.close();
		keywordList = Core.guessFromString(stemInput.toString());
		
		return;
	}
	
	public List<Keyword> getList() {
		return keywordList;
	}
	
}
