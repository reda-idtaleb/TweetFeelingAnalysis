package model.searchTweets;


public class MyTweet implements Cloneable{
	
	private String userName;
	private String tweet;	
	private String date;
	private String query;
	private Polarity polarity;
	private String id;	

	public MyTweet(String id, String userName, String tweet, String date, String query, Polarity polarity) {
		this.id = id;
		this.userName = userName;
		this.tweet = tweet;
		this.date = date;
		this.query = query;
		this.polarity = polarity;
	}
	
	public MyTweet(String id, String userName, String tweet, String date, String query) {
		this(id, userName, tweet, date, query, Polarity.UNPOLARIZED);
	}
	
	public MyTweet(String content, Polarity p) {
		this(null, null, content, null, null, p);
	}
	
	public MyTweet(String content) {
		this(null, null, content, null, null, Polarity.UNPOLARIZED);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Polarity getPolarity() {
		return polarity;
	}
	
	/**
	 * @param polarity the polarity to set
	 */
	public void setPolarity(Polarity polarity) {
		this.polarity = polarity;
	}

	@Override
	public String toString() {
		return "Tweet [id=" + id + ", userName=" + userName + ", tweet=" + tweet + ", date=" + date + ", query=" + query
				+ ", polarity=" + polarity + "]";
	}
	
	/** Creation of a cloned Match*
	 * @return an object of match
	 * @throws CloneNotSupportedException when the object can not be cloned*/
	public MyTweet clone() throws CloneNotSupportedException {
		MyTweet monClone = (MyTweet) super.clone();
		return monClone;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyTweet other = (MyTweet) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (polarity != other.polarity)
			return false;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		if (tweet == null) {
			if (other.tweet != null)
				return false;
		} else if (!tweet.equals(other.tweet))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	
}
