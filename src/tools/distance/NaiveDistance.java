package tools.distance;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NaiveDistance implements DistanceStrategy {

	@Override
	/**
	 * compute how much common words are in the both of strings
	 * @param s1 the first string
	 * @param s2 the second string
	 * @return the distance between two strings
	 */
	public float distance(String s1, String s2) {
		List<String> s1Words = Arrays.asList(s1.split("\\s"));
		List<String> s2Words = Arrays.asList(s2.split("\\s"));
		List<String> s1WordsUp = s1Words
				.stream()
				.map(s -> s.toUpperCase())
				.collect(Collectors.toList()); 
		List<String> s2WordsUp = s2Words
				.stream()
				.map(s -> s.toUpperCase())
				.collect(Collectors.toList()); 
		int numberOfWords = s1WordsUp.size() + s2WordsUp.size();
		Set<String> commonWords = s1WordsUp.stream()
										 .distinct()
										 .filter(s2WordsUp::contains)
										 .collect(Collectors.toSet());
		float ecart = numberOfWords - 2*commonWords.size();
		float distance = ecart/numberOfWords;
		return 1-distance;
	}

}
