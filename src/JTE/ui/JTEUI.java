/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.ui;

import JTE.game.JTEGameStateManager;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import journeythrougheurope.JourneyThroughEurope.JTEPropertyType;
import properties_manager.PropertiesManager;

/**
 *
 * @author Anthony
 */
public class JTEUI extends Pane {

    public enum JTEUIState {

        SPLASH_SCREEN_STATE, GAME_SETUP_STATE, PLAY_GAME_STATE, VIEW_ABOUT_STATE, VIEW_HISTORY_STATE,
        VIEW_FLIGHT_STATE
    }

    // mainStage
    private Stage primaryStage;

    // mainPane
    private BorderPane mainPane;
    private BorderPane hmPane;

    // SplashScreen
    private Pane splashPane;
    private Label splashScreenImageLabel;
    private HBox languageSelectionPane;
    private ArrayList<Button> splashButtons;
    
    // AboutScreen
    private Pane aboutPane;
    
    //HistoryScreen
    private Pane historyPane;
    
    //Game Screen Variables
    private Pane gamePane;
    

    // Padding
    private Insets marginlessInsets;

    // Image path
    private String ImgPath = "file:img/";

    // mainPane weight && height
    private int paneWidth;
    private int paneHeigth;

    // THIS CLASS WILL HANDLE ALL ACTION EVENTS FOR THIS PROGRAM
    private JTEEventHandler eventHandler;
    private JTEErrorHandler errorHandler;
    private JTEDocumentManager docManager;

    JTEGameStateManager gsm;

    public JTEUI() {
        gsm = new JTEGameStateManager(this);
        eventHandler = new JTEEventHandler(this);
        errorHandler = new JTEErrorHandler(primaryStage);
        docManager = new JTEDocumentManager(this);
        initMainPane();
        initAboutScreen();
        initSplashScreen();

    }

    public void SetStage(Stage stage) 
    {
        primaryStage = stage;
    }
    
    public BorderPane GetMainPane() 
    {
        return this.mainPane;
    }
    
    public void initSplashScreen() {
        // INIT THE SPLASH SCREEN CONTROLS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String splashScreenImagePath = props
                .getProperty(JTEPropertyType.SPLASH_SCREEN_IMAGE_NAME);
        
        BorderPane splashPane = new BorderPane();
        splashPane.getStyleClass().add("pane");
        
        splashPane.setStyle("-fx-background-image: url(File:img/splash.jpg)");
        // Image image = new Image("JTE.jpg");
        ImageView iv1 = new ImageView();
       // iv1.setImage(image);

        
        //ADD THE TITLE LABEL FOR THE GAME
        Label titleText = new Label("Journey Through Europe");
        splashPane.setTop(titleText);
        splashPane.setAlignment(titleText, Pos.TOP_CENTER);

        //CREATE START BUTTON THEN APPLY AN EVENT HANDLER
        Button startButton = new Button("Start Game");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToSwitchScreenRequest(JTEUIState.GAME_SETUP_STATE);
            }
        });
        
        //CREATE LOAD BUTTON AND APPLY EVENT HANDLER
        Button loadButton = new Button("Load Game");
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToLoadGameRequest();
            }
        });
        
        //CREATE ABOUT BUTTON AND APPLY EVENT HANDLER
        Button aboutButton = new Button("About JTE");
        aboutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_ABOUT_STATE);
            }
        });        
        
        
        //CREATE QUIT BUTTON AND CREATE EVENT HANDLER
        Button quitButton = new Button("Quit Game");
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToExitRequest();
            }
        });
        
        //CREATE A VBOX TO CONTAIN SPLASH SCREEN OPTIONS AND ADD BUTTONS TO THE LIST
        VBox vbBtn = new VBox(10);
        vbBtn.setAlignment(Pos.BOTTOM_CENTER);
        vbBtn.getChildren().add(startButton);
        vbBtn.getChildren().add(loadButton);
        vbBtn.getChildren().add(aboutButton);
        vbBtn.getChildren().add(quitButton);
        splashPane.setBottom(vbBtn);

        mainPane.setCenter(splashPane);
        //grid.setGridLinesVisible(true);
        
        
    }

    public void initMainPane() {
       
        marginlessInsets = new Insets(5, 5, 5, 5);
        mainPane = new BorderPane();

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        paneWidth = Integer.parseInt(props
                .getProperty(JTEPropertyType.WINDOW_WIDTH));
        paneHeigth = Integer.parseInt(props
                .getProperty(JTEPropertyType.WINDOW_HEIGHT));
        mainPane.resize(paneWidth, paneHeigth);
        mainPane.setPadding(marginlessInsets);
    }

    private void initSetupScreen() {
        
        //FIRST CLEAR THE SPLASH SCREEN
        mainPane.setCenter(null);
        
       //CREATE SETUP PANE
        BorderPane setupPane = new BorderPane();
        setupPane.setPadding(marginlessInsets);
        setupPane.setStyle("-fx-background-color: #D2B48C");

        //CREATE UPPER TOOLBAR FOR NUMBER OF PLAYERS
        HBox numHb = new HBox(5);
        Label numPlay = new Label("Players: ");
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
        "1", "2", "3", "4", "5", "6"));
        
        //GO BUTTON THAT STARTS JTE GAME
        Button goBtn = new Button("Go!");
        goBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
             eventHandler.respondToNewGameRequest();
         }
        });
        
        //ADD COMPONENTS TO THE TOOL PLAY AND PLACE THEM AT THE TOP
        numHb.getChildren().add(numPlay);
        numHb.getChildren().add(cb);
        numHb.getChildren().add(goBtn);
        setupPane.setTop(numHb);

        

        //CREATE RADIO BUTTONS FOR PLAYER/COMPUTER CHOICE
        final ToggleGroup group = new ToggleGroup();
        RadioButton playerChoice = new RadioButton("Player");
        playerChoice.setToggleGroup(group);
        playerChoice.setSelected(true);
        RadioButton cpuChoice = new RadioButton("Computer");
        cpuChoice.setToggleGroup(group);
        
       
        //DISPLAY SETUP PANE IN THE MAIN PANE
         mainPane.setCenter(setupPane);
        
    }

    private void initGameScreen() {
        
        //CLEAR SETUP OR SPLASH SCREEN
        mainPane.setCenter(null);
        

    }

    private void initHistoryScreen() {

    }

    private void initAboutScreen() {
        
        BorderPane aboutPane = new BorderPane();
        Label aboutLabel = new Label("This is the About page!!!");
        aboutPane.setTop(aboutLabel);
        
        Button backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToBackRequest();
            }
        });
        
        
        aboutPane.setBottom(backButton);
       
        mainPane.setCenter(aboutPane);
        
        
        

    }

    public void initFlightPlan() {

    }

    public JTEGameStateManager getGSM() 
    {
        return gsm;
    }

    public void changeWorkspace(JTEUIState uiScreen) 
    {
        switch (uiScreen) {
            case VIEW_ABOUT_STATE:
                initAboutScreen();
                break;
            case PLAY_GAME_STATE:
                initGameScreen();
                break;
            case SPLASH_SCREEN_STATE:
                initSplashScreen();
                break;
            case VIEW_HISTORY_STATE:
                mainPane.setCenter(historyPane);
                break;
            case GAME_SETUP_STATE:
                initSetupScreen();
                break;
            default:
        }
    }
    
    public Image loadImage(String imageName) 
    {
        Image img = new Image(ImgPath + imageName);
        return img;
    }
}
