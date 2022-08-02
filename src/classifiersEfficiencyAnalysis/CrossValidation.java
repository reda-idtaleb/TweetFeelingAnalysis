package classifiersEfficiencyAnalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import classifier.AbstractBayesClassifier;
import classifier.BayesFrequenceClassifier;
import classifier.BayesPresenceClassifier;
import classifier.Classifier;
import classifier.KNNClassifier;
import classifier.MLClassifiers;
import classifier.bayesStrategy.BigramStrategy;
import classifier.bayesStrategy.UnigramStrategy;
import model.TweetBase;
import model.searchTweets.*;

public class CrossValidation {
	private TweetBase tweetBase;
	private static CrossValidation instance;

	/**
	 * Classifier to cross validate.
	 */
	private MLClassifiers classifier;

	/**
	 * Number of folds used to do the cross validation.
	 */
	private int nbFolds;
	
	public CrossValidation (TweetBase tweetBase, MLClassifiers classifier, int nbFolds) {
		this.tweetBase = tweetBase;
		this.classifier = classifier;
		this.nbFolds = nbFolds;
	}

	// Generates the folds
	private TweetBase[] generatesFolds () throws IllegalArgumentException {
		Collection<MyTweet> tweets = this.tweetBase.getTweetBase();

		if (tweets.size() <= this.nbFolds) 
			throw new IllegalArgumentException("Not enough tweet in the learning base to fill all the folds." );
		else {
			TweetBase[] res = new TweetBase[this.nbFolds];
			List<MyTweet> positives = new ArrayList<MyTweet>();
			List<MyTweet> negatives = new ArrayList<MyTweet>();
			List<MyTweet> neutrals = new ArrayList<MyTweet>();
			// Initializes res
			for (int i = 0; i < res.length; i++) 
				res[i] = new TweetBase();			

			positives = tweets.parallelStream().filter(t -> 
				t.getPolarity() == Polarity.POSITIVE).collect(Collectors.toList());
			
			negatives = tweets.parallelStream().filter(t -> 
			t.getPolarity() == Polarity.NEGATIVE).collect(Collectors.toList());
			
			neutrals = tweets.parallelStream().filter(t -> 
			t.getPolarity() == Polarity.NEUTRAL).collect(Collectors.toList());
			
			// Fills the feelings lists
			/*for (MyTweet tweet : tweets) {
				Polarity p = tweet.getPolarity();
				if (p == Polarity.POSITIVE) 
					positives.add(tweet);
				else if (p == Polarity.NEUTRAL) 
					negatives.add(tweet);
				else if (p == Polarity.NEGATIVE) 
					neutrals.add(tweet);		
			}*/
			// Builds a List< List< Tweet> > with positives, negatives and neutrals tweets
			List<List<MyTweet>> lists = new ArrayList<List<MyTweet>>();
			lists.add(positives);
			lists.add(negatives);
			lists.add(neutrals);

			/**lists.parallelStream().forEach(listPolarity -> 
			{
				AtomicInteger i = new AtomicInteger(0);
				listPolarity.parallelStream().forEach( tweet ->
				{
					res[i.get() % nbFolds].add(tweet);
					i.incrementAndGet();
				});
				
			});**/
			// Fills the folds equitably
			for (List<MyTweet> list : lists) {
				int i = 0;
				for (MyTweet tweet : list) {
					res[i].add(tweet);
					i = (i + 1) % nbFolds ;
				}
			}
			return res;
		}
	}

	// Evaluates the classifier with the fold number nbFolds as learning base
	private double evaluatesWithFoldNb (int nbFold, TweetBase[] folds) throws IllegalArgumentException {
		if (nbFold >= folds.length) 
			throw new IllegalArgumentException("Unknown fold number!");
		else {
			int res = 0;
			TweetBase learningBase = new TweetBase();
			TweetBase toClassify = folds[nbFold];

			// Fills the learning base
			
			for (int i = 0; i < folds.length; i++) {
				if (i != nbFold) 
					folds[i].getTweetBase().stream().forEach(t -> {
						learningBase.add(t);
					});
			}
				
			/*for (int i = 0; i < folds.length; i++) 
				if (i != nbFold) 
					
					for (MyTweet tweet : folds[i].getTweetBase()) 
						learningBase.add(tweet);
			*/
			// Set the classifier learning base
			this.classifier.setTweetBase(learningBase);

			// Calculates the error rate
			for (MyTweet tweet : toClassify.getTweetBase()){
				Polarity p = this.classifier.classifies(tweet);
				if (p != tweet.getPolarity()) 
					res++;
			}
			return ((double)res / (double)toClassify.getTweetBase().size()) * 100;
		}
	}

	public double evaluates () {
		double res = 0;
		TweetBase[] folds = this.generatesFolds();

		for (int fold = 0; fold < this.nbFolds; fold++)
			res += this.evaluatesWithFoldNb(fold, folds);

		// Put the intiale tweet base in the classifier
		this.classifier.setTweetBase(this.tweetBase);
		return res / this.nbFolds;
	}
	
}
