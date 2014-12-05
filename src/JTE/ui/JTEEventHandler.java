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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JTEEventHandler {
    
    
    
    JTEUI ui;
    
    public JTEEventHandler(JTEUI initUI)
    {
        ui = initUI;
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
        if(player.getCurrentCity().getLandNeighbors().contains(c) || player.getCurrentCity().getSeaNeighbors().contains(c))
        {
            ui.updatePlayerPosition(c, player,str, x, y);
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
    
    public void respondToFlightRequest()
    {
        ui.changeQuadrant(5);
    }        
    
    public void respondToGameOver(JTEPlayer p)
    {
        ui.getGSM().setGameState(JTEGameStateManager.JTEGameState.GAME_OVER);
        Stage newStage = new Stage();
        VBox comp = new VBox();
        Label nameField = new Label("Player " + p.getPlayNum() + " wins!");
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
        Scene stageScene = new Scene(comp, 200, 200);
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
