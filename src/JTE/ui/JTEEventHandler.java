/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.ui;

/**
 *
 * @author Anthony
 */
import JTE.game.Card;
import JTE.game.City;
import JTE.game.JTEGameStateManager;
import JTE.game.JTEPlayer;
import JTE.ui.JTEUI.JTEUIState;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class JTEEventHandler {

    JTEUI ui;

    ArrayList<int[]> flightDir;

    public JTEEventHandler(JTEUI initUI) {
        ui = initUI;
        flightDir = new ArrayList<int[]>();
        flightDir.add(new int[]{2, 4, 5});
        flightDir.add(new int[]{3, 6, 1});
        flightDir.add(new int[]{2, 6, 4});
        flightDir.add(new int[]{3, 5, 1});
        flightDir.add(new int[]{4, 6, 1});
        flightDir.add(new int[]{3, 2, 5});
    }

    public void respondToStartRequest() {

    }

    public void respondToSwitchScreenRequest(JTEUI.JTEUIState uiState) {
        ui.changeWorkspace(uiState);
    }

    public void respondToNewGameRequest(int p, boolean[] cpu) {

        JTEGameStateManager gsm = ui.getGSM();
        gsm.makeNewGame(p, cpu);
        ui.initGameScreen();
    }

    public void respondToCityRequest(City c, JTEPlayer player, String str, double x, double y) {
        if (player.getCurrentCity().getLandNeighbors().contains(c)) {
            ui.updatePlayerPosition(c, player, str, "LAND", x, y, 1);
        } else if (player.getCurrentCity().getSeaNeighbors().contains(c)) {
            ui.updatePlayerPosition(c, player, str, "SEA", x, y, 1);
        }

    }

    public void respondToLoadGameRequest() {
        File file = new File("data/gameSave.txt");
        try {

            Scanner sc = new Scanner(file);
            ArrayList<ArrayList<String>> hands = new ArrayList<ArrayList<String>>();
            int numPlayers = Integer.parseInt(sc.next());
            int currentPlayer = Integer.parseInt(sc.next());
            int currentDice = Integer.parseInt(sc.next());
            String[] currentCities = new String[numPlayers];
            boolean[] cpu = new boolean[numPlayers];

            for (int i = 0; i < numPlayers; i++) {
                String tmpCpu = sc.next();
                if (tmpCpu.equals("HUMAN")) {
                    cpu[i] = false;
                } else {
                    cpu[i] = true;
                }
                String currCity = sc.next();
                currentCities[i] = currCity;

                int numCards = Integer.parseInt(sc.next());
                ArrayList<String> hand = new ArrayList<String>();
                for (int k = 0; k < numCards; k++) {
                    hand.add(sc.next());
                }
                hands.add(hand);
            }
            sc.close();
            JTEGameStateManager gsm = ui.getGSM();
            gsm.loadPreviousGame(numPlayers, currentPlayer, currentDice, cpu, currentCities, hands);
            ui.initGameScreen();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void respondToSaveGameRequest(ConcurrentLinkedQueue<JTEPlayer> p) 
    {
        ArrayList<ArrayList<String>> hands = new ArrayList<ArrayList<String>>();
        ConcurrentLinkedQueue<JTEPlayer> players = p;
        int numPlayers = p.size();
        int currentNum = p.peek().getPlayNum();
        int currentDice = p.peek().getDicePoints();
        while(p.peek().getPlayNum() != 1)
        {
            players.offer(players.poll());
        }    
        String[] typePlayer = new String[numPlayers];
        String[] currentCities = new String[numPlayers];
        int[] numCards = new int[numPlayers];      
        Iterator<JTEPlayer> it = p.iterator();
        int iterator = 0;
        while(it.hasNext())
        {
            JTEPlayer tmp = it.next();
            int numHand = 0;
            ArrayList<String> hand = new ArrayList<String>();
            ArrayList<Card> tmpCards = tmp.getCards();
            for(Card c: tmpCards)
            {
                hand.add(c.getCityName());
                numHand++;
            }    
            if(tmp.isCpu())
            {
                typePlayer[iterator] = "CPU";
            }    
            else if(!tmp.isCpu())
            {
                typePlayer[iterator] = "HUMAN";
            }    
            hands.add(hand);
            numCards[iterator] = numHand;
            currentCities[iterator] = tmp.getCurrentCity().getName();
            iterator++;
            
        }
        ui.gsm.saveCurrentGame(numPlayers, currentNum, currentDice, typePlayer, currentCities, numCards, hands);
        
        //DISPLAY THAT THE GAME HAS BEEN SAVED
            Stage newStage = new Stage();
            BorderPane comp = new BorderPane();
            comp.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058 );");
            Label nameField = new Label("       Game Saved!");
            Button rtnBtn = new Button("OK");
            rtnBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    newStage.close();
                }
            });

            comp.setTop(nameField);
            comp.setCenter(rtnBtn);
            Scene stageScene = new Scene(comp, 250, 100);
            stageScene.getStylesheets().add(JTEUI.class.getResource("JTESplash.css").toExternalForm());
            stageScene.setFill(Paint.valueOf("#8C92AC"));
            newStage.setScene(stageScene);
            newStage.show();
    }

    public void respondToExitRequest() {
        System.exit(0);
    }

    public void respondToFlightScreenRequest() {
        ui.changeQuadrant(5);
    }

    public void respondToFlightRequest(City c, JTEPlayer player, int currentSec, int destSec, String str, double x, double y) {
        int[] flights = flightDir.get(currentSec - 1);
        if (currentSec == destSec && player.getDicePoints() > 1) {
            ui.updatePlayerPosition(c, player, str, "FLY", x, y, 2);
        } 
        else if (destSec == flights[0] && player.getDicePoints() >= 4 || destSec == flights[1] && player.getDicePoints() >= 4 || destSec == flights[2] && player.getDicePoints() >= 4) {
            ui.updatePlayerPosition(c, player, str, "FLY", x, y, 4);
        } else {
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

    public void respondToGameOver(JTEPlayer p) {
        ui.getGSM().setGameState(JTEGameStateManager.JTEGameState.GAME_OVER);
        Stage newStage = new Stage();
        VBox comp = new VBox();
        comp.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058 );");
        Label nameField = new Label("       Player " + p.getPlayNum() + " wins!");
        Button rtnBtn = new Button("Return To Splash Screen");
        rtnBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ui.getGSM().setGameState(JTEGameStateManager.JTEGameState.GAME_NOT_STARTED);
                ui.initSplashScreen();
                newStage.close();
            }
        });
        comp.getChildren().add(nameField);
        comp.getChildren().add(rtnBtn);
        Scene stageScene = new Scene(comp, 300, 100);
        stageScene.getStylesheets().add(JTEUI.class.getResource("JTESplash.css").toExternalForm());
        newStage.setScene(stageScene);
        newStage.show();
    }

    public void respondToBackRequest() {
        if (!ui.getGSM().isGameInProgess()) {
            ui.changeWorkspace(JTEUIState.SPLASH_SCREEN_STATE);
        } else {
            ui.changeWorkspace(JTEUIState.PLAY_GAME_STATE);
        }
    }

    public void respondToHistoryRequest() {

    }

    public void respondToQuadrantRequest(int quadNum) {
        ui.changeQuadrant(quadNum);

    }
}
