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

import JTE.ui.JTEUI.JTEUIState;

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
    
    public void respondToNewGameRequest()
    {
        //ALOT OF DIFFERENT THINGS TO DO TO ACTUALLY SET UP NEW GAME
        //FOR HW5 I WILL JUST BE CHANGING THE WORKSPACE TO THE GAMEPLAY
        //SCREEN
        ui.changeWorkspace(JTEUIState.PLAY_GAME_STATE);
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
    
    public void respondToGameOver()
    {
        
    }   
    
    public void respondToBackRequest()
    {
        if(!ui.getGSM().isGameInProgess())
            ui.initSplashScreen();
            
    }
    
    public void respondToQuadrantRequest()
    {
        
    }        
}