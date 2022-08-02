package classifier;

import java.util.*;

import model.searchTweets.*;
import tools.distance.GenericDistance;
import tools.distance.DistanceStrategy;

public class KNNClassifier extends MLClassifiers{
	private DistanceStrategy metric;
	private int neighbors;
	
	public KNNClassifier(DistanceStrategy distanceStrategy, int k) {
		super();
		this.metric = distanceStrategy;
		this.neighbors = k;
	}
	
	/**
	 * KNNClassifier constructor to specify the number of neighbors
	 * @param k the number of neighbors
	 */
	public KNNClassifier(int k) {
		this(GenericDistance.jaccardMetricFactory(), k);
	}
	
	/**
	 * Default KNN Classifier constructor using:
	 *  @tweetBase=TweetBase(DataBase.csv)
	 *  @distance=LevensteinMetric
	 * 	@k=5 nearest neighbors
	 */
	public KNNClassifier() {
		this(GenericDistance.jaccardMetricFactory(), 5);
	}
	
	/**
	 * Classify a tweet by using the K-NN algorithm
	 * @param tweetContent the content of the cleaned Tweet
	 * @param k by default k=5 (a cluster of 5 unKnown tweets) 
	 */
	private ArrayList<MyTweet> knnAlgorithm(MyTweet tweet) {
		ArrayList<MyTweet> nearestNeighbors = new ArrayList<MyTweet>();	
		ArrayList<Float> distances = new ArrayList<Float>();
		List<MyTweet> tweets = this.tweetBase.getTweetBase();
		// mettre les k premiers tweets dans nearestNeighbors
		for (int i = 0; i <= this.neighbors; i++) {
			MyTweet currentTweet = tweets.get(i);
			nearestNeighbors.add(tweet);
			distances.add(this.metric.distance(tweet.getTweet(), currentTweet.getTweet()));
		}
		// On parcourt le reste de tweetBase
		for (int i = this.neighbors; i < tweets.size(); i++) {		
			// chercher le max dans le tableau @distances
			float distanceMax = 0;
			int indiceMax = 0;
			// on parcourt @distances
			for (int j = 0; j < distances.size(); j++) {
				if (distances.get(j) > distanceMax) {
					distanceMax = distances.get(j);
					indiceMax = j;
				}
			}	
			// comparer cette distance avec la distance entre le tweet et le tweet de la table des tweets
			if (distanceMax > this.metric.distance(tweet.getTweet(), tweets.get(i).getTweet())) {
				// on fait un swap
				MyTweet newTweet = tweets.get(i);
				nearestNeighbors.set(indiceMax, newTweet);
				distances.set(indiceMax, this.metric.distance(tweet.getTweet(), newTweet.getTweet()));
			}
		}
		return nearestNeighbors;
	}
	
	private Polarity deduceMajorityClass(ArrayList<MyTweet> neighbors) {
		int cptPositive = 0, cptNegative = 0, cptNeutral = 0;
		for (int i = 0; i < neighbors.size(); i++) {
			Polarity polarity = neighbors.get(i).getPolarity();
			switch(polarity) {
				case POSITIVE: cptPositive++; break;
				case NEGATIVE: cptNegative++; break;
				default: cptNeutral++; break;
			}
		}
		if ((cptNeutral >= cptPositive) && (cptNeutral >= cptNegative))
			return Polarity.NEUTRAL;
		else if (cptPositive > cptNegative) 
			return Polarity.POSITIVE;
		else 
			return Polarity.NEGATIVE;
	}
	
	@Override
	public Polarity classifies(MyTweet tweet) {
		ArrayList<MyTweet> nearestNeighbors = this.knnAlgorithm(tweet);
		return this.deduceMajorityClass(nearestNeighbors);
	}

	/**
	 * @param distanceStrategy the distanceStrategy to set
	 */
	public void setDistanceStrategy(DistanceStrategy distanceStrategy) {
		this.metric = distanceStrategy;
	}	
	
	public int getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(int neighbors) {
		this.neighbors = neighbors;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "K-nearest neighbors";
	}
}
