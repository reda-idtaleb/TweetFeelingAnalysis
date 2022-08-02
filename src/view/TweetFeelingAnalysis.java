package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TweetFeelingAnalysis extends Application {
	
	private double xOffset = 0;
	private double yOffset = 0;  
	
	@Override
    public void start(Stage primaryStage) throws Exception {	
        // just load fxml file and display it in the stage:
        FXMLLoader loader = new FXMLLoader(getClass().getResource("app.fxml"));
        System.out.println(loader.toString());
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        root.setStyle("-fx-background-color: transparent;");
    	primaryStage.initStyle(StageStyle.UNDECORATED);
    	
    	root.setOnMousePressed(new EventHandler<MouseEvent>() {
    		@Override
    		public void handle(MouseEvent event) {
    			xOffset = event.getSceneX();
    			yOffset = event.getSceneY();
    		}
		});
    	root.setOnMouseDragged(new EventHandler<MouseEvent>() {
    		@Override
    		public void handle(MouseEvent event) {
    			primaryStage.setX(event.getScreenX() - xOffset);
    			primaryStage.setY(event.getScreenY() - yOffset);
    		}
    	});
    	
        primaryStage.setScene(scene);
        
        primaryStage.show();
        
    }
	
	
}
