package model.searchTweets;

import java.util.*;

import classifier.AbstractBayesClassifier;
import classifier.BayesFrequenceClassifier;
import classifier.BayesPresenceClassifier;
import classifier.Classifier;
import classifier.KNNClassifier;
import classifier.MLClassifiers;
import classifier.bayesStrategy.*;
import classifiersEfficiencyAnalysis.CrossValidation;
import model.TweetBase;
import tools.utils.TweetCleaner;
import twitter4j.*;

public class AppModel {
	private List<MyTweet> cleanedTweets;
	private List<MyTweet> originalTweets;
	private List<MyTweet> classifiedTweets;
	private int numberOfTweets;
	private Classifier classifier; 
	private TweetBase tweetBase;
	private final int CROSS_VALIDATION_NFOLD = 5;
	private List<MLClassifiers> classifiers;
	public final static String DATA_BASE_FILE = "DataBase.csv";
	
	public AppModel() {
		this(new KNNClassifier());
	}
	
	public AppModel(Classifier classifier) {		
		this.cleanedTweets = new ArrayList<>();
		this.classifiedTweets = new ArrayList<MyTweet>();
		this.originalTweets = new ArrayList<>();
		this.numberOfTweets = 20;	
		this.tweetBase = new TweetBase(DATA_BASE_FILE);	
		this.classifier = classifier;
		//remplissage des de classifiers
		this.classifiers = new ArrayList<MLClassifiers>();
		this.buildClassifiers();
	}
	
	private void buildClassifiers() {	
		KNNClassifier knn = new KNNClassifier();
		knn.setTweetBase(tweetBase);
		classifiers.add(knn);
		
		AbstractBayesClassifier pUni = new BayesPresenceClassifier();;
		pUni.setTweetBase(tweetBase);
		classifiers.add(pUni);
		
		AbstractBayesClassifier pBi = new BayesPresenceClassifier();
		pBi.setBayesStrategy(new BigramStrategy());
		pBi.setTweetBase(tweetBase);
		classifiers.add(pBi);
		
		AbstractBayesClassifier bayesPUnibi = new BayesPresenceClassifier();
		bayesPUnibi.compositeBayesStrategy(new UnigramStrategy());
		bayesPUnibi.compositeBayesStrategy(new BigramStrategy());
		bayesPUnibi.setTweetBase(tweetBase);
		classifiers.add(bayesPUnibi);
		
		AbstractBayesClassifier fUni = new BayesFrequenceClassifier();;
		fUni.setTweetBase(tweetBase);
		classifiers.add(fUni);
		
		AbstractBayesClassifier fBi = new BayesFrequenceClassifier();
		fBi.setBayesStrategy(new BigramStrategy());
		fBi.setTweetBase(tweetBase);
		classifiers.add(fBi);
		
		AbstractBayesClassifier bayesFUnibi = new BayesFrequenceClassifier();
		bayesFUnibi.compositeBayesStrategy(new UnigramStrategy());
		bayesFUnibi.compositeBayesStrategy(new BigramStrategy());
		bayesFUnibi.setTweetBase(tweetBase);
		classifiers.add(bayesFUnibi);
	}
	
	public void updateClassifiers() {
		classifiers.forEach(c -> {
			c.setTweetBase(tweetBase); 
			});
	}
	
	public void updateClassifiers(List<Double> listEval) {
		int i = 0;
		for(MLClassifiers c : classifiers) {
			c.setEvaluation(listEval.get(i));
			i++;
		};
	}
	
	public List<MLClassifiers> getClassifiers() {
		return classifiers;
	}
	
	public int getCROSS_VALIDATION_NFOLD() {
		return CROSS_VALIDATION_NFOLD;
	}

	
	public List<MyTweet> searchTweets(String queryName) throws Exception{
		Twitter twitter = TwitterFactory.getSingleton();
		Query query = new Query(queryName);
		query.setLang("fr");
		query.setCount(this.numberOfTweets);
		QueryResult qResult = twitter.search(query);
		while (qResult.hasNext() && (originalTweets.size() < this.numberOfTweets)) {
			List< Status > list = qResult.getTweets();
			int i = 0;
			while ((i < list.size()) && (originalTweets.size() < this.numberOfTweets)) {
				Status t = list.get(i);
				if (!t.isRetweet()) 
					originalTweets.add(new MyTweet(Long.toString(t.getId()), t.getUser().getName(), t.getText(), String.valueOf(t.getCreatedAt()), queryName));			
				i++;
			}
			query = qResult.nextQuery();
			qResult = twitter.search(query);
		}
		return this.getCleanedTweets();
	}
	
