/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Anthony
 */
public class Card {
    
    private City cardCity;
    private String cityName;
    private String cardColor; 
    private ImageView frontImage;
    private ImageView backImage;
    private boolean hasInstrutions;
    private String greenImgPath = "file:img/green/";
    private String redImgPath = "file:img/red/";
    private String yellowImgPath = "file:img/yellow/";
    
    public Card(City city ,String col)
    {
        cardColor = col;
        cardCity = city;
        cityName = city.getName();
        
        switch (cardColor) {
            case "green":
                frontImage = new ImageView(new Image(greenImgPath + cityName + ".jpg"));
                break;
            case "red":
                frontImage = new ImageView(new Image(redImgPath + cityName + ".jpg"));
                break;
            case "yellow":
                frontImage = new ImageView(new Image(yellowImgPath + cityName + ".jpg"));
                break;
        }
    }        
    
    /**
     * This method returns either Green, Yellow, or Red depending on the card.
     * @return cardColor
     */
    public String getColor()
    {
        return cardColor;
    }   
    
    /**
     * This method returns the city on a specific card.
     * @return cardCity
     */
    public City getCity()
    {
        return cardCity;
    }        
    
    /**
     * This method returns the String representation of the City Name
     * @return cityName
     */
    public String getCityName()
    {
        return cityName;
    }        
    
    /**
     * This method gets and returns the front image of a card
     * @return frontImage - the imageView of a specific card.
     */
    public ImageView getFrontImage()
    {
        return frontImage;
    }        
}
