/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.game;

import javafx.scene.image.Image;

/**
 *
 * @author Anthony
 */
public class Card {
    
    private City cardCity;
    private String cityName;
    private String cardColor; 
    private Image frontImage;
    private Image backImage;
    private boolean hasInstrutions;
    private String greenImgPath = "file:img/green/";
    private String redImgPath = "file:img/red/";
    private String yellowImgPath = "file:img/yellow/";
    
    public Card(City city ,String col)
    {
        cardColor = col;
        cardCity = city;
        cityName = city.getName();
        
        if(cardColor.equals("green"))
            frontImage = new Image(greenImgPath + cityName);
        else if(cardColor.equals("red"))
            frontImage = new Image(redImgPath + cityName);
        else if(cardColor.equals("yellow"))
            frontImage = new Image(yellowImgPath + cityName);
    }        
    
    public String getColor()
    {
        return cardColor;
    }   
    
    public City getCity()
    {
        return cardCity;
    }        
    
    public String getCityName()
    {
        return cityName;
    }        
    
    public Image getFrontImage()
    {
        return frontImage;
    }        
}
