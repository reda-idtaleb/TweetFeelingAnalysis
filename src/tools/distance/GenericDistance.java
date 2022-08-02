package tools.distance;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.Jaccard;
import org.simmetrics.metrics.Levenshtein;
import org.simmetrics.simplifiers.Simplifiers;
import static org.simmetrics.builders.StringMetricBuilder.with;
import static org.simmetrics.tokenizers.Tokenizers.whitespace;

public class GenericDistance implements DistanceStrategy {
	
	StringMetric stringMetric;
	
	public GenericDistance(StringMetric stringMetric) {
		this.stringMetric = stringMetric;
	}

	public static GenericDistance levensteinMetricFactory() {
		return new GenericDistance(buildLevensteinMetric());
	}
	
	public static GenericDistance jaccardMetricFactory() {
		return new GenericDistance(buildJaccardMetric());
	}
	
	private static StringMetric buildLevensteinMetric() {
		return with(new Levenshtein())
				.simplify(Simplifiers.removeDiacritics())
				.simplify(Simplifiers.toLowerCase())
				.build();
	}
	
	private static StringMetric buildJaccardMetric() {
		return with(new Jaccard<String>())
				.tokenize(whitespace())
				.build();			
	}

	@Override
	public float distance(String s1, String s2) {
		return stringMetric.compare(s1, s2); 
	}
	
}
