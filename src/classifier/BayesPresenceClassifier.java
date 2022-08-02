package classifier;

/**
 * In this version of the bayes algorithm, we do not consider the occurrence of a word in the tweet.
 */
public class BayesPresenceClassifier extends AbstractBayesClassifier{

	@Override
	protected double calculateProbabilityOfPresence(double proba, double occurrence) {
		return Math.pow(proba, 1);	
	}

	@Override
	public String toString() {
		return super.toString()+" presence(" +getBayesStrategy().toString()+")"; 
	}
	
	
	
	
	
}
