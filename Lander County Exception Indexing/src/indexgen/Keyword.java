package indexgen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An object containing a list of terms, the common stem of those terms,
 * and the frequency of the terms' use within a sample document.
 * 
 * For example, a Keyword object generated from the input
 * 
 * <pre>
 * "Meet meets meeting"
 * </pre>
 * 
 * would store
 * 
 * <pre>
 * Stem: "meet"
 * Terms: "meet", "meets", "meeting"
 * Frequency: 3
 * </pre>
 * 
 * The structure of this object was retrieved from https://stackoverflow.com/questions/17447045/
 * and was written primarily by user sp00m. Changes were made by me.
 * 
 * @author StackOverflow User sp00m
 * @author Spencer Elkington
 */
public class Keyword implements Comparable<Keyword> {

	private String stem = "";
	private Set<String> terms = new HashSet<String>();
	private int frequency = 0;
	
	public Keyword(String stem) {
		
		this.stem = stem;
		
	}
	
	public void add(String term) {
		
		terms.add(term);
		frequency++;
		
	}
	
	@Override
	public int compareTo(Keyword c) {
		
		// Sort by descending order
		return Integer.valueOf(c.frequency).compareTo(frequency);
		
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) { // If object is being compared to itself,
			
			return true;
			
		} else if (!(obj instanceof Keyword)) { // If object is not a Keyword, cannot be the same
			
			return false;
			
		} else { // Otherwise,
			
			return stem.equals(((Keyword) obj).stem); // See if the two objects are equal to one another.
			
		}
		
	}
	
	@Override
	public int hashCode() {
		
		return Arrays.hashCode(new Object[] { stem }); // return the hashcode of object-form of stem.
		
	}
	
	public String getStem() {
		
		return stem;
		
	}
	
	public Set<String> getTerms() {
		
		return terms;
		
	}
	
	public int getFrequency() {
		
		return frequency;
		
	}
	
	public String label() {
		
		StringBuilder result = new StringBuilder();
		result.append(this.getStem());
		result.append(":");
		
		for (String term : this.terms) {
			
			result.append("\n\t");
			result.append(term);
			
		}
		
		return result.toString();
	}
	
}
