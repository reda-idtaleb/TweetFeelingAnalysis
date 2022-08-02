package classifier;

import java.util.Objects;

import model.TweetBase;

public abstract class MLClassifiers extends Classifier {
	
	protected TweetBase tweetBase;
	protected Double evaluation;
	
	public MLClassifiers() {
		evaluation = 0.0;
	}
	
	public MLClassifiers(TweetBase tweetBase) {
		this.tweetBase = tweetBase;
		evaluation = 0.0;
	}

	/**
	 * @return the tweetBase
	 */
	public TweetBase getTweetBase() {
		return tweetBase;
	}
	
	public Double getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Double evaluation) {
		this.evaluation = evaluation;
	}

	/**
	 * @param tweetBase the tweetBase to set
	 */
	public void setTweetBase(TweetBase tweetBase) {
		this.tweetBase = tweetBase;
	}
	
	@Override
	public boolean isMLClassifier() {
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tweetBase);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MLClassifiers other = (MLClassifiers) obj;
		return Objects.equals(tweetBase, other.tweetBase);
	}
	
	
}
