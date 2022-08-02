package tools.csvIO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.searchTweets.*;

public class CSVReader {

	private static final String COMMA_DELIMITER = ",";

	// returns list of tweets read from CSV file
	public static List<MyTweet> readCsv(String path) {
		List<MyTweet> tweets = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line;
			if (br.readLine() == null)
				return tweets;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(COMMA_DELIMITER);
				if (values.length > 0) {
					MyTweet tweet = new MyTweet(values[0], values[1], values[2], values[3], values[4],
							Polarity.fromValue(Integer.valueOf(values[5])));
					tweets.add(tweet);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return tweets;
	}
}
