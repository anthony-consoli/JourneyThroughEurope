/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Anthony
 */
public class JTEPlayer {
    
    // Image path
    private String ImgPath = "file:img/";
    
    
    //HAND OF CARDS DEALT TO PLAYER
    private ArrayList<Card> handOfCards = new ArrayList<Card>();
    private ConcurrentLinkedQueue<City> cpuTrip;
    private int dicePoints;
    private Image sprite;
    private Image flag;
    private ImageView playerPiece;
    private ImageView flagPiece;
    private boolean cpu;
    private City currentCity;
    private City homeCity;
    private City previousCity;
    private double currentX;
    private double currentY;
    private int playerNumber;
    private boolean rollAgain;
    private AnchorPane cardPane;
    private int yOff = 0;
    private int xOff = 0;
    private double transX;
    private double transY;
    private boolean beginTurn;
    private boolean loadedPlayer;
    private boolean canFly;
    
    /**
     * Constructor for JTEPlayer
     * @param playNum - the number of the player determines their color and the order in which they will move.
     * @param hand - this represents the hand of cards held by the player.
     * @param cmp - this boolean is used to determine whether or not the player is a computer player or not
     */
    public JTEPlayer(int playNum, ArrayList<Card>hand, boolean cmp)
    {
        switch(playNum)
        {
            case 1:
                sprite = loadSprite("piece_black.png");
                flag = loadSprite("flag_black.png");
                playerNumber = 1;
                break;
            case 2:
                sprite = loadSprite("piece_yellow.png");
                flag = loadSprite("flag_yellow.png");       
                 playerNumber = 2;
                break;
            case 3:
                sprite = loadSprite("piece_blue.png");
                flag = loadSprite("flag_blue.png");
                 playerNumber = 3;
                break;
            case 4:
                sprite = loadSprite("piece_red.png");
                flag = loadSprite("flag_red.png");
                 playerNumber = 4;
                break;
            case 5:
                sprite = loadSprite("piece_green.png");
                flag = loadSprite("flag_green.png");
                 playerNumber = 5;
                break;
            case 6:
                sprite = loadSprite("piece_white.png");
                flag = loadSprite("flag_white.png");
                 playerNumber = 6;
                break;
        }    
        
        //Initialize the important starting values for the player.
        loadedPlayer = false;
        playerNumber = playNum;
        dicePoints = 0;
        rollAgain = false;
        beginTurn = false;
        handOfCards = hand;
        homeCity = hand.get(0).getCity();
        currentCity = hand.get(0).getCity();
        previousCity = hand.get(0).getCity();
        currentX=0;
        currentY=0;
        playerPiece = new ImageView(sprite);
        flagPiece = new ImageView(flag);
        cardPane = new AnchorPane();
        canFly = true;
        cpu = cmp;
        
        //Add the players hand of cards to the card pane in the UI
        for(int i=0;i<handOfCards.size();i++)
        {
            ImageView tmpCard = handOfCards.get(i).getFrontImage();
            tmpCard.setVisible(false);
            cardPane.getChildren().add(tmpCard);
            tmpCard.setX(xOff);
            tmpCard.setY(yOff);
            yOff +=60;
         }            
    }
    
    /**
     * Getter method to
     * @return boolean 
     */
    
    public boolean getBeginTurn()
    {
        return beginTurn;
    }   
    
    public void setBeginTurn(boolean b)
    {
        beginTurn = b;
    }        
    
    public AnchorPane getCardPane()
    {
        return cardPane;
    }        
    
    /**
     * This method simulates rolling a 6 sided dice which is used during the game.
     */
    public void roll()
    {
        dicePoints = (int)(Math.random()*6) + 1;
        if(dicePoints == 6)
            rollAgain = true;
        else
            rollAgain = false;
    }        
    
    
    /**
     * This method is used to set the dice points of the player.
     * @param i - the number of dice points you wish to set (dictated by the dice)
     */
    public void setDicePoints(int i)
    {
        dicePoints = i;
    }        
    
    /**
     * Accessor method to retrieve the current player number.
     * @return playerNumber 
     */
    public int getPlayNum()
    {
        return playerNumber;
    }        
    
    /**
     * Accessor method to retrieve the amount of dice points the current player has.
     * @return dicePoints
     */
    public int getDicePoints()
    {
        return dicePoints;
    }        
    
    /**
     * This method decrements dice points when moves on the game board are made.
     * @param i - number of dice points it takes to make a particular move.
     */
    public void makeMove(int i)
    {
        dicePoints = dicePoints - i;
    }        
    /**
     * Loads player sprite.
     * @param imageName
     * @return 
     */
    public Image loadSprite(String imageName) 
    {
        Image img = new Image(ImgPath + imageName);
        return img;
    }
    
