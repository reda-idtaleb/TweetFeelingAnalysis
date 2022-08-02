package tools.utils;

import classifier.bayesStrategy.BigramStrategy;
import classifier.bayesStrategy.UnigramStrategy;

import java.util.List;

import classifier.*;
import model.TweetBase;
import model.searchTweets.AppModel;
public class ClassifierFactory {
	
	/**
	 * this method use reflection to find the right object to instantiate
	 * @param currentClassifier a name of a classifier
	 * @return an object of Classifier representing the given parameter 
	 */
	public static Classifier getClassifier(String currentClassifier, AppModel model) {
		Object classifier = getAppropriateClassifier(currentClassifier, model.getClassifiers());
		if(((Classifier)classifier).isMLClassifier())
			((MLClassifiers)classifier).setTweetBase(model.getTweetBase());
		return (Classifier) classifier;
    }	
	
	private static Object getAppropriateClassifier(String currentClassifier, List<MLClassifiers> list) {
		Object clf = null;
		for(MLClassifiers c : list)
			if(currentClassifier.contentEquals(c.toString()))
				clf = c;
		if(clf==null) 
			return new SimpleClassifier();
		return clf;
	}
}
