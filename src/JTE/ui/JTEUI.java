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
import JTE.game.JTEPlayer;
import java.io.File;
import javafx.animation.TranslateTransition;
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
        VIEW_FLIGHT_STATE
    }

    public enum JTEQuadState {

        QUAD_1, QUAD_2, QUAD_3, QUAD_4;
    }

    public enum JTEPlayerState {

        PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4, PLAYER_5, PLAYER_6;
    }

    // mainStage
    private Stage primaryStage;

    // Image path
    private String ImgPath = "file:img/";

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
    private AnchorPane gameBoardPane;
    private StackPane gamePlayPane;
    private SplitPane gameSplitPane;
    private FlowPane gameFlowPane;
    private ToolBar gameToolBar;
    private Label gamePlayerLabel;
    private AnchorPane gameCardPane;
    private Pane rightSidePane;
    Image q1Img = loadImage("gameplay_AC14.jpg");
    Image q2Img = loadImage("gameplay_DF14.jpg");
    Image q3Img = loadImage("gameplay_AC58.jpg");
    Image q4Img = loadImage("gameplay_DF58.jpg");
    private ImageView gameBoardImg;
    private VBox rightSidePanel;
    private Label playerTurnLabel;
    private Image dieImg;
    private ImageView dieImage;

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

    Image firstCard = loadImage("red/ABERDEEN.jpg");
    Image secondCard = loadImage("green/BASEL.jpg");
    Image thirdCard = loadImage("yellow/ANCONA.jpg");
    double yOff = 60;
    double xOff = 8;

    ImageView p2 = new ImageView(firstCard);
    ImageView p3 = new ImageView(secondCard);
    ImageView p4 = new ImageView(thirdCard);

    Thread one;

    private City[] cities;

    JTEPlayer player1;

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

        for (int i = 0; i < boxes.length; i++) {
            boxes[i].setStyle("-fx-border-color: black;"
                    + "-fx-border-style: solid;");
        }

        //NUMBER OF PLAYERS TO BE SELECTED
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "1", "2", "3", "4", "5", "6"));
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
                System.out.println(cb.getSelectionModel().selectedIndexProperty().getValue() + 1);
                eventHandler.respondToNewGameRequest(cb.getSelectionModel().selectedIndexProperty().getValue() + 1);

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

        gamePlayPane = new StackPane();
        gameSplitPane = new SplitPane();
        gameFlowPane = new FlowPane();
        gameToolBar = new ToolBar();
        gamePlayerLabel = new Label("Player 1                 ");
        gameCardPane = new AnchorPane();
        rightSidePane = new Pane();
        gameBoardPane = new AnchorPane();
        gameBoardImg = new ImageView();
        gameBoardImg.setImage(q1Img);
        gameBoardImg.setSmooth(true);
        rightSidePanel = new VBox();
        playerTurnLabel = new Label("Player 1 Turn");
        dieImg = loadImage("die_5.jpg");
        dieImage = new ImageView();
        dieImage.setImage(dieImg);

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
        Button flightButton = new Button("Flight Plan");
        flightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_ABOUT_STATE);
            }
        });

        //CREATE ABOUT BUTTON AND HANDLER
        Button saveButton = new Button("Save");
        aboutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_ABOUT_STATE);
            }
        });

        //CREATE VBOX FOR BUTTONS
        VBox buttonBox = new VBox();
        //ADD BUTTONS TO VBOX
        buttonBox.getChildren().add(flightButton);
        buttonBox.getChildren().add(historyButton);
        buttonBox.getChildren().add(aboutButton);
        buttonBox.getChildren().add(saveButton);

        //TESTING LABEL TO DISPLAY CITY CLICKED
        cityLabel = new Label("City");

        //ADD COMPONENTS TO RIGHT SIDE PANEL
        rightSidePanel.getChildren().addAll(playerTurnLabel, dieImage, quadControl, buttonBox, cityLabel);

        //THIS CODE WILL HAVE TO CHANGE TODO.....
        gameBoardPane.getChildren().add(gameBoardImg);

        //LOAD ALL CITIES INTO BOARD
        cities = gsm.getGameInProgress().getCities();
        for (int i = 0; i < cities.length; i++) {
            gameBoardPane.getChildren().add(cities[i]);
            //THIS WILL SET THE CIRCULAR CITY OBJECTS TRANSPARENT
            cities[i].setOpacity(0);
        }

        for (int i = 1; i < cities.length + 1; i++) {
            String tmpStr = gameBoardPane.getChildren().get(i).toString();
            City tmp = (City) gameBoardPane.getChildren().get(i);
            tmp.setOnMouseClicked(mouseEvent -> {
                eventHandler.respondToCityRequest(tmp, player1, tmpStr, tmp.getX(), tmp.getY());

            });
        }

        //TESTING
        initPlayerSprites();
        changeQuadrant(player1.getCurrentCity().getQuad());

        rightSidePane.getChildren().addAll(gameBoardPane);

        gameToolBar.getItems().add(gamePlayerLabel);

        gameFlowPane.getChildren().addAll(gameToolBar, gameCardPane);

        gameSplitPane.getItems().addAll(gameFlowPane, rightSidePane, rightSidePanel);
        gameSplitPane.setDividerPositions(0.2f, 0.8f, 0.9f);

        gamePlayPane.getChildren().add(gameSplitPane);

        gsm.setGameOn();
        mainPane.setCenter(gamePlayPane);

    }

    public void initPlayerSprites() {

        player1 = new JTEPlayer(1, false);
        p1.setImage(player1.getSprite());
        p2.setImage(player1.getFlag());
        gameBoardPane.getChildren().add(p2);
        player1.setCurrentCity(cities[17]);
        gameBoardPane.getChildren().add(p1);
        p1.setX(player1.getCurrentX() - offsetX);
        p1.setY(player1.getCurrentY() - offsetY);
        p2.setX(player1.getCurrentX() - offsetX - 10);
        p2.setY(player1.getCurrentY() - offsetY - 10);
        xPos = p1.getX();
        yPos = p1.getY();
        drawLines();

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

    public JTEGameStateManager getGSM() {
        return gsm;
    }

    public void changeWorkspace(JTEUIState uiScreen) {
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

    public HBox createSetupSelection(Image imageName) {

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

    public Image loadImage(String imageName) {
        Image img = new Image(ImgPath + imageName);
        return img;
    }

    public void changeQuadrant(int quadNum) {

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
        }
    }

    public void updatePlayerPosition(City c, JTEPlayer player, String tempStr, double x, double y) {
        clearLines();
        cityLabel.setText(tempStr);
        double xDest = x - offsetX;
        double yDest = y - offsetY;
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(1000), p1);
        translateTransition1.setFromX(player.getCurrentX() - offsetX - 40);
        translateTransition1.setFromY(player.getCurrentY() - offsetY - 190);
        translateTransition1.setToX(x - offsetX - 45);
        translateTransition1.setToY(y - offsetY - 195);
        clearLines();
        translateTransition1.play();
        player.setCurrentCity(c);
        clearLines();
        drawLines();
        updatePos(xDest, yDest);

    }

    public void updatePos(double x, double y) {
        xPos = x;
        yPos = y;
    }

    public void drawLines() {
        double x1 = player1.getCurrentCity().getX();
        double y1 = player1.getCurrentCity().getY();
        for (int i = 0; i < player1.getCurrentCity().getLandNeighbors().size(); i++) {
            if (player1.getCurrentCity().getLandNeighbors().get(i).getQuad() == player1.getCurrentCity().getQuad()) {
                double x2 = player1.getCurrentCity().getLandNeighbors().get(i).getX();
                double y2 = player1.getCurrentCity().getLandNeighbors().get(i).getY();

                Line line1 = new Line(x1, y1, x2, y2);
                line1.setStrokeWidth(5);
                line1.setStroke(Color.RED);
                line1.setMouseTransparent(true);
                gameBoardPane.getChildren().add(line1);
            }
        }
        for (int i = 0; i < player1.getCurrentCity().getSeaNeighbors().size(); i++) {
            if (player1.getCurrentCity().getSeaNeighbors().get(i).getQuad() == player1.getCurrentCity().getQuad()) {
                double x2 = player1.getCurrentCity().getSeaNeighbors().get(i).getX();
                double y2 = player1.getCurrentCity().getSeaNeighbors().get(i).getY();

                Line line2 = new Line(x1, y1, x2, y2);
                line2.setStrokeWidth(5);
                line2.setStroke(Color.RED);
                line2.setMouseTransparent(true);
                gameBoardPane.getChildren().add(line2);
            }
        }

    }

    public void clearLines() {
        for (int i = 0; i < gameBoardPane.getChildren().size(); i++) {
            if (gameBoardPane.getChildren().get(i).getClass() == Line.class) {
                gameBoardPane.getChildren().remove(i);
            }
        }

    }
}
