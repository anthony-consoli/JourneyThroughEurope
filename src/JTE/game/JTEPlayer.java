/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.game;

import java.util.ArrayList;
import java.util.Stack;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    double currentX;
    double currentY;
    
    public JTEPlayer(int playNum, boolean cmp)
    {
        switch(playNum)
        {
            case 1:
                sprite = loadSprite("piece_black.png");
                flag = loadSprite("flag_black.png");
                break;
            case 2:
                sprite = loadSprite("piece_yellow.png");
                flag = loadSprite("flag_yellow.png");                
                break;
            case 3:
                sprite = loadSprite("piece_blue.png");
                flag = loadSprite("flag_blue.png");
                break;
            case 4:
                sprite = loadSprite("piece_red.png");
                flag = loadSprite("flag_red.png");
                break;
            case 5:
                sprite = loadSprite("piece_green.png");
                flag = loadSprite("flag_green.png");
                break;
            case 6:
                sprite = loadSprite("piece_white.png");
                flag = loadSprite("flag_white.png");
                break;
        }    
        
        currentCity = null;
        currentX=0;
        currentY=0;
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
}
