package distance;

import org.junit.jupiter.api.Test;

import tools.distance.DistanceStrategy;
import tools.distance.NaiveDistance;

public class NaiveDistanceTest extends DistanceStrategyTest{
	
	@Override
	public DistanceStrategy createInstance() {
		return new NaiveDistance();
	}
	
	@Test 
	public void testNaiveDistanceBetweenTooSimilarStrings() {
		s1 = "super il fait beau";
		s2 = "super il fait chaud";
		distance = distanceStrategy.distance(s1, s2);
		assert(distance >= 1/2);
	}
	
	@Test 
	public void testNaiveDistanceBetweenTooDiferentStrings() {
		s1 = "super un grand magasin";
		s2 = "super une petite boutique";
		distance = distanceStrategy.distance(s1, s2);
		assert(distance <= 0.5);
	}

	

}
