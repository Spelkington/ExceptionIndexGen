package indexgen;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;


/**
 * The Core object is an object used to process the frequency of words with a common root within a document. This will be used to determine "key words"
 * within a document.
 * 
 * The structure of this object was retrieved from https://stackoverflow.com/questions/17447045/
 * and was written primarily by user sp00m. Changes were made by me.
 * 
 * @author StackOverflow User sp00m
 * @author Spencer Elkington
 */
public class Core {

	/**
	 * Analyzes a text input and returns a list of keywords, sorted by decreasing frequency, that occur within that input.
	 * For example, the input
	 * 
	 * <pre>
	 * "meet meets meeting"
	 * </pre>
	 * 
	 * might return a list containing
	 * 
	 * <pre>
	 * "meet"
	 * 		> "meet"
	 * 		> "meets"
	 * 		> "meeting"
	 * </pre>
	 * 
	 * @param Input string to be analyzed
	 * @return A list of Keyword objects containing a stem and all the terms that fell into that string.
	 * @throws IOException if the input string has issues tokenizing.
	 */
	public static List<Keyword> guessFromString(String input) throws IOException {
		
		TokenStream tokenStream = null;
		
		try {
			
			// Replace all dashes in order to keep dashed words whole.
			input = input.replaceAll("-+", "-0");
			
			// Replace any punctuation characters except apostrophes and dashes with whitespace.
			input = input.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
			
			// Remove contractions.
			input = input.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");
			
			// Tokenize input.
			tokenStream = new ClassicTokenizer();
			((Tokenizer) tokenStream).setReader(new StringReader(input));
			
			// Make tokens lowercase
			tokenStream = new LowerCaseFilter(tokenStream);
			
			// Remove dots from acronyms.
			tokenStream = new ClassicFilter(tokenStream);
			
			// Convert all characters to ASCII
			tokenStream = new ASCIIFoldingFilter(tokenStream);
			
			// Remove English stop words.
			tokenStream = new StopFilter(tokenStream, EnglishAnalyzer.getDefaultStopSet());
			
			List<Keyword> keywords = new LinkedList<Keyword>();
			CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
			tokenStream.reset();
			
			while (tokenStream.incrementToken()) {
				
				String term = token.toString();
				
				// Stem the term.
				String stem = Utility.stem(term);
				boolean isNumber = true;
				
				try {
					Integer.parseInt(stem);
				} catch (NumberFormatException e) {
					isNumber = false;
				}
				
				if (stem != null && !isNumber) {
					// Create the keyword, or get the existing one.
					Keyword keyword = Utility.find(keywords, new Keyword(stem.replaceAll("-0", "-")));
					// add the initial token to the stem category
					keyword.add(term.replaceAll("-0", "-"));
				}
				
			}
			
			Collections.sort(keywords);
			
			return keywords;
			
		} finally {
			
			if (tokenStream != null) {
				
				tokenStream.close();
				
			}
			
		}
		
	}
	
}
