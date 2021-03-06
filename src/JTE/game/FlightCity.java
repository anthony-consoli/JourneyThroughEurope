/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.game;

import java.util.ArrayList;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author Anthony
 */
public class FlightCity extends Circle {

    private double radius = 6.0;
    private String cityName;
    private double xPos;
    private double yPos;
    private int sector;
    private String color;

    
    /** 
     * Default constructor for FlightCity has no function
     */
    public FlightCity() {

    }
    /**
     * Constructor for FlightCity which takes in the name, indices, and sector value of 
     * cities with airports and constructs them.
     * @param name
     * @param centerX
     * @param centerY
     * @param q 
     */
    public FlightCity(String name, double centerX, double centerY, int q) {
        super(centerX, centerY, 6.0, Paint.valueOf("#FF0000"));
        cityName = name;
        xPos = centerX;
        yPos = centerY;
        sector = q;

    }
    
    /**
     * Getter method that returns the sector value of the city on the flight plan.
     * @return an integer value of the sector of the city.
     */
    public int getSector()
    {
        return sector;
    }        
    
    /**
     * Getter method that returns the name of a certain city.
     * @return the City name
     */
    public String getCityName()
    {
        return cityName;
    }        
}
