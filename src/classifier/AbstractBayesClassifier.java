package classifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import classifier.bayesStrategy.BayesianStrategy;
import classifier.bayesStrategy.CompositeStrategy;
import classifier.bayesStrategy.UnigramStrategy;
import model.searchTweets.*;

import java.util.Map.Entry;

/**
 * This an object representing the bayesian classification based on the Bayes algorithm
 */
public abstract class AbstractBayesClassifier extends MLClassifiers {

	// Unpolarized is not considered as a class of polarity
	private Polarity[] polarities;  
	
	/**
	 * Bayesian strategy corresponds to if we consider the combination of the words of the vocabulary or not.
	 */
	private BayesianStrategy bayesStrategy;
	private CompositeStrategy c = new CompositeStrategy();
	/**
	 * Length of the words to accept when the classification with simplified method.
	 */
	private final int WORD_LENGTH_MIN = 3;	
	
	public AbstractBayesClassifier() {
		this(new UnigramStrategy());
	}
	
	public AbstractBayesClassifier(BayesianStrategy bayesStrategy) {
		super();
		this.polarities = Arrays.copyOfRange(Polarity.values(), 1, Polarity.values().length);
		this.bayesStrategy = bayesStrategy;
	}

	/**
	 * calculates P(c=Feeling)
	 * @param polarity a polarity 
	 * @return the probability of this polarity
	 */
	private double getProbabilityOfPolarity(Polarity polarity) {
		List<MyTweet> base = this.tweetBase.getTweetBase();
		double cmp = 0;
		double totalTweets = base.size();
		for(MyTweet t : base)
			if(t.getPolarity()==polarity) 
				cmp++;
		return cmp/totalTweets;
	}
	
	private Polarity bayesAlgorithm(MyTweet tweet) {
		double proba = 1;
		HashMap<Polarity, Double> eachClassProba = new HashMap<Polarity, Double>();
		Set<String> tweetVocabulary = this.bayesStrategy.buildVocabulary(this.removeSmallWords(tweet));
		double occurenceWord = 0;
		for(Polarity p : this.polarities) {
			for(String w : tweetVocabulary) {
				// On calcule P(Ai|Ck) qui correspond à P(word|polarity)
				occurenceWord = this.countWordOccurence(tweet.getTweet(), w);
				proba *= this.calculateProbabilityOfPresence(this.calculateProbability(w.toLowerCase(), p), occurenceWord);		
			}
			eachClassProba.put(p, proba*this.getProbabilityOfPolarity(p));
			proba = 1;
		}
		return this.maximize(eachClassProba);
	}

	public List<String> removeSmallWords(MyTweet tweet) {
		List<String> words = Arrays.asList(tweet.getTweet().toLowerCase().split(" "));
		words = words.stream()
					 .filter(w -> w.length() >= this.WORD_LENGTH_MIN)
					 .collect(Collectors.toList());
		return words;
	}
	
	/**
	 * Calculate the probability of a word w appearing in tweets with a given polarity p.
	 * @param w a word of tweet
	 * @param p a polarity
	 * @return the probability of a word appearing in tweets with polarity p. P(Wj|Pk) = N(j,k) / Nk
	 */
	private double calculateProbability(String w, Polarity p) {
		// nombre total de mots des textes de la classe Pk. Correspond à Nk de la formule du calcul
		double totalPWords = 0;
		double wordOccurences = 0;
		for (MyTweet t : this.tweetBase.getTweetBase())
			if(t.getPolarity() == p) { 
				totalPWords += t.getTweet().split(" ").length;
				wordOccurences += this.countWordOccurence(t.getTweet(), w);
			}
		return (wordOccurences + 1) / (totalPWords + this.computeAllWordsOfTweetBase());
	}
	
	private Polarity maximize(HashMap<Polarity, Double> eachClassProba) {
		double max = 0;
		Polarity correspondingClass = Polarity.UNPOLARIZED;
		double sum = eachClassProba.values().stream().mapToDouble(Double::doubleValue).sum();
		for (Entry<Polarity, Double> entry : eachClassProba.entrySet()) {
			double classProba = entry.getValue()/sum;
			if(max < classProba) {
				max = classProba;
				correspondingClass = entry.getKey();
			}		       
	    }
		return correspondingClass;
	}
	
	private int computeAllWordsOfTweetBase() {
		int cpt = 0;
		for (MyTweet t : this.tweetBase.getTweetBase())
			cpt += t.getTweet().split(" ").length;
		return cpt;
	}
	
	/**
	 * count the occurrence of a word in a text
	 * @param text a text
	 * @param word a word
	 * @return the number of occurrence of a word in a text
	 */
	private double countWordOccurence(String text, String word) {
		List<String> splitText = Arrays.asList(text.split(" "));
        	double cpt = 0;
		for(String s : splitText)
	        if (s.equals(word))
	        	cpt++;
		return cpt;
	}
	
	protected abstract double calculateProbabilityOfPresence(double proba, double occurrence);
	
	@Override
	public Polarity classifies(MyTweet tweet) {
		return this.bayesAlgorithm(tweet);
	}

	/**
	 * @return the wORD_LENGTH_MIN
	 */
	public int getWORD_LENGTH_MIN() {
		return WORD_LENGTH_MIN;
	}

	public BayesianStrategy getBayesStrategy() {
		return bayesStrategy;
	}

	public void setBayesStrategy(BayesianStrategy bayesStrategy) {
		this.bayesStrategy = bayesStrategy;
	}
	
	
	public void compositeBayesStrategy(BayesianStrategy bayesStrategy) {
		c.add(bayesStrategy);
		this.bayesStrategy = c;
	}

	@Override
	public String toString() {
		return "Bayes";
	}
	
	

}
