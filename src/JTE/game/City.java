/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.game;

import java.awt.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author Anthony
 */
public class City extends Circle {
 
    private double radius = 6.0;
    private String cityName;
    private double xPos;
    private double yPos;
    private int quad;
    private String color;
    
    
 public City(String name, String col, double centerX, double centerY, int q)
 {
     super(centerX, centerY, 6.0, Paint.valueOf("#FF0000"));
     cityName = name;
     xPos = centerX;
     yPos = centerY;
     quad = q;
     color = col;
     
 }   
 
 public String getColor()
 {
     return color;
 }        
 
 public String getName()
 {
     return cityName;
 }       
 
 public double getX()
 {
     return xPos;
 }       
 
 public double getY()
 {
     return yPos;
 }       
 public int getQuad()
 {
     return quad;
 }       
 
@Override   
 public String toString()
 {
     return cityName;
 }        
 
 
        
 
}
