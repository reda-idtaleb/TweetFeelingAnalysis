package classifier;

import model.searchTweets.*;

public abstract class Classifier {
	
	/**
	 * Classifies a tweet.
	 * 
	 * @param tweetContent message to analyse
	 * @return the feeling of the tweet
	 */
	public abstract Polarity classifies (MyTweet tweet);
	
	public boolean isMLClassifier() {
		return false;
	}
	
	public abstract String toString();
	
}
