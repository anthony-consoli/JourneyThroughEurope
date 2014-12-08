/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.ui;

import JTE.game.City;
import JTE.game.JTEGameStateManager;
import java.util.ArrayList;
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
import JTE.game.FlightCity;
import JTE.game.JTEPlayer;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 *
 * @author Anthony
 */
public class JTEUI extends Pane {

    public enum JTEUIState {

        SPLASH_SCREEN_STATE, GAME_SETUP_STATE, PLAY_GAME_STATE, VIEW_ABOUT_STATE, VIEW_HISTORY_STATE,
        VIEW_FLIGHT_STATE , VIEW_INFO_STATE
    }

    public enum JTEQuadState {

        QUAD_1, QUAD_2, QUAD_3, QUAD_4, FLIGHT;
    }

    public enum JTEPlayerState {

        PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4, PLAYER_5, PLAYER_6;
    }

    //
    PropertiesManager props = PropertiesManager.getPropertiesManager();
    
    // mainStage
    private Stage primaryStage;

    // Image path
    private String ImgPath = "file:img/";

    // mainPane
    private BorderPane mainPane;
    private BorderPane hmPane;

    // SplashScreen
    private BorderPane splashPane;
    private Label splashScreenImageLabel;
    private HBox languageSelectionPane;
    private ArrayList<Button> splashButtons;

    // AboutScreen
    private BorderPane aboutPane;

    //HistoryScreen
    private BorderPane historyPane;
    private VBox historyBox;
    private ScrollPane sp;

    //Info Screen
    private BorderPane infoPane;
    private VBox infoBox;
    private ScrollPane infoSp;

    //Game Screen Variables
    private Pane gamePane;
    private AnchorPane gameBoardPane;
    private StackPane gamePlayPane;
    private SplitPane gameSplitPane;
    private FlowPane gameFlowPane;
    private ToolBar gameToolBar;
    private Label gamePlayerLabel;
    private Pane gameCardPane;
    private Pane rightSidePane;
    private BorderPane setupPane;
    Button flightButton;
    Image q1Img = loadImage("gameplay_AC14.jpg");
    Image q2Img = loadImage("gameplay_DF14.jpg");
    Image q3Img = loadImage("gameplay_AC58.jpg");
    Image q4Img = loadImage("gameplay_DF58.jpg");
    Image flightPlan = loadImage("Flight_Plan1.jpg");
    String aboutString = ("Journey through Europe is a family board game published by Ravensburger.\nThe board is a map of Europe with various major cities marked,\nfor example, Athens, Amsterdam and London.\nThe players are given a home city from which they will\nbegin and are then dealt a number of cards with various other cities on them.\nThey must plan a route between each of the cities in their hand of cards.\nOn each turn they throw a die and move between the cities. \nThe winner is the first player to visit each of their cities and then return to their home base.\n\nCredits: Anthony Consoli and Ravenburger Games\nDecember 8th, 2014");
    private ImageView gameBoardImg;
    private VBox rightSidePanel;
    private Label playerTurnLabel;
    private Label dieLabel;
    private Image dieImg;
    private ImageView dieImage;
    private boolean[] cpuPlayer;

    // Padding
    private Insets marginlessInsets;

    // mainPane weight && height
    private int paneWidth;
    private int paneHeigth;

    // THIS CLASS WILL HANDLE ALL ACTION EVENTS FOR THIS PROGRAM
    private JTEEventHandler eventHandler;
    private JTEErrorHandler errorHandler;
    private JTEDocumentManager docManager;

    //CURRENT QUADRANT
    private JTEQuadState currentQuadrant;

    Label cityLabel;

    JTEGameStateManager gsm;
    JTEFileLoader fileLoader;
    
    

    File schemaFile = new File("data/JTESchema.xsd");
    //CITY XML FILE
    File citiesFile = new File("data/JTE.xml");

    //JUST FOR SPRITE TESTING / CARD TESTING
    Image pic = loadImage("piece_black.png");
    ImageView p1 = new ImageView(pic);
    double xPos;
    double yPos;
    final double offsetX = 25;
    final double offsetY = 40;

    
    Image die1 = loadImage("die_1.jpg");
    Image die2 = loadImage("die_2.jpg");
    Image die3 = loadImage("die_3.jpg");
    Image die4 = loadImage("die_4.jpg");
    Image die5 = loadImage("die_5.jpg");
    Image die6 = loadImage("die_6.jpg");
    double yOff = 60;
    double xOff = 8;
    int k = 1;

    JTEPlayer currentPlayer;

    private City[] cities;
    private FlightCity[] flightCities;
    private HashMap<String, FlightCity> flightHash;

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

    public void SetStage(Stage stage) {
        primaryStage = stage;
    }

    public BorderPane GetMainPane() {
        return this.mainPane;
    }

