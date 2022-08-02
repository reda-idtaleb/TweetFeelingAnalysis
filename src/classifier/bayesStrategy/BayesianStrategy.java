package classifier.bayesStrategy;

import java.util.List;
import java.util.Set;

public interface BayesianStrategy {
	public Set<String> buildVocabulary(List<String> words);
}
