package src.innovationx.classic.content;
import java.util.ArrayList;

/**
 * Racism Filter
 * @author Harry Andreas
 */

public class RacismFilter {
	
	/**
	 * Contains all the racist terms.
	 */
	public ArrayList<String> racistTerms = new ArrayList<String>();
	public String[] terms = {"nigga", "fag", "n i g g a", "n166a", "gay", "quire"};
	
	public RacismFilter() {
		for(String term: terms) {
			racistTerms.add(term);
		}
	}
	

}
