package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import classifier.Classifier;
import classifier.KNNClassifier;
import classifier.MLClassifiers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.TweetBase;
import model.searchTweets.AppModel;
import model.searchTweets.MyTweet;
import model.searchTweets.Polarity;
import tools.csvIO.CSVWriter;
import tools.utils.ClassifierFactory;

public class UiController implements Initializable{
	
	private AppModel model;
	private double yPositionOfATweet;
	private Classifier currentClassifier;
	private List<MyTweet> listOfClassifiedTweet = new ArrayList<MyTweet>();
	
    @FXML private HBox toolBar;
    @FXML private Spinner<Integer> spinnerScroller;
    @FXML private Button searchButton;
    @FXML private Button clearButton;
    @FXML private TextField keywordField;
    @FXML private AnchorPane tweetsBody;
    @FXML private Label spinnerLabel;
    @FXML private Label keywordErrorMessage; 
    @FXML private Label TotalTweetSize;
    @FXML private Button closeBtn;
    @FXML private Button reduceBtn;
    @FXML private VBox vBoxRoot;
    @FXML private BorderPane borderPane;
    @FXML private ToggleGroup classifier;
    @FXML private Label selectedClassifier;
    @FXML private RadioMenuItem simpleClf;
    @FXML private RadioMenuItem knnClf;
    @FXML private RadioMenuItem bayesPUniClf;
    @FXML private RadioMenuItem bayesPBiClf;
    @FXML private RadioMenuItem bayesPUniBiClf;
    @FXML private RadioMenuItem bayesFUniClf;
    @FXML private RadioMenuItem bayesFBiClf;
    @FXML private RadioMenuItem bayesFUniBiClf;
    @FXML private RadioMenuItem allBaseBtn;
    @FXML private RadioMenuItem ileviaBaseBtn;
    @FXML private RadioMenuItem equipeBaseBtn;
    @FXML private RadioMenuItem electionBaseBtn;
    @FXML private Menu classifierMenu;
    @FXML private Button globalFeeling;
    @FXML private Button classifierEvaluation;
    @FXML private BorderPane mainView;
    @FXML private MenuItem export;
    private List<Double> allEvaluationsAllBase;
    private String buttonSelected;
    private List<Double> allEvaluations;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1){
    	 // initialisation du modèle de l'application
        
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2000000, 20);
		this.simpleClf.setToggleGroup(classifier);
		this.knnClf.setToggleGroup(classifier);
		this.bayesPUniClf.setToggleGroup(classifier);
		this.bayesPBiClf.setToggleGroup(classifier);
		this.bayesPUniBiClf.setToggleGroup(classifier);
		this.bayesFUniClf.setToggleGroup(classifier);
		this.bayesFBiClf.setToggleGroup(classifier);
		this.bayesFUniBiClf.setToggleGroup(classifier);
		this.spinnerScroller.setValueFactory(valueFactory);
        this.keywordField.getStyleClass().add("keywordF");
        this.clearButton.getStyleClass().add("clearBtn");
        this.searchButton.getStyleClass().add("searchBtn");
        this.spinnerLabel.getStyleClass().add("spinnerLabel");
        this.spinnerScroller.getStyleClass().add("spinnerScroller");  
        this.closeBtn.getStyleClass().add("highlight");
        this.reduceBtn.getStyleClass().add("highlight");
        
        globalFeeling.getStyleClass().add("rightMenuLabel");
        globalFeeling.setOnMouseEntered(e -> globalFeeling.setStyle("-fx-background-radius: 0; -fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill:white"));
        globalFeeling.setOnMouseExited(e -> globalFeeling.setStyle("-fx-background-color: transparent; -fx-text-fill: white"));
       
        classifierEvaluation.getStyleClass().add("rightMenuLabel");
        classifierEvaluation.setOnMouseEntered(e -> classifierEvaluation.setStyle("-fx-background-radius: 0; -fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill:white"));
        classifierEvaluation.setOnMouseExited(e -> classifierEvaluation.setStyle("-fx-background-color: transparent; -fx-text-fill: white"));
        
        this.spinnerScroller.hoverProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(spinnerScroller.isHover())
					spinnerLabel.setStyle("-fx-effect: dropshadow(three-pass-box, blue, 1, 0, 0, 0);");				
				else
					spinnerLabel.setStyle("-fx-effect: dropshadow(three-pass-box, white, 0, 0, 0, 0);");	
			}});  
        this.keywordField.setOnKeyPressed( event -> {
        	if( event.getCode() == KeyCode.ENTER ) 
        		this.displayTweets();
        });
        this.model = new AppModel();
        // initialisation du classifieur par défaut
		if (this.knnClf.isSelected()) {
			String defaultClf = "K-nearest neighbors";
			this.updateCurrentClassifier(defaultClf);
			this.selectedClassifier.setText("Classifieur courant: "+defaultClf);
		}//-fx-background-color: #826dc9;
		else 
			this.selectedClassifier.setText("No classifier selected !");
		
		this.buttonSelected = "sentimentglobal";
		this.allEvaluations = this.evaluateAllClassifiers();
		allEvaluationsAllBase = new ArrayList<Double>();
		
		allEvaluations.forEach(d -> allEvaluationsAllBase.add(d));
    }

	@FXML
	private void handleSimpleClf(ActionEvent event) {
		this.makeSelectedClassifier(event);
		this.selectedClassifier.setText("The current classifier is: " + this.simpleClf.getText());
		invokeCurrentClassifier();
		invokeBarChart(this.allEvaluations);
	}
	
	@FXML
	private void handleKnnClf(ActionEvent event) {
		this.makeSelectedClassifier(event);
		this.selectedClassifier.setText("The current classifier is: " + this.knnClf.getText());
		invokeCurrentClassifier();
		invokeBarChart(this.allEvaluations);
	}
	
	@FXML
	private void handleBPUniClf(ActionEvent event) {
		this.makeSelectedClassifier(event);
		this.selectedClassifier.setText("The current classifier is: Bayes presence(1-gram)");
		invokeCurrentClassifier();
		invokeBarChart(this.allEvaluations);
	}
	
	@FXML
	private void handleBPBiClf(ActionEvent event) {
		this.makeSelectedClassifier(event);
		this.selectedClassifier.setText("The current classifier is: Bayes presence(2-gram)") ;
		invokeCurrentClassifier();
		invokeBarChart(this.allEvaluations);
	}
	
	@FXML
	private void handleBPUniBiClf(ActionEvent event) {
		this.makeSelectedClassifier(event);
		this.selectedClassifier.setText("The current classifier is: Bayes presence(1&2-gram)") ;
		invokeCurrentClassifier();
		invokeBarChart(this.allEvaluations);
	}
	
	@FXML
	private void handleBFUniClf(ActionEvent event) {
		this.makeSelectedClassifier(event);
		this.selectedClassifier.setText("The current classifier is: Bayes frequence(1-gram)");
		invokeCurrentClassifier();
		invokeBarChart(this.allEvaluations);
	}
	
	@FXML
	private void handleBFBiClf(ActionEvent event) {
		this.makeSelectedClassifier(event);
		this.selectedClassifier.setText("The current classifier is: Bayes frequence(2-gram)");
		invokeCurrentClassifier();
		invokeBarChart(this.allEvaluations);
	}
	
	@FXML
	private void handleBFUniBiClf(ActionEvent event) {
		this.makeSelectedClassifier(event);
		this.selectedClassifier.setText("The current classifier is: Bayes frequence(1&2-gram)");
		invokeCurrentClassifier();
		invokeBarChart(this.allEvaluations);
	}
	
	@FXML
	private void exportCSV(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Exportez...");
	    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV Files (*.csv)", "*.csv"));
	    fileChooser.setInitialFileName("donnee"+new Date().getTime()+".csv");
	    File exportpath = fileChooser.showSaveDialog(null);
	    FileWriter f;
		try {		
			f = new FileWriter(exportpath.getPath());
			CSVWriter.writeCsv(listOfClassifiedTweet, f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * update the current classifier and the classifier of the model
	 * @param clf the selected classifier
	 */
	private void updateCurrentClassifier(String clf) {
		this.currentClassifier = ClassifierFactory.getClassifier(clf, model);
		this.model.setClassifier(currentClassifier);
	}
	
	private void makeSelectedClassifier(ActionEvent event) {
		String prefix = "";
		String b1 = "1&2-gram";
		String clf = ((RadioMenuItem) event.getSource()).getText();		
		if (clf.contentEquals("1-gram") | clf.contentEquals("2-gram") | clf.contentEquals(b1)) {
			prefix += ((RadioMenuItem) event.getSource()).getParentMenu().getText();
			if(clf.contentEquals(b1)) 
				clf = prefix + "(" + b1 + ")";
			else
				clf = prefix +"("+ clf +")";
		}
		Label l = new Label(clf);
		this.updateCurrentClassifier(clf);
	    l.setStyle("-fx-text-fill: #4320c4;");
	    this.classifierMenu.setText("");
	    this.classifierMenu.setGraphic(l);
	}
	
	@FXML
	protected void handleCloseAction(ActionEvent event) {
		Stage stage = (Stage) closeBtn.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	protected void handleReduceAction(ActionEvent event) {
		Stage stage = (Stage) reduceBtn.getScene().getWindow();
		stage.setIconified(true);
	}
	
	@FXML
	protected void handleAllBase(ActionEvent event) {	
		this.model.setTweetBase(new TweetBase("DataBase.csv"));
		this.model.updateClassifiers();
		this.model.updateClassifiers(allEvaluationsAllBase);
		invokeBarChart(this.allEvaluationsAllBase);
	}
	
	@FXML
	protected void handleIleviaBase(ActionEvent event) {
		String base = ((RadioMenuItem) event.getSource()).getText();
		this.model.setTweetBase(new TweetBase("ileviaBase.csv"));
		this.model.updateClassifiers();	
		this.allEvaluations = this.evaluateAllClassifiers();
		invokeBarChart(this.allEvaluations);
	}
	
	@FXML
	protected void handleEquipeBase(ActionEvent event) {
		String base = ((RadioMenuItem) event.getSource()).getText();
		this.model.setTweetBase(new TweetBase("equipeBase.csv"));
		this.model.updateClassifiers();
		this.allEvaluations = this.evaluateAllClassifiers();
		invokeBarChart(this.allEvaluations);
	}
	
	@FXML
	protected void handleElectionBase(ActionEvent event) {
		String base = ((RadioMenuItem) event.getSource()).getText();
		this.model.setTweetBase(new TweetBase("electionBase.csv"));
		this.model.updateClassifiers();
		this.allEvaluations = this.evaluateAllClassifiers();
		invokeBarChart(this.allEvaluations);
	}
	
	public Object loadController(FXMLLoader loader) {
		return loader.getController();
	}
	
	@FXML
	protected void handleViewAction(ActionEvent event) {
        String btnItemID = ((Button) event.getSource()).getText();
        if (btnItemID.contentEquals("Sentiment global")) {
        	this.buttonSelected = "sentimentglobal";
        	globalFeeling.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2)");
        	invokePieChart();
        }
        else if (btnItemID.contentEquals("Evaluation")) {
        	this.buttonSelected = "evaluation";
        	classifierEvaluation.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2)");
        	invokeCurrentClassifier();
            invokeBarChart(this.allEvaluations);
            
        } 
	}
	
	private void invokeCurrentClassifier() {
		FXMLLoader loader = this.loadRessource(this.buttonSelected);
		Object loaderControler = this.loadController(loader);
		if (loaderControler instanceof EvaluationController) {
			((EvaluationController)loaderControler).setAppModel(this.model);
			if(!this.model.getClassifiedTweets().isEmpty())
				((EvaluationController)loaderControler).updateCurrentClfText(model.getClassifier().toString());
		}
	}
	
	private void invokePieChart() {
		FXMLLoader loader = this.loadRessource(this.buttonSelected);
		System.out.println("invokePieChart() | LOADER : " + loader.getLocation().getPath());
		Object loaderControler = this.loadController(loader);
		if (loaderControler instanceof SentimentController) {
			((SentimentController)loaderControler).setAppModel(this.model);
			if(!this.model.getClassifiedTweets().isEmpty())
				((SentimentController)loaderControler).getPieChart();
		}
	}
	
	private void invokeBarChart(List<Double> list) {
		FXMLLoader loader = this.loadRessource(this.buttonSelected);
		Object loaderControler = this.loadController(loader);
		if (loaderControler instanceof EvaluationController) {
			((EvaluationController)loaderControler).setAppModel(this.model);
			((EvaluationController)loaderControler).getBarChart(list);
		}
	}
	
	private FXMLLoader loadRessource(String fxmlName) {
		try {
			URL s = getClass().getResource("/view/"+fxmlName + ".fxml");
	        FXMLLoader loader = new FXMLLoader(s);
	        System.out.println("loadRessource(String fxmlName) | LOADER = " + loader.getLocation().getPath());
	        mainView.setCenter(loader.load());
	        return loader;
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@FXML
	protected void handleSpreadAction(MouseEvent event) {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		if (stage.isMaximized())
			stage.setMaximized(false);
		else
			stage.setMaximized(true);
	}
	
	private void configureCommonStyleOfATweet(TextFlow tweet, double yPosition, String backgroundColor, String borderColor) {
		tweet.setPrefWidth(335.0);
		tweet.setLayoutX(12.0);
		tweet.setLayoutY(yPosition);	
		tweet.setLineSpacing(3.0);
		tweet.setStyle("-fx-background-radius: 15 15 15 15;" 
				+ "-fx-background-color: #" + backgroundColor.substring(2) + ";"
				+ "-fx-effect: dropshadow(three-pass-box, #"+ borderColor.substring(2) + ", 3, 0, 0, 1);");
		((Text)tweet.getChildren().get(0)).setFont(Font.font("dyuthi", 13));
		((Text)tweet.getChildren().get(0)).setFill(Color.web("0x424242"));	
	}
	
	
	public double makeAStylishTweet(TextFlow tweet, double yPosition) {
		this.configureCommonStyleOfATweet(tweet, yPosition, Color.WHITE.toString(), Color.GRAY.toString());
		tweet.setPadding(new Insets(15, 15, 25, 15));
		double w = tweet.prefWidth(-1);
		double h = tweet.prefHeight(w);
		return h;
	}

	public void makeAStylishErrorMessage(TextFlow errorTextFlow, double yPositionOfATweet) {
		this.configureCommonStyleOfATweet(errorTextFlow, yPositionOfATweet, Color.web("0xffbaba").toString(), Color.web("0xD8000C").toString());
		errorTextFlow.setPadding(new Insets(15, 15, 15, 30));
		((Text)errorTextFlow.getChildren().get(0)).setFill(Color.web("0xD8000C"));
	}
	
	public void defaultScrollPaneStyle() {
		this.tweetsBody.setStyle("-fx-background-color: #ebebed;"
				+ "-fx-background-radius: 20px 20px 20px 20px;");
	}
	
	
	
	private void displayErrorMsgWhenEmptyRequest() {
		this.keywordErrorMessage.setTextFill(Color.web("0xd44424")); 
	}
	
	private boolean isEmptyRequest() {
		if (this.keywordField.getText().isEmpty())
			return true;
		return false;	
	}
	
	public Double evaluateCurrentClassifier() {
		return this.model.evaluateClassifier();
	}
	
	public void displayTweets() {
		if (this.isEmptyRequest()) {
			this.displayErrorMsgWhenEmptyRequest();
			return; //Don't send a query if nothing is entered
		}
		else 
			this.keywordErrorMessage.setTextFill(Color.WHITE);
		
		this.model.clear();  // clear the list of tweets before re-searching
		this.tweetsBody.getChildren().clear(); 		
		Integer spinnerValue = this.spinnerScroller.getValue();		
		this.model.setNumberOfTweets(spinnerValue);
		try {
			this.organizeTweets();					
		} catch (Exception e) {
			Text errorLabel= new Text("An error is occured while query is sent to twitter server! Try again!");
			final TextFlow errorMessage = new TextFlow(errorLabel);
			this.makeAStylishErrorMessage(errorMessage, yPositionOfATweet);
		};			
	}

	private void organizeTweets() throws Exception {	
		this.yPositionOfATweet = 14.0;
		this.model.setClassifiedTweets(new ArrayList<>());
		listOfClassifiedTweet = this.model.classifies(this.model.searchTweets(this.keywordField.getText()));
		List<MyTweet> listUncleanedTweets = this.model.getUncleanedTweets();
		this.tweetsBody.setStyle("-fx-background-color: #ebebed; -fx-background-radius:  20");
		int i = 0;
		if (!listOfClassifiedTweet.isEmpty()) {
			TextFlow labeledTweet = new TextFlow();
			for (MyTweet tweet : listOfClassifiedTweet) {		
				Text labelContent = new Text(listUncleanedTweets.get(i).getTweet());
				labeledTweet = new TextFlow(labelContent);
				double h = this.makeAStylishTweet(labeledTweet, yPositionOfATweet);
				this.tweetsBody.getChildren().add(labeledTweet);
				yPositionOfATweet += h ;
				ImageView smiley = this.addSmilies(tweet, yPositionOfATweet);
				this.tweetsBody.getChildren().add(smiley);
				this.tweetsBody.getChildren().add(addPositiveSmileyButton(smiley.getLayoutX(), smiley.getLayoutY(), tweet.getId()));
				this.tweetsBody.getChildren().add(addNegativeSmileyButton(smiley.getLayoutX(), smiley.getLayoutY(), tweet.getId()));
				this.tweetsBody.getChildren().add(addNeutralSmileyButton(smiley.getLayoutX(), smiley.getLayoutY(), tweet.getId()));
				yPositionOfATweet += 40; 
				i++;
			}
			invokePieChart();

			TextFlow lastTextFlow = new TextFlow(new Text(" "));
			makeAStylishTweet(lastTextFlow, yPositionOfATweet-50);
			lastTextFlow.setPrefWidth(50);
			lastTextFlow.setOpacity(0);
			this.tweetsBody.getChildren().add(lastTextFlow);	
		}
		else {
			final TextFlow emptyErrorMessage = new TextFlow(new Text("No tweets retrieved! Please try another keyword!"));
			this.makeAStylishErrorMessage(emptyErrorMessage, yPositionOfATweet);
			this.tweetsBody.getChildren().add(emptyErrorMessage);
			this.tweetsBody.setStyle("-fx-background-color: transparent;");
		}
	}

	private List<MyTweet> classifies(List<MyTweet> tweets){
		return this.model.classifies(tweets);	
	}
	
	private ImageView addSmilies(MyTweet t, double yPosition) {
		ImageView smiley = t.getPolarity().getSmileyFace();
		smiley.setFitWidth(27);
		smiley.setFitHeight(29);
		smiley.setLayoutX(316);
		smiley.setLayoutY(yPosition-31);
		smiley.getStyleClass().add("face");

		return smiley;
	}
	
	public void clearKeywordField() {
		this.keywordField.setText("");
		this.hideClearButton();
	}
	
	public void changeColorWhenkeywordFieldSelected() {	
		this.keywordField.setStyle("-fx-background-color: white; "
				+ "-fx-border-color: white white #3b1aa8 white;"
				+ "-fx-border-width: 1.5px;");
		this.showClearButton();
	}
	
	public void showClearButton() {
		if (this.keywordField.getText().length() != 0)
			this.clearButton.setStyle("-fx-text-fill: #3b1aa8;"
				+ "-fx-border-color: white;"
				+ "-fx-background-color: white;");
		else
			this.hideClearButton();		
	}

	public void hideClearButton() {
		this.clearButton.setStyle("-fx-text-fill: white;"
				+ "-fx-border-color: white;"
				+ "-fx-background-color: white;");
	}
	
	public void setDefaultColorWhenBodyClicked() {
		this.keywordErrorMessage.setTextFill(Color.WHITE);
		this.keywordField.setStyle("-fx-background-color: white; "
				+ "-fx-border-color: white white lightgray white;");
		this.tweetsBody.requestFocus();
		if (this.keywordField.getText().length() != 0)
			this.clearButton.setStyle("-fx-text-fill: #a1a1a1;"
				+ "-fx-border-color: white;"
				+ "-fx-background-color: white;");
		else
			this.hideClearButton();
	}
	
	public void beDarkButtonWhenPressed() {		
		this.searchButton.setStyle("-fx-background-color: #220f61;"
				+ "-fx-background-radius: 0px 10px 10px 0px;");
	}
	
	public void defaultButtonColorWhenReleased() {	
		this.searchButton.setStyle("-fx-background-color: #3b1aa8;"
				+ "-fx-background-radius: 0px 10px 10px 0px;");	
	}
	
	public Button addPositiveSmileyButton(double xPos, double yPos, String id) {
		Button positiveButton = new Button();
		positiveButton.getStyleClass().add(id + " " + yPos);
		positiveButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    @Override
		    public void handle(ActionEvent event) {
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	Button btn = (Button) event.getSource();
		    	String tweetId = btn.getStyleClass().toString().split(" ")[1];
		    	String y = btn.getStyleClass().toString().split(" ")[2];
		    	// On récupère tous les smilies dans les tweets affichés
		    	Set<Node> faces = tweetsBody.lookupAll(".face");
		    	ImageView face = null;
		    	for (Node n : faces) {
		    		if (((ImageView) n).getLayoutY() == yPos) {
		    			face = (ImageView) n;
		    		}
		    	}
		    	for (MyTweet t : listOfClassifiedTweet) {
		    		if (t.getId().contentEquals(tweetId)){
		    			t.setPolarity(Polarity.POSITIVE);
		    			invokePieChart();
		    			face.setVisible(false);
						tweetsBody.getChildren().add(addSmilies(t, Double.parseDouble(y) +31));
		    		}
		    	}
		    }
		});
		positiveButton.setTranslateX(xPos - 90);
		positiveButton.setTranslateY(yPositionOfATweet + 5);
		ImageView view = Polarity.POSITIVE.getSmileyFace();
		view.setFitWidth(20);
		view.setFitHeight(20);
		positiveButton.setGraphic(view);
		return positiveButton;
	}
	
	public Button addNegativeSmileyButton(double xPos, double yPos, String id) {
		Button negativeButton = new Button();
		negativeButton.getStyleClass().add(id + " " + yPos);
		negativeButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    @Override
		    public void handle(ActionEvent event) {
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	Button btn = (Button) event.getSource();
		    	String tweetId = btn.getStyleClass().toString().split(" ")[1];
		    	String y = btn.getStyleClass().toString().split(" ")[2];
		    	// On récupère tous les smilies dans les tweets affichés
		    	Set<Node> faces = tweetsBody.lookupAll(".face");
		    	ImageView face = null;
		    	for (Node n : faces) {
		    		if (((ImageView) n).getLayoutY() == yPos) {
		    			face = (ImageView) n;
		    		}
		    	}
		    	for (MyTweet t : listOfClassifiedTweet) {
		    		if (t.getId().contentEquals(tweetId)){
		    			t.setPolarity(Polarity.NEGATIVE);
		    			invokePieChart();
		    			face.setVisible(false);
						tweetsBody.getChildren().add(addSmilies(t, Double.parseDouble(y) +31));
		    		}
		    	}
		    }
		});
		negativeButton.setTranslateX(xPos - 50);
		negativeButton.setTranslateY(yPositionOfATweet + 5);
		ImageView view = Polarity.NEGATIVE.getSmileyFace();
		view.setFitWidth(20);
		view.setFitHeight(20);
		negativeButton.setGraphic(view);
		return negativeButton;
	}
	
	public Button addNeutralSmileyButton(double xPos, double yPos, String id) {
		Button neutralButton = new Button();
		neutralButton.getStyleClass().add(id + " " + yPos);
		neutralButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    @Override
		    public void handle(ActionEvent event) {
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	Button btn = (Button) event.getSource();
		    	String tweetId = btn.getStyleClass().toString().split(" ")[1];
		    	String y = btn.getStyleClass().toString().split(" ")[2];
		    	// On récupère tous les smilies dans les tweets affichés
		    	Set<Node> faces = tweetsBody.lookupAll(".face");
		    	ImageView face = null;
		    	for (Node n : faces) {
		    		if (((ImageView) n).getLayoutY() == yPos) {
		    			face = (ImageView) n;
		    		}
		    	}
		    	for (MyTweet t : listOfClassifiedTweet) {
		    		if (t.getId().contentEquals(tweetId)){
		    			t.setPolarity(Polarity.NEUTRAL);
		    			invokePieChart();
		    			face.setVisible(false);
						tweetsBody.getChildren().add(addSmilies(t, Double.parseDouble(y) +31));
		    		}
		    	}
		    }
		});
		neutralButton.setTranslateX(xPos - 10);
		neutralButton.setTranslateY(yPositionOfATweet + 5);
		ImageView view = Polarity.NEUTRAL.getSmileyFace();
		view.setFitWidth(20);
		view.setFitHeight(20);
		neutralButton.setGraphic(view);
		return neutralButton;
	}
	
	public List<Double> evaluateAllClassifiers(){
		List<Double> h = new ArrayList<Double>();
		List<MLClassifiers> clf = this.model.getClassifiers();
		clf.forEach(c -> {		
			double eval = model.evaluateClassifier(c);
			System.out.println(c + " " + eval);
			h.add(eval);		
		});
		return h;
	}
	
}
