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

import JTE.file.JTEFileLoader;
import JTE.ui.JTEUI;

public class JTEGameStateManager {
    
    public enum JTEGameState{
        
        GAME_NOT_STARTED, GAME_IN_PROGRESS, GAME_OVER
    }
    
    //STORES THE CURRENT STATE OF THIS GAME
    private JTEGameState currentGameState;
    
    //WHEN THE STATE OF THE GAME CHANGES IT WILL NEED TO BE 
    //REFLECTED IN THE USER INTERFACE
    private JTEUI ui;
    
    //THIS IS THE GAME CURRENTLY BEING PLAYED
    private JTEGameData gameInProgress;
    
    private JTEFileLoader fileLoader;
    
    private boolean isGameOn = false;
    
    public JTEGameStateManager(JTEUI initUI)
    {
        ui = initUI;
        
        //WE HAVE NOT STARTED A GAME YET
        currentGameState = JTEGameState.GAME_NOT_STARTED;
        
        //JTE HAS NOT BEEN STARTED YET
        gameInProgress = null;
    }   
    
    //ACCESSOR METHODS
    /**
     * Acessor method for getting the game currently being played.
     * 
     * @return The game currently being played
     */
    public JTEGameData getGameInProgress() {
        return gameInProgress;
    }
    
    public JTEFileLoader getFileLoader()
    {
        return fileLoader;
    }        
    
     
    /**
     * Accessor method for testing to see if the current game is over.
     *
     * @return true if the game in progress has completed, false otherwise.
     */
    public boolean isGameOver() {
        return currentGameState == JTEGameState.GAME_OVER;
    }
    
     /**
     * Accessor method for testing to see if the current game is in progress.
     *
     * @return true if a game is in progress, false otherwise.
     */
    public boolean isGameInProgress() {
        return currentGameState == JTEGameState.GAME_IN_PROGRESS;
    }

    
    public void processMove()
    {
        
    }   
    
    /**
     * This method receives the number of players in the game and uses
     * that number to create a new game effectively starting it.
     */
    
    public void makeNewGame(int numPlayers)
    {
        //MAKE A GAME WITH THE APPROPRIATE NUMBER OF PLAYERS
        gameInProgress = new JTEGameData(numPlayers);
        
        
        //THE GAME HAS OFFICALLY STARTED
        currentGameState = JTEGameState.GAME_IN_PROGRESS;
        
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
    
    
    public boolean isGameInProgess()
    {
        return isGameOn;
    }
    
    public void setGameOn()
    {
        isGameOn = true;
    }   
    

}
