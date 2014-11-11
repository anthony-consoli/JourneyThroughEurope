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
    
 public City(String name, double centerX, double centerY)
 {
     super(centerX, centerY, 6.0, Paint.valueOf("#FF0000"));
     String cityName = name;
     
 }   
@Override   
 public String toString()
 {
     return cityName;
 }        
 
        
 
}
