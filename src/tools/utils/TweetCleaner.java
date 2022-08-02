package tools.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.searchTweets.MyTweet;

public class TweetCleaner {
	
	// enlève les bruits dans le tweet
	public String clean(String tweet) {
		String tweetWithOutUrl = removeNoise("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", tweet, "");
		String cleanTweet = removeNoise("[^(a-zA-Z àáâãäåçèéêëìíîïðòóôõöùúûüýÿAZÀÁÂÃÄÅÇÈÉÊËÌÍÎÏÐÒÓÔÕÖÙÚÛÜÝŸ)]|RT", tweetWithOutUrl, "");
		return cleanTweet;		
	}
		
	// remplace une chaine de caracteres qui match avec une expression régulière par un replacementString
	public String removeNoise(String regex, String originalString, String replacementString) {
		Pattern p1 = Pattern.compile(regex);
	    Matcher m = p1.matcher(originalString); 
        return m.replaceAll(replacementString);
	}
	
	// vérifie pour chaque tweet si il est français et puis enlève les bruits
	public static List<MyTweet> getCleanTweets(List<MyTweet> tweets){
		List<MyTweet> cleanTweets = new ArrayList<MyTweet>();
		for (MyTweet t : tweets) {
			t.setTweet(new TweetCleaner().clean(t.getTweet()));
			cleanTweets.add(t);
		}
        System.out.println(cleanTweets.toString());

		return cleanTweets;		
		
	}
	
}
