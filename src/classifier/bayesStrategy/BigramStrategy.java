package classifier.bayesStrategy;

import java.util.*;

public class BigramStrategy implements BayesianStrategy {

	@Override
	public Set<String> buildVocabulary(List<String> words) {
		int n = 2; // combinaison de deux mots consécutifs
		List<String> bigram = new ArrayList<String>();
		String[] composedWord = new String[n];
		for(int i = 0; i <= words.size()-2; i++) {
			composedWord[0] = words.get(i);
			composedWord[1] = words.get(i + 1);
			bigram.add(String.join(" ", composedWord[0], composedWord[1]));
			composedWord = new String[n];
		}
		return new HashSet<String>(bigram);		
	}	
	
	@Override
	public String toString() {
		return "2-gram" ;
	}
}