    public void initSplashScreen() {
        // INIT THE SPLASH SCREEN CONTROLS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String splashScreenImagePath = props
                .getProperty(JTEPropertyType.SPLASH_SCREEN_IMAGE_NAME);

        splashPane = new BorderPane();
        splashPane.getStyleClass().add("pane");

        splashPane.setStyle("-fx-background-image: url(File:img/splash.jpg)");
        Image boxImage = loadImage("Game.jpg");
        // Image image = new Image("JTE.jpg");
        ImageView iv1 = new ImageView(boxImage);
       // iv1.setImage(image);

        //ADD THE TITLE LABEL FOR THE GAME
        Label titleText = new Label("Journey Through Europe");
        titleText.setStyle("-fx-font-size: 48");
        splashPane.setTop(titleText);
        splashPane.setAlignment(titleText, Pos.TOP_CENTER);
        splashPane.setCenter(iv1);
        splashPane.setAlignment(iv1, Pos.CENTER);

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

        changeWorkspace(JTEUIState.SPLASH_SCREEN_STATE);
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
        //mainPane.setCenter(null);
        //CREATE SETUP PANE
        setupPane = new BorderPane();
        setupPane.setPadding(marginlessInsets);
        setupPane.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058 );");
        cpuPlayer = new boolean[6];
        Arrays.fill(cpuPlayer, false);

        //CREATE UPPER TOOLBAR FOR NUMBER OF PLAYERS
        HBox numHb = new HBox(5);
        Label numPlay = new Label("Players: ");

        GridPane playerGrid = new GridPane();
        Image flagImg1 = loadImage("flag_red.png");

        HBox[] boxes = new HBox[6];

        boxes[0] = createSetupSelection(flagImg1, 0);
        boxes[1] = createSetupSelection(flagImg1, 1);
        boxes[2] = createSetupSelection(flagImg1, 2);
        boxes[3] = createSetupSelection(flagImg1, 3);
        boxes[4] = createSetupSelection(flagImg1, 4);
        boxes[5] = createSetupSelection(flagImg1, 5);

        playerGrid.add(boxes[0], 0, 0);
        playerGrid.add(boxes[1], 1, 0);
        playerGrid.add(boxes[2], 2, 0);
        playerGrid.add(boxes[3], 0, 1);
        playerGrid.add(boxes[4], 1, 1);
        playerGrid.add(boxes[5], 2, 1);

        for (int i = 0; i < boxes.length; i++) {
            boxes[i].setStyle("-fx-border-color: black;"
                    + "-fx-border-style: solid;");
        }