	public List<MyTweet> getUncleanedTweets(){
		return this.originalTweets;
	}
	
	public List<MyTweet> classifies(List<MyTweet> tweets) {
		for(MyTweet t : tweets) {
			t.setPolarity(this.classifier.classifies(t));
			classifiedTweets.add(t);
		}
		return this.classifiedTweets;
	}
	
	public ArrayList<Integer> getStatistics(List<MyTweet> liste){
		ArrayList<Integer> stats = new ArrayList<Integer>();
		stats.add(this.getPositiveTweetsNumber(liste));
		stats.add(this.getNegativeTweetsNumber(liste));
		stats.add(this.getNeutralTweetsNumber(liste));
		stats.add(liste.size());
		return stats;
	}
	
	public void clear() {
		this.originalTweets.clear();
		this.cleanedTweets.clear();
	}
	
	// getters/setters
	/**
	 * @return the tweets
	 */
	public List<MyTweet> getCleanedTweets() {
		this.originalTweets.forEach(t -> {try {
			this.cleanedTweets.add(t.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}});
		return TweetCleaner.getCleanTweets(this.cleanedTweets);
	}

	/**
	 * @param tweets the tweets to set
	 */
	public void setTweets(List<MyTweet> tweets) {
		this.cleanedTweets = tweets;
	}


	/**
	 * @return the numberOfTweets
	 */
	public int getNumberOfTweets() {
		return numberOfTweets;
	}


	/**
	 * @param numberOfTweets the numberOfTweets to set
	 */
	public void setNumberOfTweets(int numberOfTweets) {
		this.numberOfTweets = numberOfTweets;
	}

	/**
	 * @return the classifier
	 */
	public Classifier getClassifier() {
		return classifier;
	}

	/**
	 * @param classifier the classifier to set
	 */
	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

	public TweetBase getTweetBase() {
		return tweetBase;
	}

	public void setTweetBase(TweetBase tweetBase) {
		this.tweetBase = tweetBase;
	}
	

	public int getNeutralTweetsNumber(List<MyTweet> classifiedTweets) {
		int n = 0;
		for(MyTweet t : classifiedTweets){
			if (t.getPolarity() == Polarity.NEUTRAL) {
				n ++;
			}
		}
		return n;	
	}
	
	public int getPositiveTweetsNumber(List<MyTweet> classifiedTweets) {
		int n = 0;
		for(MyTweet t : classifiedTweets){
			if (t.getPolarity() == Polarity.POSITIVE) {
				n ++;
			}
		}
		return n;	
	}
	
	public int getNegativeTweetsNumber(List<MyTweet> classifiedTweets) {
		int n = 0;
		for(MyTweet t : classifiedTweets){
			if (t.getPolarity() == Polarity.NEGATIVE) {
				n ++;
			}
		}
		return n;	

	}
	
	public Double evaluateClassifier() {
		return this.evaluateClassifier(this.classifier);
	}
	
	public Double evaluateClassifier(Classifier c) {
		if (c.isMLClassifier()) {
			CrossValidation crossValidation = new CrossValidation(tweetBase, (MLClassifiers)c, this.CROSS_VALIDATION_NFOLD);
			Double eval = crossValidation.evaluates();
			if(c.toString().contentEquals(this.classifier.toString()))
				((MLClassifiers)this.classifier).setEvaluation(eval);
			((MLClassifiers)c).setEvaluation(eval);
			return eval;
		}
		return -1.0;
	}
	
	public List<MyTweet> getClassifiedTweets() {
		return classifiedTweets;
	}

	public void setClassifiedTweets(List<MyTweet> classifiedTweets) {
		this.classifiedTweets = classifiedTweets;
	}
	
}
