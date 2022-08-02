package distance;

import org.junit.jupiter.api.Test;

import tools.distance.GenericDistance;
import tools.distance.DistanceStrategy;

public class LevenstienDistanceTest extends DistanceStrategyTest{
	
	@Override
	protected DistanceStrategy createInstance() {
		return GenericDistance.levensteinMetricFactory();
	}
	
	@Test
	public void testLevenstienNullDistance() {
		distance = this.distanceStrategy.distance("examen", "examen");
		assert(distance == 1.0);
	}
	
	@Test 
	public void testLevensteinPositiveDistance() {
		distance = this.distanceStrategy.distance("niche", "chiens");
		assert(distance <= 0.5);	
	}

	

}