        //NUMBER OF PLAYERS TO BE SELECTED
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "1", "2", "3", "4", "5", "6"));
        cb.getSelectionModel().selectLast();
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov,
                    Number value, Number new_value) {
                for (int i = 5; i > (int) new_value; i--) {
                    boxes[i].setVisible(false);
                }
                for (int i = 0; i < (int) new_value + 1; i++) {
                    boxes[i].setVisible(true);
                }

            }
        });

        //GO BUTTON THAT STARTS JTE GAME BASED ON PLAYER INFO SELECTED
        Button goBtn = new Button("Go!");
        goBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (int i = 0; i < cpuPlayer.length; i++) {
                    System.out.println(cpuPlayer[i]);
                }
                eventHandler.respondToNewGameRequest(cb.getSelectionModel().selectedIndexProperty().getValue() + 1, cpuPlayer);

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
        initInfoScreen();
        gamePlayPane = new StackPane();
        gameSplitPane = new SplitPane();
        gameFlowPane = new FlowPane();
        gameFlowPane.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058 );");
        gameToolBar = new ToolBar();
        //gameToolBar.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058 );");
        gamePlayerLabel = new Label("Player 1                 ");
        gameCardPane = new Pane();
        rightSidePane = new Pane();
        gameBoardPane = new AnchorPane();
        gameBoardPane.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058);");
        gameBoardImg = new ImageView();
        gameBoardImg.setImage(q1Img);
        gameBoardImg.setSmooth(true);
        rightSidePanel = new VBox();
        rightSidePanel.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058 );");
        playerTurnLabel = new Label("Player 1 Turn");
        dieImage = new ImageView();

        //SET UP QUADRANT SELECTION BUTTONS
        GridPane quadControl = new GridPane();
        Button q1 = new Button("Q1");
        q1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToQuadrantRequest(1);
            }
        });
        Button q2 = new Button("Q2");
        q2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToQuadrantRequest(2);
            }
        });
        Button q3 = new Button("Q3");
        q3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToQuadrantRequest(3);
            }
        });
        Button q4 = new Button("Q4");
        q4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToQuadrantRequest(4);
            }
        });

        //ADD QUADRANT CONTROLS TO THE GRID
        quadControl.add(q1, 0, 0);
        quadControl.add(q2, 1, 0);
        quadControl.add(q3, 0, 1);
        quadControl.add(q4, 1, 1);

        //CREATE HISTORY BUTTON AND HANDLER
        Button historyButton = new Button("Game History");
        historyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                updateHistory();
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

        //CREATE ABOUT BUTTON AND HANDLER
        flightButton = new Button("Flight Plan");

        flightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToFlightScreenRequest();
            }
        });

        //CREATE SAVE BUTTON AND HANDLER
        Button saveButton = new Button("Save");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToSaveGameRequest(gsm.getGameInProgress().getPlayers());

            }
        });
        
        //CREATE INFO BUTTON AND HANDLER
        Button infoButton = new Button("City Info");
        infoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_INFO_STATE);
            }
        });

        //CREATE VBOX FOR BUTTONS
        VBox buttonBox = new VBox();
        buttonBox.setSpacing(10);

        //ADD BUTTONS TO VBOX
        buttonBox.getChildren().add(quadControl);
        buttonBox.getChildren().add(flightButton);
        buttonBox.getChildren().add(historyButton);
        buttonBox.getChildren().add(aboutButton);
        buttonBox.getChildren().add(saveButton);
        buttonBox.getChildren().add(infoButton);

        //TESTING LABEL TO DISPLAY CITY CLICKED
        cityLabel = new Label("City");
        dieLabel = new Label("Dice Points: ");

        //ADD COMPONENTS TO RIGHT SIDE PANEL
        rightSidePanel.getChildren().addAll(playerTurnLabel, dieImage, dieLabel, buttonBox, cityLabel);

        //THIS CODE WILL HAVE TO CHANGE TODO.....
        gameBoardPane.getChildren().add(gameBoardImg);

        //LOAD ALL CITIES INTO BOARD
        cities = gsm.getGameInProgress().getCities();
        for (int i = 0; i < cities.length; i++) {
            gameBoardPane.getChildren().add(cities[i]);
            //THIS WILL SET THE CIRCULAR CITY OBJECTS TRANSPARENT
            cities[i].setOpacity(0);
        }

        //LOAD FLIGHT CITIES
        initFlightPlan();

        for (int i = 1; i < cities.length + 1; i++) {
            String tmpStr = gameBoardPane.getChildren().get(i).toString();
            City tmp = (City) gameBoardPane.getChildren().get(i);
            tmp.setOnMouseClicked(mouseEvent -> {
                if (tmp != currentPlayer.getPreviousCity()) {
                    eventHandler.respondToCityRequest(tmp, currentPlayer, tmpStr, tmp.getX(), tmp.getY());
                }

            });
            tmp.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    tmp.setCursor(Cursor.HAND);
                }
            });
            tmp.setOnMouseDragEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                eventHandler.respondToCityRequest(tmp, currentPlayer, tmpStr, tmp.getX(), tmp.getY());
                }
            });            
        }

        //TESTING
        initPlayerSprites();

        rightSidePane.getChildren().addAll(gameBoardPane);

        gameToolBar.getItems().add(gamePlayerLabel);

        gameFlowPane.getChildren().addAll(gameToolBar, gameCardPane);

        gameSplitPane.getItems().addAll(gameFlowPane, rightSidePane, rightSidePanel);
        gameSplitPane.setDividerPositions(0.2f, 0.8f, 0.9f);

        gamePlayPane.getChildren().add(gameSplitPane);

        //gsm.setGameOn();
        changeWorkspace(JTEUIState.PLAY_GAME_STATE);

    }

    public void initPlayerSprites() {

        Iterator<JTEPlayer> it = gsm.getGameInProgress().getPlayers().iterator();
        while (it.hasNext()) {
            JTEPlayer playerTmp = it.next();
            ImageView tmpSpr = playerTmp.getSpritePiece();
            ImageView tmpFlg = playerTmp.getFlagPiece();
            AnchorPane cardPane = playerTmp.getCardPane();
            playerTmp.setCurrentCity(playerTmp.getCurrentCity());
            setPlayerPosition(playerTmp.getCurrentCity(), playerTmp, playerTmp.getCurrentCity().getName(), playerTmp.getCurrentCity().getX(), playerTmp.getCurrentCity().getY());
            tmpFlg.setX(playerTmp.getHomeCity().getX() - offsetX + 10);
            tmpFlg.setY(playerTmp.getHomeCity().getY() - offsetY + 10);
            gameCardPane.getChildren().add(cardPane);
            cardPane.setVisible(false);
            gameBoardPane.getChildren().add(tmpSpr);
            gameBoardPane.getChildren().add(tmpFlg);
            // allow the label to be dragged around.
            final Delta dragDelta = new Delta();
             tmpSpr.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    tmpSpr.startFullDrag();
                }
            });
        }
        if(!gsm.getGameInProgress().getCurrentPlayer().isLoaded())
        {
            changeQuadrant(gsm.getGameInProgress().getCurrentPlayer().getCurrentCity().getQuad());
            setCurrentPlayer(gsm.getGameInProgress().getCurrentPlayer());    
            dealCards();
        }
            else
        {
            gsm.setCardsVisible();
            clearLines();
            gsm.startTurn();
            drawLines(gsm.getGameInProgress().getCurrentPlayer());
        }
        
    }

    private void initHistoryScreen() {

        historyPane = new BorderPane();

        Label historyLabel = new Label("Game History");
        historyBox = new VBox();

        historyPane.setCenter(historyBox);
        historyPane.setTop(historyLabel);
 

        Button backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToBackRequest();
            }
        });

        historyPane.setBottom(backButton);

        sp = new ScrollPane();
        //sp.setStyle
        sp.setContent(historyPane);
    }

    private void initInfoScreen() {

        infoPane = new BorderPane();

        Label infoLabel = new Label("City Information");
        infoBox = new VBox();

        infoPane.setCenter(infoBox);
        infoPane.setTop(infoLabel);
        infoPane.setAlignment(infoLabel, Pos.TOP_CENTER);

        Button backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToBackRequest();
            }
        });

        infoPane.setBottom(backButton);

        infoSp = new ScrollPane();
        //sp.setStyle
        infoSp.setContent(infoPane);
        loadCityInfo();
    }

    public void loadCityInfo() {
        ArrayList<String> info = new ArrayList();
        File file = new File("data/SRSText.txt");
        try {

            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                info.add(sc.nextLine());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < info.size(); i++) {
            Label label = new Label(info.get(i));
            infoBox.getChildren().add(label);
        }
    }

    public void updateHistory() {
        historyBox.getChildren().clear();
        ArrayList<String> history = gsm.getGameHistory();
        for (int i = 0; i < history.size(); i++) {
            Label label = new Label(history.get(i));
            historyBox.getChildren().add(label);
        }
    }

    private void initAboutScreen() {

        aboutPane = new BorderPane();
        aboutPane.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058 );");
        Label aboutTitle = new Label("About Journey Through Europe");
        aboutPane.setTop(aboutTitle);
        aboutPane.setAlignment(aboutTitle, Pos.TOP_CENTER);
        Label aboutLabel = new Label(aboutString);
        aboutPane.setAlignment(aboutLabel, Pos.CENTER);
        aboutLabel.setStyle("-fx-font-size: 26");
        aboutPane.setCenter(aboutLabel);
        Button backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToBackRequest();
            }
        });

        aboutPane.setBottom(backButton);

    }

    public void initFlightPlan() {

        flightHash = gsm.getGameInProgress().getFlightCities();
        Iterator it = flightHash.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            FlightCity tmpCity = (FlightCity) pairs.getValue();
            String tmpStr = (String) pairs.getKey();
            int destSector = tmpCity.getSector();
            City destCity = gsm.getGameInProgress().getCityHash().get(tmpStr);
            gameBoardPane.getChildren().add(tmpCity);
            double tmpX = gsm.getGameInProgress().getCityHash().get(tmpStr).getX();
            double tmpY = gsm.getGameInProgress().getCityHash().get(tmpStr).getY();
            tmpCity.setOnMouseClicked(mouseEvent -> {
                int currentSector = flightHash.get(gsm.getGameInProgress().getCurrentPlayer().getCurrentCity().getName()).getSector();
                eventHandler.respondToFlightRequest(destCity, currentPlayer, currentSector, destSector, tmpStr, tmpX, tmpY);

            });
            tmpCity.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    tmpCity.setCursor(Cursor.HAND);
                }
            });
            tmpCity.setOpacity(0);
        }

    }

    public JTEGameStateManager getGSM() {
        return gsm;
    }

    public JTEEventHandler getEventHandler() {
        return eventHandler;
    }

    public void changeWorkspace(JTEUIState uiScreen) {
        switch (uiScreen) {
            case VIEW_ABOUT_STATE:
                mainPane.setCenter(aboutPane);
                break;
            case PLAY_GAME_STATE:
                mainPane.setCenter(gamePlayPane);
                break;
            case SPLASH_SCREEN_STATE:
                mainPane.setCenter(splashPane);
                break;
            case VIEW_HISTORY_STATE:
                mainPane.setCenter(sp);
                break;
            case GAME_SETUP_STATE:
                initSetupScreen();
                break;
            case VIEW_INFO_STATE:
                mainPane.setCenter(infoSp);
                break;
            default:
        }
    }

    public HBox createSetupSelection(Image imageName, int i) {

        //CREATE RADIO BUTTONS FOR PLAYER/COMPUTER CHOICE
        final ToggleGroup group = new ToggleGroup();
        RadioButton playerChoice = new RadioButton("Player");
        playerChoice.setToggleGroup(group);
        playerChoice.setSelected(true);
        RadioButton cpuChoice = new RadioButton("CPU");
        cpuChoice.setToggleGroup(group);

        playerChoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                cpuPlayer[i] = false;
            }
        });
        cpuChoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                cpuPlayer[i] = true;
            }
        });

        HBox gridBox = new HBox();
        gridBox.setPrefSize(400, 400);
        ImageView flag = new ImageView();
        flag.setImage(imageName);
        flag.setFitWidth(25);
        flag.setPreserveRatio(true);
        flag.setSmooth(true);
        flag.setCache(true);
        Label nameLabel = new Label("       Name:");
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

    public Image loadImage(String imageName) {
        Image img = new Image(ImgPath + imageName);
        return img;
    }

    public void changeQuadrant(int quadNum) {
        clearLines();
        if (quadNum == gsm.getGameInProgress().getCurrentPlayer().getCurrentCity().getQuad()) {
            drawLines(gsm.getGameInProgress().getCurrentPlayer());
        } else {
            clearLines();
        }
        Iterator<JTEPlayer> it = gsm.getGameInProgress().getPlayers().iterator();
        while (it.hasNext()) {
            JTEPlayer tmpPlayer = it.next();
            if (tmpPlayer.getCurrentCity().getQuad() != quadNum) {
                tmpPlayer.getSpritePiece().setVisible(false);
            } else {
                //drawLines(tmpPlayer);
                tmpPlayer.getSpritePiece().setVisible(true);
            }
            if (tmpPlayer.getHomeCity().getQuad() != quadNum) {
                tmpPlayer.getFlagPiece().setVisible(false);
            } else {
                tmpPlayer.getFlagPiece().setVisible(true);
            }
        }

        if (quadNum == 1) {
            currentQuadrant = JTEQuadState.QUAD_1;
            gameBoardImg.setImage(q1Img);

            for (int i = 0; i < cities.length; i++) {
                switch (cities[i].getQuad()) {
                    case 1:
                        gameBoardPane.getChildren().get(i + 1).setVisible(true);
                        break;
                    case 2:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 3:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 4:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                }
            }
            for (int i = 0; i < gameBoardPane.getChildren().size(); i++) {
                if (gameBoardPane.getChildren().get(i).getClass() == FlightCity.class) {
                    gameBoardPane.getChildren().get(i).setVisible(false);
                }
            }

        } else if (quadNum == 2) {
            currentQuadrant = JTEQuadState.QUAD_2;
            gameBoardImg.setImage(q2Img);

            for (int i = 0; i < cities.length; i++) {
                switch (cities[i].getQuad()) {
                    case 1:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 2:
                        gameBoardPane.getChildren().get(i + 1).setVisible(true);
                        break;
                    case 3:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 4:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                }
            }
            for (int i = 0; i < gameBoardPane.getChildren().size(); i++) {
                if (gameBoardPane.getChildren().get(i).getClass() == FlightCity.class) {
                    gameBoardPane.getChildren().get(i).setVisible(false);
                }
            }
        } else if (quadNum == 3) {
            currentQuadrant = JTEQuadState.QUAD_3;
            gameBoardImg.setImage(q3Img);

            for (int i = 0; i < cities.length; i++) {
                switch (cities[i].getQuad()) {
                    case 1:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 2:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 3:
                        gameBoardPane.getChildren().get(i + 1).setVisible(true);
                        break;
                    case 4:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                }
            }
            for (int i = 0; i < gameBoardPane.getChildren().size(); i++) {
                if (gameBoardPane.getChildren().get(i).getClass() == FlightCity.class) {
                    gameBoardPane.getChildren().get(i).setVisible(false);
                }
            }
        } else if (quadNum == 4) {
            currentQuadrant = JTEQuadState.QUAD_4;
            gameBoardImg.setImage(q4Img);

            for (int i = 0; i < cities.length; i++) {
                switch (cities[i].getQuad()) {
                    case 1:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 2:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 3:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 4:
                        gameBoardPane.getChildren().get(i + 1).setVisible(true);
                        break;
                }
            }
            for (int i = 0; i < gameBoardPane.getChildren().size(); i++) {
                if (gameBoardPane.getChildren().get(i).getClass() == FlightCity.class) {
                    gameBoardPane.getChildren().get(i).setVisible(false);
                }
            }
        } else if (quadNum == 5) {
            currentQuadrant = JTEQuadState.FLIGHT;
            clearLines();
            gameBoardImg.setImage(flightPlan);

            for (int i = 0; i < cities.length; i++) {
                switch (cities[i].getQuad()) {
                    case 1:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 2:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 3:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;
                    case 4:
                        gameBoardPane.getChildren().get(i + 1).setVisible(false);
                        break;

                }
            }
            for (int i = 0; i < gameBoardPane.getChildren().size(); i++) {
                if (gameBoardPane.getChildren().get(i).getClass() == FlightCity.class) {
                    gameBoardPane.getChildren().get(i).setVisible(true);
                }
            }
        }
    }

    public void setPlayerPosition(City c, JTEPlayer player, String tempStr, double x, double y) {
        if (c.hasAirport()) {
            setFlightButton(true);
        } else {
            setFlightButton(false);
        }

        if (!player.isCpu()) {
            player.getSpritePiece().setVisible(true);
        } else {
            changeQuadrant(c.getQuad());
        }
        clearLines();
        cityLabel.setText(tempStr);
        double xDest = x - offsetX;
        double yDest = y - offsetY;
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(1000), player.getSpritePiece());
        translateTransition1.setFromX(player.getCurrentX() - offsetX);
        translateTransition1.setFromY(player.getCurrentY() - offsetY);
        translateTransition1.setToX(x - offsetX);
        translateTransition1.setToY(y - offsetY);
        clearLines();
        translateTransition1.play();
        player.setCurrentCity(c);
        clearLines();
        drawLines(player);
    }

    public void updatePlayerPosition(City c, JTEPlayer player, String tempStr, String type, double x, double y, int mov) {
        if (c.hasAirport()) {
            setFlightButton(true);
        } else {
            setFlightButton(false);
        }

        if (!player.isCpu() && type != "FLY") {
            player.getSpritePiece().setVisible(true);
        } else {
            changeQuadrant(c.getQuad());
            player.getSpritePiece().setVisible(true);
        }
        if (!player.getBeginTurn() && type.equals("SEA")) {
            gsm.changeTurn();
            gsm.startTurn();
        } else {
            player.setBeginTurn(false);
            clearLines();
            cityLabel.setText(tempStr);
            double xDest = x - offsetX;
            double yDest = y - offsetY;
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(1000), player.getSpritePiece());
            translateTransition1.setFromX(player.getCurrentX() - offsetX);
            translateTransition1.setFromY(player.getCurrentY() - offsetY);
            translateTransition1.setToX(x - offsetX);
            translateTransition1.setToY(y - offsetY);
            clearLines();
            translateTransition1.play();
            player.setPreviousCity(player.getCurrentCity());
            player.setCurrentCity(c);
            clearLines();
            drawLines(player);
            player.makeMove(mov);
            gsm.addToHistory("Player " + player.getPlayNum() + ": moved to " + c.getName());
            translateTransition1.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    dieLabel.setText("Dice Points: " + player.getDicePoints());
                    for (int i = 0; i < player.getCards().size(); i++) {
                        if (c.equals(player.getCards().get(i).getCity()) && c != player.getHomeCity()) {
                            player.getCardPane().getChildren().remove(i);
                            player.removeCard(player.getCards().get(i));
                            player.setDicePoints(0);
                            gsm.addToHistory("Player " + player.getPlayNum() + ": returned " + c.getName() + " to the dealer");
                        }
                    }
                    if (player.getCards().size() == 1 && c == player.getHomeCity()) {
                        player.getCardPane().getChildren().remove(0);
                        player.removeCard(player.getCards().get(0));
                        player.setDicePoints(1);
                        eventHandler.respondToGameOver(player);
                    }

                    if (player.isCpu() && player.getDicePoints() > 0) {
                        gsm.cpuTurn();
                    } else if (player.getDicePoints() <= 0) {
                        if (player.getRollAgain()) {
                            player.roll();
                            updateDie(player.getDicePoints());
                            if (player.isCpu()) {
                                gsm.cpuTurn();
                            }
                        } else {
                            gsm.changeTurn();
                            gsm.startTurn();
                            //drawLines(gsm.getGameInProgress().getCurrentPlayer());
                        }
                    }

                }
            });
        }
    }

    public void setCurrentPlayer(JTEPlayer player) {
        currentPlayer = player;
        Iterator<JTEPlayer> it = gsm.getGameInProgress().getPlayers().iterator();
        while (it.hasNext()) {
            JTEPlayer tmp = it.next();
            if (tmp == currentPlayer) {
                tmp.getCardPane().setVisible(true);
            } else {
                tmp.getCardPane().setVisible(false);
            }
        }

        switch (player.getPlayNum()) {
            case 1:
                playerTurnLabel.setText("Player 1 Turn");
                gamePlayerLabel.setText("Player 1                 ");
                break;
            case 2:
                playerTurnLabel.setText("Player 2 Turn");
                gamePlayerLabel.setText("Player 2                 ");
                break;
            case 3:
                playerTurnLabel.setText("Player 3 Turn");
                gamePlayerLabel.setText("Player 3                 ");
                break;
            case 4:
                playerTurnLabel.setText("Player 4 Turn");
                gamePlayerLabel.setText("Player 4                 ");
                break;
            case 5:
                playerTurnLabel.setText("Player 5 Turn");
                gamePlayerLabel.setText("Player 5                 ");
                break;
            case 6:
                playerTurnLabel.setText("Player 6 Turn");
                gamePlayerLabel.setText("Player 6                 ");
                break;
        }

    }

    /**
     * THIS METHOD DRAWS RED LINES BETWEEN NEIGHBORING CITIES IN EACH QUADRANT
     */
    public void drawLines(JTEPlayer p) {
        double x1 = p.getCurrentCity().getX();
        double y1 = p.getCurrentCity().getY();
        for (int i = 0; i < p.getCurrentCity().getLandNeighbors().size(); i++) {
            if (p.getCurrentCity().getLandNeighbors().get(i).getQuad() == p.getCurrentCity().getQuad()) {
                double x2 = p.getCurrentCity().getLandNeighbors().get(i).getX();
                double y2 = p.getCurrentCity().getLandNeighbors().get(i).getY();

                Line line1 = new Line(x1, y1, x2, y2);
                line1.setStrokeWidth(5);
                line1.setStroke(Color.RED);
                line1.setMouseTransparent(true);
                gameBoardPane.getChildren().add(line1);
            }
        }
        for (int i = 0; i < p.getCurrentCity().getSeaNeighbors().size(); i++) {
            if (p.getCurrentCity().getSeaNeighbors().get(i).getQuad() == p.getCurrentCity().getQuad()) {
                double x2 = p.getCurrentCity().getSeaNeighbors().get(i).getX();
                double y2 = p.getCurrentCity().getSeaNeighbors().get(i).getY();

                Line line2 = new Line(x1, y1, x2, y2);
                line2.setStrokeWidth(5);
                line2.setStroke(Color.RED);
                line2.setMouseTransparent(true);
                gameBoardPane.getChildren().add(line2);
            }
        }

    }

    /**
     * THIS METHOD REMOVES RED LINES DRAWN AS PATHS FROM PREVIOUSLY VISITED
     * CITIES
     */
    public void clearLines() {
        for (int i = 0; i < gameBoardPane.getChildren().size(); i++) {
            if (gameBoardPane.getChildren().get(i).getClass() == Line.class) {
                gameBoardPane.getChildren().remove(i);
            }
        }

    }

    public void updateDie(int i) {
        switch (i) {
            case 1:
                dieImage.setImage(die1);
                dieLabel.setText("Dice Points: 1");
                break;
            case 2:
                dieImage.setImage(die2);
                dieLabel.setText("Dice Points: 2");
                break;
            case 3:
                dieImage.setImage(die3);
                dieLabel.setText("Dice Points: 3");
                break;
            case 4:
                dieImage.setImage(die4);
                dieLabel.setText("Dice Points: 4");
                break;
            case 5:
                dieImage.setImage(die5);
                dieLabel.setText("Dice Points: 5");
                break;
            case 6:
                dieImage.setImage(die6);
                dieLabel.setText("Dice Points: 6");
                break;
        }
    }

    public void goBackToCity(City c, JTEPlayer player, String tempStr, double x, double y) {
        player.getSpritePiece().setVisible(true);
        clearLines();
        cityLabel.setText(tempStr);

        double xDest = x - offsetX;
        double yDest = y - offsetY;
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(1000), player.getSpritePiece());
        translateTransition1.setFromX(player.getCurrentX() - offsetX);
        translateTransition1.setFromY(player.getCurrentY() - offsetY);
        translateTransition1.setToX(x - offsetX);
        translateTransition1.setToY(y - offsetY);
        clearLines();
        translateTransition1.play();
        player.setCurrentCity(c);
        clearLines();
        drawLines(player);
    }

    public void changeScreen(int k) {

        Iterator<JTEPlayer> it = gsm.getGameInProgress().getPlayers().iterator();
        while (it.hasNext()) {
            JTEPlayer tmp = it.next();
            if (tmp.getPlayNum() == k) {
                tmp.getCardPane().setVisible(true);
            } else {
                tmp.getCardPane().setVisible(false);
            }
        }

        switch (k) {
            case 1:
                playerTurnLabel.setText("Player 1 Turn");
                gamePlayerLabel.setText("Player 1                 ");
                break;
            case 2:
                playerTurnLabel.setText("Player 2 Turn");
                gamePlayerLabel.setText("Player 2                 ");
                break;
            case 3:
                playerTurnLabel.setText("Player 3 Turn");
                gamePlayerLabel.setText("Player 3                 ");
                break;
            case 4:
                playerTurnLabel.setText("Player 4 Turn");
                gamePlayerLabel.setText("Player 4                 ");
                break;
            case 5:
                playerTurnLabel.setText("Player 5 Turn");
                gamePlayerLabel.setText("Player 5                 ");
                break;
            case 6:
                playerTurnLabel.setText("Player 6 Turn");
                gamePlayerLabel.setText("Player 6                 ");
                break;
        }
    }

    public void setFlightButton(boolean b) {
        if (b == true) {
            flightButton.setDisable(false);
        } else {
            flightButton.setDisable(true);
        }
    }

    public void dealCards() {
        Iterator<JTEPlayer> it = gsm.getGameInProgress().getPlayers().iterator();
        TranslateTransition[] transitions = new TranslateTransition[(gsm.getGameInProgress().getNumPlayers() * 3)];
        ArrayList<ImageView> cardImages = new ArrayList<ImageView>();
        while (it.hasNext()) {
            JTEPlayer tmp = it.next();
            for (int i = 0; i < 3; i++) {
                cardImages.add(tmp.getCards().get(i).getFrontImage());
            }
        }

        Iterator<ImageView> it1 = cardImages.iterator();
        int j = 0;
        int delay = 0;
        while (it1.hasNext()) {
            for (int i = 0; i < 3; i++) {
                ImageView tmpImg = it1.next();
                transitions[j] = new TranslateTransition(Duration.millis(1000), tmpImg);
                transitions[j].setDelay(Duration.millis(delay));
                transitions[j].setFromX(330);
                transitions[j].setFromY(420);
                transitions[j].setToX(0);
                transitions[j].setToY(0);
                //yOff += 60;
                delay += 1000;
                j++;
            }
            yOff = 0;
        }
        Iterator<ImageView> nxtImg = cardImages.iterator();
        ImageView firstCard = nxtImg.next();
        firstCard.setVisible(true);
        Iterator<JTEPlayer> players = gsm.getGameInProgress().getPlayers().iterator();
        int firstPlayQuad = players.next().getCurrentCity().getQuad();
        for (int i = 0; i < transitions.length; i++) {
            if (i < transitions.length - 1) {
                ImageView nextImg = nxtImg.next();
                if (i == 0 || i == 1 || i == 3 || i == 4 || i == 6 || i == 7 || i == 9 || i == 10 || i == 12 || i == 13 || i == 15 || i == 16) {
                    transitions[i].setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent arg0) {
                            nextImg.setVisible(true);
                        }
                    });
                } else if (i == 2) {
                    transitions[i].setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent arg0) {
                            nextImg.setVisible(true);
                            if (gsm.getGameInProgress().getNumPlayers() == 1) {
                                changeScreen(1);

                            } else {
                                changeScreen(2);
                                changeQuadrant(players.next().getCurrentCity().getQuad());
                            }
                        }
                    });
                } else if (i == 5) {
                    transitions[i].setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent arg0) {
                            nextImg.setVisible(true);
                            if (gsm.getGameInProgress().getNumPlayers() == 2) {
                                changeScreen(1);
                            } else {
                                changeScreen(3);
                                changeQuadrant(players.next().getCurrentCity().getQuad());
                            }
                        }
                    });
                } else if (i == 8) {
                    transitions[i].setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent arg0) {
                            nextImg.setVisible(true);
                            if (gsm.getGameInProgress().getNumPlayers() == 3) {
                                changeScreen(1);
                            } else {
                                changeScreen(4);
                                changeQuadrant(players.next().getCurrentCity().getQuad());
                            }
                        }
                    });
                } else if (i == 11) {
                    transitions[i].setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent arg0) {
                            nextImg.setVisible(true);
                            if (gsm.getGameInProgress().getNumPlayers() == 4) {
                                changeScreen(1);
                            } else {
                                changeScreen(5);
                                changeQuadrant(players.next().getCurrentCity().getQuad());
                            }
                        }
                    });
                } else if (i == 14) {
                    transitions[i].setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent arg0) {
                            nextImg.setVisible(true);
                            if (gsm.getGameInProgress().getNumPlayers() == 5) {
                                changeScreen(1);
                            } else {
                                changeScreen(6);
                                changeQuadrant(players.next().getCurrentCity().getQuad());
                            }
                        }
                    });
                }
            } else {
                transitions[i].setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        changeScreen(1);
                        changeQuadrant(firstPlayQuad);
            clearLines();
            gsm.startTurn();
            drawLines(gsm.getGameInProgress().getCurrentPlayer());
                    }
                });
            }

        }

        for (int i = 0; i < transitions.length; i++) {
            transitions[i].play();
        }
    }
    
    public void showFlightError(){
            Stage newStage = new Stage();
            BorderPane comp = new BorderPane();
            comp.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058 );");
            Label nameField = new Label("You cannot make this flight!");
            Button rtnBtn = new Button("OK");
            rtnBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    newStage.close();
                }
            });

            comp.setTop(nameField);
            comp.setCenter(rtnBtn);
            Scene stageScene = new Scene(comp, 330, 120);
            stageScene.getStylesheets().add(JTEUI.class.getResource("JTESplash.css").toExternalForm());
            newStage.setScene(stageScene);
            newStage.show();
        }
    
}


class Delta {

    double x, y;
}
