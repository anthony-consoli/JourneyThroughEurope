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

    public FlightCity() {

    }

    public FlightCity(String name, double centerX, double centerY, int q) {
        super(centerX, centerY, 6.0, Paint.valueOf("#FF0000"));
        cityName = name;
        xPos = centerX;
        yPos = centerY;
        sector = q;

    }
    
    public int getSector()
    {
        return sector;
    }        
    
    public String getCityName()
    {
        return cityName;
    }        
}
