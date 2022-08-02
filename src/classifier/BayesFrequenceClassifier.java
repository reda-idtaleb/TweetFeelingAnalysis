package classifier;

/**
 * In this version of the bayes algorithm, we consider the occurrence of a word in the tweet.
 */
public class BayesFrequenceClassifier extends AbstractBayesClassifier{
	
	@Override
	protected double calculateProbabilityOfPresence(double proba, double occurrence) {
		return Math.pow(proba, occurrence);	
	}
	
	@Override
	public String toString() {
		return super.toString()+" frequence(" +getBayesStrategy().toString()+")";
	}
}
