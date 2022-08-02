package classifier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.searchTweets.*;

public class SimpleClassifier extends Classifier{
	
	private final String POSITIVE_PATH = "./resources/positive.txt";
	private final String NEGATIVE_PATH = "./resources/negative.txt";
	
	public SimpleClassifier() {
	}
	
	private String fileToString(String path) {
		StringBuffer s = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line = "";
		    while ((line = br.readLine()) != null) 
		    	s.append(line);		    
		    br.close();
		} catch (FileNotFoundException e) {
            System.out.println("File " + path + "not found !");
            e.printStackTrace();
		} catch (IOException e) {
            System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return s.toString();
	}
	
	// Add back slash before metachars(smilies) of regex
	private String formatSmilies (String s) {
		String metachars = "()[]";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			String charRead = ((Character) s.charAt(i)).toString();
			if (metachars.contains(charRead)) 
				buf.append("\\" + charRead);
			else 
				buf.append(charRead);
		}
		return buf.toString();
	}
		
	private Polarity getPolarity(String tweetContent, List<String> positiveWords, List<String> negativeWords) {
		String toUpperCase = tweetContent.toUpperCase();
		int cpt = 0;
		for (String positiveWord : positiveWords) {
			String positiveToUpperCase = positiveWord.toUpperCase();
			String regex = ".*" + this.formatSmilies(positiveToUpperCase) + ".*";
			if (toUpperCase.matches(regex))
				cpt++;
		}
		for (String negativeWord : negativeWords) {
			String negativeToUpperCase = negativeWord.toUpperCase();
			String regex = ".*" + this.formatSmilies(negativeToUpperCase) + ".*";	
			if (toUpperCase.matches(regex)) 
				cpt--;
		}
		if (cpt < 0) 
			return Polarity.NEGATIVE;
		else if (cpt > 0) 
			return Polarity.POSITIVE;
		else 
			return Polarity.NEUTRAL;
	}
	
	private List<String> removeEmptyString (List<String> ls) {
		List<String> res = new ArrayList<String>();
		for (String s : ls) {
			String st = s.trim();
			if (!st.isEmpty())
				res.add(st);
		}
		return res;
	}
	
	@Override
	public Polarity classifies(MyTweet tweet) {
		List<String> positiveWords = this.removeEmptyString(Arrays.asList(this.fileToString(POSITIVE_PATH).split(",")));
		List<String> negativeWords = this.removeEmptyString(Arrays.asList(this.fileToString(NEGATIVE_PATH).split(",")));
		return this.getPolarity(tweet.getTweet(), positiveWords, negativeWords);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Mot-cle";
	}
}
