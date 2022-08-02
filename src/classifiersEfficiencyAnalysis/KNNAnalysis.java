package classifiersEfficiencyAnalysis;

import static org.simmetrics.builders.StringMetricBuilder.with;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.Jaccard;
import org.simmetrics.metrics.Levenshtein;
import org.simmetrics.simplifiers.Simplifiers;
import static org.simmetrics.tokenizers.Tokenizers.whitespace;


import classifier.KNNClassifier;
import classifier.MLClassifiers;
import model.TweetBase;
import model.searchTweets.MyTweet;
import model.searchTweets.Polarity;
import tools.distance.DistanceStrategy;
import tools.distance.GenericDistance;


// Pour récupérer la hashMap qui contient les K valeurs (5,10,15,20,50)
// Il suffit d'appeler la fonction fillErrorRates et 
// d'appeler ensuite l'attribut : errorRatesPerNeighborsNumber

public class KNNAnalysis {
	
	public ArrayList<MyTweet> totalBase = new ArrayList<MyTweet>();
    public List<MyTweet> learningBase = new ArrayList<MyTweet>();
    public List<MyTweet> testBase = new ArrayList<MyTweet>();
    public ArrayList<Polarity> correctPolarities = new ArrayList<Polarity>();
    // une HAshMap qui contient les polarité calculées par le classifieur pour chaque K (Nombre de voisins)
    public HashMap<Integer, ArrayList<Polarity>> polaritiesPerNeighborsNumber = new HashMap<Integer, ArrayList<Polarity>>();
    public KNNClassifier classifier = new KNNClassifier();
    public HashMap<Integer, Double> errorRatesPerNeighborsNumber = new HashMap<Integer, Double>();



    public KNNAnalysis() {
    	// On récupère notre base complète
    	this.totalBase = (ArrayList<MyTweet>) new TweetBase("DataBase.csv").getTweetBase();
    	// On la divise 75% learningBase et 25% testBase
    	this.splitTotalBase();
        // On garde la colonne polarité contenant les bons annotations 
        this.testBase.forEach(t -> {this.correctPolarities.add(t.getPolarity());});
        // On met tous les polarité de l'ensemble du test à unpolarisé
        this.testBase.forEach(t -> {t.setPolarity(Polarity.UNPOLARIZED);});
        // On ajoute les polarités correctes à notre HashMap avec la clé 0
        polaritiesPerNeighborsNumber.put(0, correctPolarities);

    }
    
    private void splitTotalBase() {
        int totalBaseSize =  totalBase.size();
        int learningBaseSize = totalBaseSize*75/100;
        int testBaseSize = totalBaseSize-learningBaseSize; 
        learningBase = totalBase.subList(0, learningBaseSize-1);
        testBase = totalBase.subList(totalBaseSize-testBaseSize-1, totalBaseSize);
    }
    

	private int getIncorrectPolaritiesNumber(int NeighborsNumber) {
		int incorrectPolaritiesNumber = 0;
        for (int i =0 ; i<testBase.size(); i++) {
        	if (correctPolarities.get(i) == polaritiesPerNeighborsNumber.get(NeighborsNumber).get(i)) {
        		continue;
        	}
        	else {
        		incorrectPolaritiesNumber ++;
        	}
        }
		return incorrectPolaritiesNumber;
	}

	private void classifyAndAddPolarities(int neighborsNumber) {
		classifier.setNeighbors(neighborsNumber);
        ArrayList<Polarity> polarities = new ArrayList<Polarity>();
        testBase.forEach(t -> {polarities.add(classifier.classifies(t));});
        polaritiesPerNeighborsNumber.put(neighborsNumber, polarities);
	}
	
	private double getErrorRate(int neighborsNumber) {
        classifyAndAddPolarities(neighborsNumber);
        int incorrectPolaritiesNumber = getIncorrectPolaritiesNumber(neighborsNumber);
		return ((double)incorrectPolaritiesNumber/testBase.size())*100;	
	}
	
	private void fillErrorRates() {
		errorRatesPerNeighborsNumber.put(5, getErrorRate(5));
		errorRatesPerNeighborsNumber.put(10, getErrorRate(10));
		errorRatesPerNeighborsNumber.put(15, getErrorRate(15));
		errorRatesPerNeighborsNumber.put(50, getErrorRate(50));
		errorRatesPerNeighborsNumber.put(20, getErrorRate(20));
	}

}
