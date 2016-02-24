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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class JTEGameStateManager {

    //GAME STATES 
    public enum JTEGameState {

        GAME_NOT_STARTED, GAME_IN_PROGRESS, GAME_OVER
    }

    //THESE REPRESNT THE TURNS OF ALL PLAYERS
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

    private ArrayList<String> gameHistory;
    
    public JTEGameStateManager(JTEUI initUI) {
        ui = initUI;

        //WE HAVE NOT STARTED A GAME YET
        currentGameState = JTEGameState.GAME_NOT_STARTED;

        //JTE HAS NOT BEEN STARTED YET
        gameInProgress = null;
        
        gameHistory = new ArrayList();
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
    //public boolean isGameInProgress() {
      //  return currentGameState == JTEGameState.GAME_IN_PROGRESS;
   // }

    /**
     * This method starts the turn for both human and CPU players.
     * After updating important values, the method checks whether or not the 
     * current player is CPU or human and reacts accordingly.
     */
    public void startTurn() {
        ui.clearLines();
        gameInProgress.getCurrentPlayer().setBeginTurn(true);
        gameInProgress.getCurrentPlayer().setPreviousCity(null);
        gameInProgress.getCurrentPlayer().getSpritePiece().setVisible(true);
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
            gameInProgress.getCurrentPlayer().setCanFly(true);
        }
        ui.updateDie(gameInProgress.getCurrentPlayer().getDicePoints());
        ui.setCurrentPlayer(gameInProgress.getCurrentPlayer());
        if (gameInProgress.getCurrentPlayer().isCpu()) {
            cpuTurn();
        }

    }

    /**
     * This method carries out the remainder of a turn for a CPU player.
     */
    public void cpuTurn() {
        ui.clearLines();
        if (!gameInProgress.getCurrentPlayer().getCpuTrip().isEmpty()) {
            //System.out.println(gameInProgress.getCurrentPlayer().getCpuTrip().toString());
            City tmpCity = gameInProgress.getCurrentPlayer().getCpuTrip().poll();
            if (gameInProgress.getCurrentPlayer().getCurrentCity().getLandNeighbors().contains(tmpCity) || gameInProgress.getCurrentPlayer().getCurrentCity().getSeaNeighbors().contains(tmpCity) )
                ui.getEventHandler().respondToCityRequest(tmpCity, gameInProgress.getCurrentPlayer(), tmpCity.getName(), tmpCity.getX(), tmpCity.getY());
            
        }
    }
    
    /**
     * This method changes the turn to the next player in the queue. 
     */
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

    /**
     * This method will take in data for a txt save file as parameters.
     * This method will then take that data and set the data of the game in progress accordingly.
     * The method is used to load a previous game which was saved.
     * @param num
     * @param currNum
     * @param currentDice
     * @param cpu
     * @param currentCities
     * @param oldCards 
     */
    public void loadPreviousGame(int num, int currNum, int currentDice, boolean[] cpu, String[] currentCities, ArrayList<ArrayList<String>> oldCards) {
        
        //USE DATA FROM TXT FILE TO START A PREVIOUS GAME
        gameInProgress = new JTEGameData(num, currNum, currentDice, cpu, currentCities, oldCards);
        
        //THE GAME HAS RESUMED
        currentGameState = JTEGameState.GAME_IN_PROGRESS;

    }
    
    /**
     * This method takes in critical game data as parameters and write that data to an 
     * external txt file in a specific format that will be utilized as save game data.
     * @param numPlayers
     * @param currentNum
     * @param currentDice
     * @param typePlayer
     * @param currentCities
     * @param numCards
     * @param hands 
     */
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
        Iterator<String> strIt = gameHistory.iterator();
        while(strIt.hasNext())
            writer.println(strIt.next());
        writer.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }    
        
    }

    public void isMoveValid() {

    }

    /**
     * This method checks if the game is in progress.
     * @return - True if game is in progress. False otherwise.
     */
    public boolean isGameInProgess() {
        if(currentGameState == JTEGameState.GAME_IN_PROGRESS)
            return true;
        else
            return false;
    }

    /**
     * This method is used to set the game to on.
     */
    public void setGameOn() {
        isGameOn = true;
    }
    
    /**
     * Mutator method to set the game state.
     * @param state 
     */
    public void setGameState(JTEGameState state)
    {
        currentGameState = state;
    }        
    
    /**
     * This method adds the string representing the player move to the overall game history.
     * @param str 
     */
    public void addToHistory(String str)
    {
        gameHistory.add(str);
    }        
    
    /**
     * This method get the game history as an ArrayList of entries and returns it.
     * @return gameHistory - the arraylist of current game history.
     */
    public ArrayList<String>getGameHistory()
    {
        return gameHistory;
    }      
    
    /**
     * This method sets whether or not the cards are visible on the UI.
     */
    public void setCardsVisible()
    {
        Iterator<JTEPlayer> it = gameInProgress.getPlayers().iterator();
        while(it.hasNext())
        {
            JTEPlayer temp = it.next();
            int numCards = temp.getCards().size();
            for(Card c : temp.getCards())
            {
                c.getFrontImage().setVisible(true);
            }    
        }    
    }        
}
