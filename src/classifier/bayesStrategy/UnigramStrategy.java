package classifier.bayesStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnigramStrategy implements BayesianStrategy {

	@Override
	public Set<String> buildVocabulary(List<String> words) {	
		return new HashSet<String>(words);
	}
	
	@Override
	public String toString() {
		return "1-gram" ;
	}

}
