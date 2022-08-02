package tools.csvIO;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import model.searchTweets.*;

public class CSVWriter {
	
	public static final String EXTENSION = ".csv";
	private static final String DELIMITER = ",";
	private static final String SEPARATOR = "\n";
	
	public static void writeCsv(List<MyTweet> listOfTweets, FileWriter file) throws IOException {
			// Itérer bookList

			Iterator<MyTweet> it = listOfTweets.iterator();
			while (it.hasNext()) {
				MyTweet tweet = (MyTweet) it.next();
				file.append(String.valueOf(tweet.getId()));
				file.append(DELIMITER);
				file.append(tweet.getUserName());
				file.append(DELIMITER);
				file.append(tweet.getTweet().replace('\n', ' ').replace(',', ' '));
				file.append(DELIMITER);
				file.append(String.valueOf(tweet.getDate()));
				file.append(DELIMITER);
				file.append(tweet.getQuery());
				file.append(DELIMITER);
				file.append(String.valueOf(tweet.getPolarity().getValue()));
				file.append(SEPARATOR);
			}
			file.close();
	}
	
}
