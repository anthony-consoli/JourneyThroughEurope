/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.ui;

import JTE.game.City;
import JTE.game.JTEGameStateManager;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import journeythrougheurope.JourneyThroughEurope.JTEPropertyType;
import properties_manager.PropertiesManager;
import JTE.file.JTEFileLoader;
import java.io.File;


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
    JTEFileLoader fileLoader;
    
    File schemaFile = new File("data/PathXLevelSchema.xsd");
    //CITY XML FILE
    File citiesFile = new File("data/Cali.xml");
    
    private HashMap<String, City> cities;

    public JTEUI() {
        gsm = new JTEGameStateManager(this);
        eventHandler = new JTEEventHandler(this);
        errorHandler = new JTEErrorHandler(primaryStage);
        docManager = new JTEDocumentManager(this);
        fileLoader = new JTEFileLoader(schemaFile);
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
        titleText.setStyle("-fx-font-size: 48");
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
        
        
        GridPane playerGrid = new GridPane();
        Image flagImg1 = loadImage("flag_red.png");
        
        HBox[] boxes = new HBox[6]; 
        
        boxes[0] = createSetupSelection(flagImg1);
        boxes[1] = createSetupSelection(flagImg1);
        boxes[2] = createSetupSelection(flagImg1);
        boxes[3] = createSetupSelection(flagImg1);
        boxes[4] = createSetupSelection(flagImg1);
        boxes[5] = createSetupSelection(flagImg1);
        
        playerGrid.add(boxes[0], 0, 0);
        playerGrid.add(boxes[1], 1, 0);
        playerGrid.add(boxes[2], 2, 0);
        playerGrid.add(boxes[3], 0, 1);
        playerGrid.add(boxes[4], 1, 1);
        playerGrid.add(boxes[5], 2, 1);
        
        for(int i = 0; i<boxes.length; i++)
            boxes[i].setStyle("-fx-border-color: black;"
                    + "-fx-border-style: solid;");
        
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                 public void changed(ObservableValue ov,
                         Number value, Number new_value) {
                    for(int i = 5; i > (int)new_value; i--)
                        boxes[i].setVisible(false);
                    for(int i = 0; i<(int)new_value+1;i++)
                        boxes[i].setVisible(true);
                                    
                 }
             });
        
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

        




        

        
        setupPane.setCenter(playerGrid);
        
        
        
       
        //DISPLAY SETUP PANE IN THE MAIN PANE
         mainPane.setCenter(setupPane);
        
    }

    public void initGameScreen() {
        
        //CLEAR SETUP OR SPLASH SCREEN
        mainPane.setCenter(null);
        initHistoryScreen();
        
        StackPane gamePlayPane = new StackPane();
        SplitPane gameSplitPane = new SplitPane();
        FlowPane  gameFlowPane = new FlowPane();
        ToolBar   gameToolBar = new ToolBar();
        Label     gamePlayerLabel = new Label("Player 1                 ");
        Pane      gameCardPane = new Pane();
        Pane      rightSidePane = new Pane();
        AnchorPane gameBoardPane = new AnchorPane();
        Image q1Img = loadImage("gameplay_AC14.jpg");
        ImageView gameBoardImg = new ImageView();
        gameBoardImg.setImage(q1Img);
       // gameBoardImg.setPreserveRatio(true);
        gameBoardImg.setSmooth(true);
      //  gameBoardImg.setScaleX(.3);
      //  gameBoardImg.fitWidthProperty().bind(rightSidePane.widthProperty());
       // gameBoardImg.fitHeightProperty().bind(rightSidePane.heightProperty());
        VBox rightSidePanel = new VBox();
        Label playerTurnLabel = new Label("Player 1 Turn");
        Image dieImg = loadImage("die_5.jpg");
        ImageView dieImage = new ImageView();
        dieImage.setImage(dieImg);
        
        GridPane quadControl = new GridPane();
        Button q1 = new Button("Q1");
        Button q2 = new Button("Q2");
        Button q3 = new Button("Q3");
        Button q4 = new Button("Q4");
        
        //Add Quadrant Control Boxes to the Grid
        quadControl.add(q1, 0,0);
        quadControl.add(q2, 1, 0);
        quadControl.add(q3, 0,1);
        quadControl.add(q4, 1, 1);
        
        VBox buttonBox = new VBox();
       
        //CREATE HISTORY BUTTON AND HANDLER
        Button historyButton = new Button("Game History");
        historyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_HISTORY_STATE);
            }
        });  
        
        //CREATE ABOUT BUTTON AND HANDLER
        Button aboutButton = new Button("About JTE");
        aboutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_ABOUT_STATE);
            }
        });  
        
        //ADD BUTTONS TO HBOX
        buttonBox.getChildren().add(historyButton);
        buttonBox.getChildren().add(aboutButton);
        
        //CREATE RIGHT SIDE PANEL
        rightSidePanel.getChildren().addAll(playerTurnLabel, dieImage, quadControl, buttonBox);
        
        Label cityLabel = new Label();
        
        
        gameBoardPane.getChildren().add(gameBoardImg);
        gameBoardPane.getChildren().add(cityLabel);
        //LOAD CITIES INTO QUADRANT
        cities = fileLoader.loadCities(citiesFile);
            

        gameBoardPane.getChildren().add(cities.get("ABERDEEN"));
        gameBoardPane.getChildren().add(cities.get("ARHUS"));
        gameBoardPane.getChildren().add(cities.get("BELFAST"));
        gameBoardPane.getChildren().add(cities.get("BERGEN"));
        gameBoardPane.getChildren().add(cities.get("BIRMINGHAM"));
        gameBoardPane.getChildren().add(cities.get("BREMEN"));
        gameBoardPane.getChildren().add(cities.get("CORK"));
        gameBoardPane.getChildren().add(cities.get("DUBLIN"));
        gameBoardPane.getChildren().add(cities.get("FAROER"));
        gameBoardPane.getChildren().add(cities.get("GLASGOW"));
        gameBoardPane.getChildren().add(cities.get("GRONINGEN"));
        gameBoardPane.getChildren().add(cities.get("HAMBURG"));
        gameBoardPane.getChildren().add(cities.get("INVERNESS"));
        gameBoardPane.getChildren().add(cities.get("KIEL"));
        gameBoardPane.getChildren().add(cities.get("LEEDS"));
        gameBoardPane.getChildren().add(cities.get("LIVERPOOL"));
        gameBoardPane.getChildren().add(cities.get("NEWCASTLE"));
        gameBoardPane.getChildren().add(cities.get("REYKJAVIK"));
        gameBoardPane.getChildren().add(cities.get("SHETLAND ISLANDS"));
        gameBoardPane.getChildren().add(cities.get("STAVANGER"));
        
        for(int i = 2;i<22;i++)
        {
            String tmp = gameBoardPane.getChildren().get(i).toString();
            gameBoardPane.getChildren().get(i).setOnMouseClicked(mouseEvent -> {
                cityLabel.setText(tmp);
            });
        }    
        
        rightSidePane.getChildren().addAll(gameBoardPane);
        
        gameToolBar.getItems().add(gamePlayerLabel);

        gameFlowPane.getChildren().addAll(gameToolBar, gameCardPane);
        
        gameSplitPane.getItems().addAll(gameFlowPane, rightSidePane, rightSidePanel);
        gameSplitPane.setDividerPositions(0.2f, 0.8f, 0.9f);
        
        gamePlayPane.getChildren().add(gameSplitPane);
        
        gsm.setGameOn();
        mainPane.setCenter(gamePlayPane);
        
        

    }

    private void initHistoryScreen() {
        
              
        BorderPane historyPane = new BorderPane();
        Label historyLabel = new Label("Game History");
        historyPane.setTop(historyLabel);
        
        Button backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToBackRequest();
            }
        });

        historyPane.setBottom(backButton);
        mainPane.setCenter(historyPane);
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
                initHistoryScreen();
                break;
            case GAME_SETUP_STATE:
                initSetupScreen();
                break;
            default:
        }
    }
    
    public HBox createSetupSelection(Image imageName)
    {
        
        //CREATE RADIO BUTTONS FOR PLAYER/COMPUTER CHOICE
        final ToggleGroup group = new ToggleGroup();
        RadioButton playerChoice = new RadioButton("Player");
        playerChoice.setToggleGroup(group);
        playerChoice.setSelected(true);
        RadioButton cpuChoice = new RadioButton("CPU");
        cpuChoice.setToggleGroup(group);
        
        HBox gridBox = new HBox();
        ImageView flag = new ImageView();
        flag.setImage(imageName);
        flag.setFitWidth(25);
        flag.setPreserveRatio(true);
        flag.setSmooth(true);
        flag.setCache(true);
        Label nameLabel = new Label("Name:");
        TextField nameText = new TextField();
        VBox nameVBox = new VBox();
        VBox imageVBox = new VBox();
        VBox selectVBox = new VBox();
        imageVBox.getChildren().add(flag);
        selectVBox.getChildren().add(playerChoice);
        selectVBox.getChildren().add(cpuChoice);
        nameVBox.getChildren().add(nameLabel);
        nameVBox.getChildren().add(nameText);
        gridBox.getChildren().add(imageVBox);
        gridBox.getChildren().add(selectVBox);
        gridBox.getChildren().add(nameVBox);
        
        return gridBox;
    }        
    
    public Image loadImage(String imageName) 
    {
        Image img = new Image(ImgPath + imageName);
        return img;
    }
    
    
}
