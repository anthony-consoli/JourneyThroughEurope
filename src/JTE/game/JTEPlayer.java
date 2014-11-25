/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.game;

import java.util.ArrayList;
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
    ArrayList<Card> handOfCards = new ArrayList<Card>();
    int dicePoints;
    Image sprite;
    Image flag;
    ImageView playerPiece;
    ImageView flagPiece;
    boolean cpu;
    City currentCity;
    City homeCity;
    double currentX;
    double currentY;
    int playerNumber;
    boolean rollAgain;
    AnchorPane cardPane;
    int yOff = 0;
    int xOff = 0;
    double transX;
    double transY;
    boolean firstTurn;
    
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
        
        playerNumber = playNum;
        dicePoints = 0;
        rollAgain = false;
        handOfCards = hand;
        homeCity = hand.get(0).getCity();
        currentCity = null;
        currentX=0;
        currentY=0;
        playerPiece = new ImageView(sprite);
        flagPiece = new ImageView(flag);
        cardPane = new AnchorPane();
        double transX = 25;
        double transY = 25;
        
        for(int i=0;i<handOfCards.size();i++)
        {
            ImageView tmpCard = handOfCards.get(i).getFrontImage();
            cardPane.getChildren().add(tmpCard);
            tmpCard.setX(xOff);
            tmpCard.setY(yOff);
            yOff +=60;
         }            
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
    public void makeMove()
    {
        dicePoints--;
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
    public void removeCard(Card c)
    {
        handOfCards.remove(c);
    }        
}
