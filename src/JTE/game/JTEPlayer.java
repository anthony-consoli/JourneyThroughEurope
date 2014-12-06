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
        
        loadedPlayer = false;
        playerNumber = playNum;
        dicePoints = 0;
        rollAgain = false;
        beginTurn = false;
        handOfCards = hand;
        homeCity = hand.get(0).getCity();
        currentCity = hand.get(0).getCity();
        currentX=0;
        currentY=0;
        playerPiece = new ImageView(sprite);
        flagPiece = new ImageView(flag);
        cardPane = new AnchorPane();
        cpu = cmp;
        
        for(int i=0;i<handOfCards.size();i++)
        {
            ImageView tmpCard = handOfCards.get(i).getFrontImage();
            cardPane.getChildren().add(tmpCard);
            tmpCard.setX(xOff);
            tmpCard.setY(yOff);
            yOff +=60;
         }            
    }
    
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
    
    public void roll()
    {
        dicePoints = (int)(Math.random()*6) + 1;
    }        
    
    public void setDicePoints(int i)
    {
        dicePoints = i;
    }        
    
    public int getPlayNum()
    {
        return playerNumber;
    }        
    
    public int getDicePoints()
    {
        return dicePoints;
    }        
    public void makeMove(int i)
    {
        dicePoints = dicePoints - i;
    }        
    
    public Image loadSprite(String imageName) 
    {
        Image img = new Image(ImgPath + imageName);
        return img;
    }
    
    public void setCurrentCity(City c)
    {
        currentCity = c;
        currentX = c.getX();
        currentY = c.getY();
    }        
    
    public City getCurrentCity()
    {
        return currentCity;
    }        
    
    public City getHomeCity()
    {
        return homeCity;
    }        
    
    public double getCurrentX()
    {
        return currentX;
    }        
    
    public double getCurrentY()
    {
        return currentY;
    }        
 
    public Image getSprite()
    {
        return sprite;
    }        
    
    public Image getFlag()
    {
        return flag;
    }      
    
    public ArrayList<Card> getCards()
    {
        return handOfCards;
    }        
    
    public void addHandOfCards(ArrayList<Card> hand)
    {
        handOfCards = hand;
    }        
    
    public ImageView getSpritePiece()
    {
        return playerPiece;
    }        
    
    public ImageView getFlagPiece()
    {
        return flagPiece;
    } 
    
    public void setTransCoord(double x, double y)
    {
        transX = x;
        transY = y;
    }        
    public double getTransX()
    {
        return transX;
        
    }   
    public double getTransY()
    {
        return transY;
    } 
    
    public boolean isCpu()
    {
        return cpu;
    }        
    
    public void setTrip(ConcurrentLinkedQueue<City> trip)
    {
        cpuTrip = trip;
    }        
    
    public void removeCard(Card c)
    {
        handOfCards.remove(c);
    }        
    
    public  ConcurrentLinkedQueue<City> getCpuTrip()
    {
        return cpuTrip;
    }        
    public boolean isLoaded()
    {
        return loadedPlayer;
    }        
    
    public void setLoaded(boolean b)
    {
        loadedPlayer = b;
    }        
}

