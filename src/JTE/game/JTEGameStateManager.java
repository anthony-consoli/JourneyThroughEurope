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
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class JTEGameStateManager {

    public enum JTEGameState {

        GAME_NOT_STARTED, GAME_IN_PROGRESS, GAME_OVER
    }

    public enum JTETurnState {

        PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4, PLAYER_5, PLAYER_6
    }

    //STORES THE CURRENT STATE OF THIS GAME
    private JTEGameState currentGameState;

    //STORES CURRENT PLAYER STATE
    private JTETurnState currentPlayerState;

    //WHEN THE STATE OF THE GAME CHANGES IT WILL NEED TO BE 
    //REFLECTED IN THE USER INTERFACE
    private JTEUI ui;

    //THIS IS THE GAME CURRENTLY BEING PLAYED
    private JTEGameData gameInProgress;

    private JTEFileLoader fileLoader;

    private boolean isGameOn = false;

    private JTEPlayer current;

    public JTEGameStateManager(JTEUI initUI) {
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

    public JTEFileLoader getFileLoader() {
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

    public void startTurn() {
        ui.clearLines();
        gameInProgress.getCurrentPlayer().setBeginTurn(true);
        if(gameInProgress.getCurrentPlayer().getCurrentCity().hasAirport())
            ui.setFlightButton(true);
        ui.changeQuadrant(gameInProgress.getCurrentPlayer().getCurrentCity().getQuad());
        if(gameInProgress.getCurrentPlayer().isLoaded())
        {  
            gameInProgress.getCurrentPlayer().setLoaded(false);
        }  
        else
        {    
            gameInProgress.getCurrentPlayer().roll();
        }
        System.out.println("Player: " + gameInProgress.getCurrentPlayer().getPlayNum() + " rolled a " + gameInProgress.getCurrentPlayer().getDicePoints());
        ui.updateDie(gameInProgress.getCurrentPlayer().getDicePoints());
        ui.setCurrentPlayer(gameInProgress.getCurrentPlayer());
        if (gameInProgress.getCurrentPlayer().isCpu()) {
            cpuTurn();
        }

    }

    public void cpuTurn() {
        ui.clearLines();
        if (!gameInProgress.getCurrentPlayer().getCpuTrip().isEmpty()) {
            //System.out.println(gameInProgress.getCurrentPlayer().getCpuTrip().toString());
            City tmpCity = gameInProgress.getCurrentPlayer().getCpuTrip().poll();
            ui.getEventHandler().respondToCityRequest(tmpCity, gameInProgress.getCurrentPlayer(), tmpCity.getName(), tmpCity.getX(), tmpCity.getY());
            
        }
    }

    public void changeTurn() {
        gameInProgress.getPlayers().offer(gameInProgress.getPlayers().remove());
        gameInProgress.setCurrentPlayer(gameInProgress.getPlayers().peek());
        ui.clearLines();
    }

    /**
     * This method receives the number of players in the game and uses that
     * number to create a new game effectively starting it.
     */
    public void makeNewGame(int numPlayers, boolean[] cpu) {
        //MAKE A GAME WITH THE APPROPRIATE NUMBER OF PLAYERS
        gameInProgress = new JTEGameData(numPlayers, cpu);

        //THE GAME HAS OFFICALLY STARTED
        currentGameState = JTEGameState.GAME_IN_PROGRESS;

        //START WITH PLAYER 1 TURN
       // currentPlayerState = JTETurnState.PLAYER_1;

    }

    public void loadPreviousGame(int num, int currNum, int currentDice, boolean[] cpu, String[] currentCities, ArrayList<ArrayList<String>> oldCards) {
        
        //USE DATA FROM TXT FILE TO START A PREVIOUS GAME
        gameInProgress = new JTEGameData(num, currNum, currentDice, cpu, currentCities, oldCards);
        
        //THE GAME HAS RESUMED
        currentGameState = JTEGameState.GAME_IN_PROGRESS;

    }

    public void saveCurrentGame(int numPlayers, int currentNum, int currentDice, String[] typePlayer, String[] currentCities, int[] numCards, ArrayList<ArrayList<String>> hands) 
    {
        try
        {
        PrintWriter writer = new PrintWriter("data/gameSave.txt", "UTF-8");
        writer.println(numPlayers);
        writer.println(currentNum);
        writer.println(currentDice);
        
        for(int i =0; i<numPlayers;i++)
        {
            String playerType = typePlayer[i];
            writer.println(playerType);
            String currentCity = currentCities[i];
            writer.println(currentCity);
            ArrayList<String> currentHand = hands.get(i);
            writer.println(currentHand.size());
            for(int k = 0; k<currentHand.size();k++)
            {
                writer.println(currentHand.get(k));
            }    
        }
        writer.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }    
        
    }

    public void isMoveValid() {

    }

    public boolean isGameInProgess() {
        if(currentGameState == JTEGameState.GAME_IN_PROGRESS)
            return true;
        else
            return false;
    }

    public void setGameOn() {
        isGameOn = true;
    }
    
    public void setGameState(JTEGameState state)
    {
        currentGameState = state;
    }        
}
