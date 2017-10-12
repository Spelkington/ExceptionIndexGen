package indexgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	
	/**
	 * Creates a FrequencyData object that stores the keywords of the most frequent word in the
	 * English language.
	 * 
	 * NOTE: The current data file only has the top 5000 entries.
	 * 
	 * @param loadTo, the # of entries that should be analyzed. Cannot be above 5000.
	 * @throws IllegalArgumentException if the number of requested words is greater than 5000.
	 * @throws FileNotFoundException if the frequency data file cannot be found.
	 * @throws IOException if the Core class cannot analyze the frequency file.
	 */
	public FrequencyData(int loadTo) throws IllegalArgumentException, FileNotFoundException, IOException {
		
		// If user requests too many words, throw exception.
		if (loadTo > 5000) {
			throw new IllegalArgumentException("FrequencyData cannot load more than 5000 words.");
		}
		
		Scanner dataScan = new Scanner(DATA_FILE);
		StringBuilder stemInput = new StringBuilder();
		
		int currentLine = 1;
		while (dataScan.hasNextLine() && currentLine <= loadTo) {
			
			String[] entry = dataScan.nextLine().split("\t");
			
			if (!entry[2].equals("n")) {
				// Append the word to the frequency list.
				stemInput.append(entry[1]);
				stemInput.append("\n");
			} 
			/*else {
				System.out.println(entry[1] + " was excluded: noun");
			}*/
			
			currentLine++;
			
		}
		
		dataScan.close();
		keywordList = Core.guessFromString(stemInput.toString());
		
		return;
	}
	
	public List<Keyword> getList() {
		return keywordList;
	}
	
}
