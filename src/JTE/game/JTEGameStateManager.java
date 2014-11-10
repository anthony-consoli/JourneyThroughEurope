/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.game;

/**
 *
 * @author Anthony
 */

import JTE.ui.JTEUI;

public class JTEGameStateManager {
    
    
    JTEUI ui;
    private boolean isGameOn = false;
    
    public JTEGameStateManager(JTEUI initUI)
    {
        ui = initUI;
    }   
    
    public void makeNewGame()
    {
        
    }   
    
    public void loadPreviousGame()
    {
        
    }
    
    public void saveCurrentGame()
    {
        
    }
    
    public void isMoveValid()
    {
        
    }   
    
    public void getGameInProgress()
    {
        
    }
    
    public boolean isGameInProgess()
    {
        return isGameOn;
    }
    
    public void setGameOn()
    {
        isGameOn = true;
    }        
}