    /**
     * Setter method to set the players current city.
     * @param c 
     */
    public void setCurrentCity(City c)
    {
        currentCity = c;
        currentX = c.getX();
        currentY = c.getY();
    }        
    
    /**
     * Getter method to get the players current city.
     * @return City - the players current city.
     */
    public City getCurrentCity()
    {
        return currentCity;
    }        
    
    /**
     * Getter method to retrieve the current players home city.
     * @return homeCity - the players home city.
     */
    public City getHomeCity()
    {
        return homeCity;
    }        
    
    /**
     * Returns the current X value of the player
     * @return currentX
     */
    public double getCurrentX()
    {
        return currentX;
    }        
    
    /**
     * Returns the current Y value of the player
     * @return currentY
     */
    public double getCurrentY()
    {
        return currentY;
    }        
 
    /**
     * Returns the sprite image of the player.
     * @return 
     */
    public Image getSprite()
    {
        return sprite;
    }        
    
    /**
     * Returns the home flag image for the player.
     * @return 
     */
    public Image getFlag()
    {
        return flag;
    }      
    
    /**
     * Returns the players hand of cards as an ArrayList of cards.
     * @return 
     */
    public ArrayList<Card> getCards()
    {
        return handOfCards;
    }        
    
    /**
     * Method used to add a hand of cards to a JTEplayer
     * @param hand - ArrayLIst of cards that represents the hand of cards.
     */
    public void addHandOfCards(ArrayList<Card> hand)
    {
        handOfCards = hand;
    }        
    
    /**
     * Returns the ImageView of the Sprite
     * @return 
     */
    public ImageView getSpritePiece()
    {
        return playerPiece;
    }        
    
    /**
     * Returns the ImageView of the players home flag.
     * @return 
     */
    public ImageView getFlagPiece()
    {
        return flagPiece;
    } 
    
    /**
     * Sets the trans. coordinate of the Player.
     * @param x
     * @param y 
     */
    public void setTransCoord(double x, double y)
    {
        transX = x;
        transY = y;
    }        
    
    /**
     * Gets the trans. X coordinate of the player
     * @return 
     */
    public double getTransX()
    {
        return transX;
        
    }   
    
    /**
     * Gets the trans. Y coordinate of the player
     * @return 
     */
    public double getTransY()
    {
        return transY;
    } 
    
    /**
     * Method to determine whether the player is a cpu player or not.
     * @return 
     */
    public boolean isCpu()
    {
        return cpu;
    }        
    
    /**
     * Sets the trip path for a cpu player.
     * @param trip 
     */
    public void setTrip(ConcurrentLinkedQueue<City> trip)
    {
        cpuTrip = trip;
    }        
    
    /**
     * This method removes a card from the players hand. 
     * This is done when the player arrives at that particular city.
     * @param c - card to be removed
     */
    public void removeCard(Card c)
    {
        handOfCards.remove(c);
    }        
    
    /**
     * This method returns a ConcurrentLinkedQueue containing the current trip of the cpu player.
     * @return 
     */
    public  ConcurrentLinkedQueue<City> getCpuTrip()
    {
        return cpuTrip;
    }        
    
    /**
     * Returns a boolean which represents if the player has been loaded from a previous game.
     * @return 
     */
    public boolean isLoaded()
    {
        return loadedPlayer;
    }        
    
    /**
     * This method sets the player to be loaded or not.
     * This depends on whether the current game has been saved and reloaded.
     * @param b 
     */
    public void setLoaded(boolean b)
    {
        loadedPlayer = b;
    }        
    
    /**
     * This method sets the roll again boolean to true if the player rolls 6 and should roll again.
     * @return 
     */
    public boolean getRollAgain()
    {
        return rollAgain;
    }        
    
    /**
     * This method returns the previous city that the player had visited.
     * @return 
     */
    public City getPreviousCity()
    {
        return previousCity;
    }        
    
    /**
     * This method sets the previous city of the current player.
     * @param c 
     */
    public void setPreviousCity(City c)
    {
        previousCity = c;
    }        
    
    /**
     * This method sets whether the player can fly of not.
     * @param b 
     */
    public void setCanFly(boolean b)
    {
        canFly =b;
    }        
    
    /**
     * This method returns the value which incates if the player can fly or not.
     * @return 
     */
    public boolean canFly()
    {
        return canFly;
    }        
}

