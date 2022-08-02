package controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import classifier.*;
import classifier.Classifier;
import classifier.KNNClassifier;
import classifier.bayesStrategy.BigramStrategy;
import classifier.bayesStrategy.*;
import classifiersEfficiencyAnalysis.CrossValidation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import model.searchTweets.*;

public class EvaluationController {
	@FXML private Label title;
	@FXML private Label pourcentage;
	@FXML private Label clfName;
	@FXML private BarChart<String, Double> bc;
	@FXML private Label info;
    
	private AppModel model;
	
	private static final DecimalFormat df = new DecimalFormat("0.000");

	public void getBarChart(List<Double> h) {
		Classifier currentClassifier = model.getClassifier();
		this.clfName.setText(currentClassifier.toString());
		if(currentClassifier.isMLClassifier()) {				
			this.pourcentage.setOpacity(1);		
			this.pourcentage.setText(this.getEvaluation(((MLClassifiers)currentClassifier).getEvaluation())+"%");
			this.info.setVisible(true);
			bc.setVisible(true);
			final CategoryAxis xAxis = new CategoryAxis();
	        final NumberAxis yAxis = new NumberAxis();
	        bc.setTitle("Comparaison des taux d'erreur des classifieurs");
	        xAxis.setLabel("Classifieur");       
	        yAxis.setLabel("Taux d'erreur");
			
	        double[] error = new double[7];
	        for (MLClassifiers c : this.model.getClassifiers()) {
	            if(c instanceof KNNClassifier )
	            	error[0] = c.getEvaluation();
	            else if(c instanceof BayesPresenceClassifier) { 
	            	if ((((BayesPresenceClassifier)c).getBayesStrategy()) instanceof UnigramStrategy)
	            		error[1] = c.getEvaluation();
	            	else if ((((BayesPresenceClassifier)c).getBayesStrategy()) instanceof BigramStrategy)
	            		error[2] = c.getEvaluation();
	            	else if ((((BayesPresenceClassifier)c).getBayesStrategy()) instanceof CompositeStrategy)
	            		error[3] = c.getEvaluation();
	            }
	            else if(c instanceof BayesFrequenceClassifier) { 
	            	if ((((BayesFrequenceClassifier)c).getBayesStrategy()) instanceof UnigramStrategy)
	            		error[4] = c.getEvaluation();
	            	else if ((((BayesFrequenceClassifier)c).getBayesStrategy()) instanceof BigramStrategy)
	            		error[5] = c.getEvaluation();
	            	else if ((((BayesFrequenceClassifier)c).getBayesStrategy()) instanceof CompositeStrategy)
	            		error[6] = c.getEvaluation();
	            }
	        }
	        
	        System.out.println("here");
	        XYChart.Series series0 = new XYChart.Series();
	        series0.setName("KNN");       
	        series0.getData().add(new XYChart.Data("KNN", error[0]));
	        
	        XYChart.Series series1 = new XYChart.Series();
	        series1.setName("1-gram");       
	        series1.getData().add(new XYChart.Data("BayesPresence", error[1]));
	        series1.getData().add(new XYChart.Data("BayesFrequence", error[4]));
	        
	        XYChart.Series series2 = new XYChart.Series();
	        series2.setName("2-gram");
	        series2.getData().add(new XYChart.Data("BayesPresence", error[2]));
	        series2.getData().add(new XYChart.Data("BayesFrequence", error[5]));
	        
	        XYChart.Series series3 = new XYChart.Series();
	        series3.setName("1-gram & 2-gram");
	        series3.getData().add(new XYChart.Data("BayesPresence", error[3]));
	        series3.getData().add(new XYChart.Data("BayesFrequence", error[6]));
	        bc.getData().addAll(series0, series1, series2, series3);
		}
		else {
			this.pourcentage.setText("");
			this.bc.setOpacity(0);
			this.clfName.setText("Evaluation impossible sur le classifieur Mot-cle");
			this.info.setVisible(false);
		}
	}

	private String getEvaluation(double d) {
		return df.format(d);
	}
	
	public void updateCurrentClfText(String s) {
		this.clfName.setText(s);
	}
	
	public void setAppModel(AppModel model2) {
		this.model = model2;
		
	}
	
}
