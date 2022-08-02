package tools.distance;

public interface DistanceStrategy {
	
	/**
	 * compute the distance between two Strings
	 * @param s1 the first string
	 * @param s2 the second string
	 * @return the distance between two Strings 
	 */
	public float distance(String s1, String s2);
}
