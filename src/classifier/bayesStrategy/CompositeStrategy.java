package classifier.bayesStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompositeStrategy extends HashSet<BayesianStrategy> implements BayesianStrategy {
	
	private static final long serialVersionUID = 1L;

	public CompositeStrategy() {
	}

	@Override
	public Set<String> buildVocabulary(List<String> words) {
		Set<String> vocab = new HashSet<String>();
		for(BayesianStrategy strategy : this)
			vocab.addAll(strategy.buildVocabulary(words));
		return vocab;
	}

	public Set<BayesianStrategy> getComposite() {
		return this;
	}
	
	@Override
	public String toString() {
		return "1&2-gram" ;
	}
}
