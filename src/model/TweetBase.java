package model;

import java.util.ArrayList;
import java.util.List;

import model.searchTweets.*;
import tools.csvIO.CSVReader;

public class TweetBase {
	
	private ArrayList<MyTweet> tweetBase;
	private final static String DATA_BASE_PATH = "./resources/";
	private String path;
	
	public TweetBase() {
		this.tweetBase = new ArrayList<MyTweet>();
	}
	
	public TweetBase(String path) {
		this.tweetBase = new ArrayList<MyTweet>();	
		this.fillTweetBaseFrom(DATA_BASE_PATH+path);
	}
	
	public void fillTweetBaseFrom(String path) {
		CSVReader.readCsv(path).forEach(tweet -> {this.add(tweet);});		
	}
	
	public List<MyTweet> getTweetBase() {
		return new ArrayList<MyTweet>(this.tweetBase);
	}
	
	public void add(MyTweet tweet) {
		this.tweetBase.add(tweet);
	} 
	
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setTweetBase(ArrayList<MyTweet> tweetBase) {
		this.tweetBase = tweetBase;
	}

}
