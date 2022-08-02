package distance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tools.distance.DistanceStrategy;

public abstract class DistanceStrategyTest {
	
	protected String s1, s2;
	protected float distance;
	protected DistanceStrategy distanceStrategy;
	
	@BeforeEach
	public void init() {
		this.distanceStrategy = this.createInstance();
	}
	
	protected abstract DistanceStrategy createInstance();
	
	@Test
	public void testEqualDistance() {
		distance =  this.distanceStrategy.distance("TemPs", "temps");
		assert(distance == 1.0);
		
		distance =  this.distanceStrategy.distance("Un exEmple de Phrase", "un exeMple DE phrase");
		assert(distance == 1.0);
	}
}
