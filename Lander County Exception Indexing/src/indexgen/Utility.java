package indexgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * The Utility class is a collection of helper methods to process the frequency of root words within a document.
 * 
 * The structure of this object was retrieved from https://stackoverflow.com/questions/17447045/
 * and was written primarily by user sp00m. Changes were made by me.
 * 
 * @author StackOverflow User sp00m
 * @author Spencer Elkington
 */
public class Utility {

	/**
	 * Processes a word into the stem of that word.
	 * For example, input
	 * 
	 * <pre>
	 * "meeting"
	 * </pre>
	 * 
	 * would be stemmed into the base word,
	 * 
	 * <pre>
	 * "meet"
	 * </pre>
	 * 
	 * @param term to be stemmed
	 * @return stemmed term.
	 * @throws IOException if the term cannot be tokenized.
	 */
	public static String stem(String term) throws IOException {
		
		TokenStream tokenStream = null;
		
		try {
			
			// Convert input into tokens
			tokenStream = new ClassicTokenizer();
			((Tokenizer) tokenStream).setReader(new StringReader(term));
			
			// Get the stems
			tokenStream = new PorterStemFilter(tokenStream);
			
			// Remove duplicate stems by adding to a set.
			Set<String> stems = new HashSet<String>();
			CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
			tokenStream.reset();
			
			while (tokenStream.incrementToken()) {
				
				stems.add(token.toString());
				
			}
			
			// Return a null value if no/too many stems were found
			if (stems.size() != 1) {
				return null;
			}
			String stem = stems.iterator().next();
			
			// If the stem has characters that aren't alpha-numerical,
			if (!stem.matches("[a-zA-Z0-9-]+")) {
				return null;
			}
			
			return stem;
			
		} finally {
			
			if (tokenStream != null) {
				tokenStream.close();
			}
			
		}
		
	}
	
	/**
	 * Determines if an object already exists within a Collection containing the same class.
	 * If the object is not detected, it is added to the collection.
	 * If it is detected, nothing happens.
	 * 
	 * Will return the example object.
	 * 
	 * @param Collection of like objects.
	 * @param Object to be searched for.
	 * @return The object that was searched for.
	 */
	public static <T> T find(Collection<T> collection, T example) {
		
		for (T element : collection) { // For each element in the collection,
			
			if (element.equals(example)) { // If the collection element equals the example element
				return element; // Return the element -- they're both the same.
			}
			
		}
		
		// If the for loop finished, the example must not be in the collection
		
		collection.add(example); // Add it to the collection,
		return example; // And return the example.
		
	}
	
	/**
	 * Converts the scannable contents of a file into a string.
	 * 
	 * @param Input file
	 * @return String containing the scannable contents of the string.
	 */
	public static String fileToString(File input) {
		
		StringBuilder result = new StringBuilder();
		Scanner fileScan = null;
				
		try {
			fileScan = new Scanner(input);
		} catch (FileNotFoundException e) {
			//TODO: Make this do something reasonable(?)
		}
		
		while (fileScan.hasNextLine()) {
			
			result.append(fileScan.nextLine() + "\n");
			
		}
		
		fileScan.close();
		return result.toString();
		
	}
	
	public static List<Keyword> filterKeywordList(List<Keyword> masterList, List<Keyword> filterList) {
		
		List<Keyword> result = new LinkedList<>();
		
		for (Keyword masterKey : masterList) {
			
			for (Keyword filterKey : filterList) {
				
				if (masterKey.getStem().equals(filterKey.getStem())) {
					
					result.add(masterKey);
					break;
					
				}
				
			}
			
		}
		
		return result;
		
	}
	
	public static List<String> expandTerms(List<Keyword> keyList) {
		
		List<String> result = new LinkedList<String>();
		
		for (Keyword key : keyList) {
			
			for (String term : key.getTerms()) {
				
				result.add(term);
				
			}
			
		}
		
		return result;
		
	}
	
	public static File writeListToFile(String directory, List<String> list) throws IOException {
		
		StringBuilder insert = new StringBuilder();
		
		for (String term : list) {
			
			insert.append(term);
			insert.append(System.lineSeparator());
			
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(directory));
		writer.write(insert.toString());
		writer.close();
		
		return new File(directory);
		
	}
	
}
