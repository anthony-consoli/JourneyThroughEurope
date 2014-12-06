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

import JTE.game.City;
import JTE.game.JTEGameStateManager;
import JTE.game.JTEPlayer;
import JTE.ui.JTEUI.JTEUIState;
import java.util.ArrayList;
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
    
    public JTEEventHandler(JTEUI initUI)
    {
        ui = initUI;
        flightDir = new ArrayList<int[]>();
        flightDir.add(new int[]{2,4,5});
        flightDir.add(new int[]{3,6,1});
        flightDir.add(new int[]{2,6,4});
        flightDir.add(new int[]{3,5,1});
        flightDir.add(new int[]{4,6,1});
        flightDir.add(new int[]{3,2,5});
    }
    
    public void respondToStartRequest()
    {
        
    }
    

    
    public void respondToSwitchScreenRequest(JTEUI.JTEUIState uiState)
    {
        ui.changeWorkspace(uiState);
    }   
    
    public void respondToNewGameRequest(int p, boolean[] cpu)
    {

        JTEGameStateManager gsm = ui.getGSM();
        gsm.makeNewGame(p, cpu);
        ui.initGameScreen();
    } 
    
    public void respondToCityRequest(City c, JTEPlayer player, String str, double x, double y)
    {
        if(player.getCurrentCity().getLandNeighbors().contains(c)) 
        {
            ui.updatePlayerPosition(c, player,str, "LAND", x, y,1);
        }    
        else if(player.getCurrentCity().getSeaNeighbors().contains(c))
        {
            ui.updatePlayerPosition(c, player,str, "SEA", x, y,1);
        }    
        
    }        
    
    public void respondToLoadGameRequest()
    {
        
    }   
    
    public void respondToSaveGameRequest()
    {
        
    }   
    
    public void respondToExitRequest()
    {
        System.exit(0);
    }   
    
    public void respondToFlightScreenRequest()
    {
        ui.changeQuadrant(5);
    }        
    
    public void respondToFlightRequest(City c, JTEPlayer player, int currentSec, int destSec, String str, double x, double y)
    {
        int[] flights = flightDir.get(currentSec - 1);
        if(currentSec == destSec && player.getDicePoints() > 1)
        {
            ui.updatePlayerPosition(c, player, str, "FLY", x, y,2);
        }
        else if(destSec == flights[0] || destSec == flights[1] || destSec == flights[2] && player.getDicePoints() > 3)
        {
            ui.updatePlayerPosition(c, player, str, "FLY", x, y, 4);
        }    
        else
        {
            Stage newStage = new Stage();
            BorderPane comp = new BorderPane();
            Label nameField = new Label("You cannot make this flight!");
            Button rtnBtn = new Button("OK");
            rtnBtn.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent e)
                {
                    newStage.close();
                }        
            });

            comp.setTop(nameField);
            comp.setCenter(rtnBtn);
            Scene stageScene = new Scene(comp, 400, 400);
            stageScene.getStylesheets().add(JTEUI.class.getResource("JTESplash.css").toExternalForm());
            stageScene.setFill(Paint.valueOf("#8C92AC"));
            newStage.setScene(stageScene);
            newStage.show();            
        }    
        
        
    }        
    
    public void respondToGameOver(JTEPlayer p)
    {
        ui.getGSM().setGameState(JTEGameStateManager.JTEGameState.GAME_OVER);
        Stage newStage = new Stage();
        VBox comp = new VBox();
        Label nameField = new Label("       Player " + p.getPlayNum() + " wins!");
        Button rtnBtn = new Button("Return To Splash Screen");
        rtnBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e)
            {
                ui.initSplashScreen();
                ui.getGSM().setGameState(JTEGameStateManager.JTEGameState.GAME_NOT_STARTED);
                newStage.close();
            }        
        });
        comp.getChildren().add(nameField);
        comp.getChildren().add(rtnBtn);
        Scene stageScene = new Scene(comp, 300, 100);
        stageScene.getStylesheets().add(JTEUI.class.getResource("JTESplash.css").toExternalForm());
        stageScene.setFill(Paint.valueOf("#8C92AC"));
        newStage.setScene(stageScene);
        newStage.show();
    }
    
    
    public void respondToBackRequest()
    {
        if(!ui.getGSM().isGameInProgess())
            ui.changeWorkspace(JTEUIState.SPLASH_SCREEN_STATE);
        else
            ui.changeWorkspace(JTEUIState.PLAY_GAME_STATE);
    }
    
    public void respondToHistoryRequest()
    {
        
    }        
    
    public void respondToQuadrantRequest(int quadNum)
    {
        ui.changeQuadrant(quadNum);
            
    }        
}
