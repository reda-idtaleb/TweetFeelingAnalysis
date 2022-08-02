package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import model.searchTweets.*;

public class SentimentController{
	@FXML PieChart pieChart;
	private AppModel appModel;
	@FXML Label title; 


	public void getPieChart() {
		title.setText("");
		this.pieChart.setVisible(true);
		ArrayList<Integer> stats = this.appModel.getStatistics(this.appModel.getClassifiedTweets());
		pieChart.setStyle("-fx-background-color:white; -fx-background-radius:20");
		pieChart.setTitle("Les proportions de tweets negatifs, positifs et neutres");
		pieChart.setLabelLineLength(10);

		PieChart.Data data1 = new PieChart.Data("Positifs",stats.get(0));
		PieChart.Data data2 = new PieChart.Data("Negatifs",stats.get(1));
		PieChart.Data data3 = new PieChart.Data("Neutres",stats.get(2));
		pieChart.getData().clear();

		pieChart.getData().add(data1);
		pieChart.getData().add(data2);
		pieChart.getData().add(data3);
		
		pieChart.getData().forEach(data -> {
			 String percentage = String.format("%.2f%%", (data.getPieValue() * 100 /stats.get(3)));
			 Tooltip toolTip = new Tooltip(percentage);
			 Tooltip.install(data.getNode(), toolTip);
			});
	}

	public void setAppModel(AppModel model) {
		this.appModel = model;
	}


	
	
	
}
