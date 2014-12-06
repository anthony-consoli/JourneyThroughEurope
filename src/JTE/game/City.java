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
public class City extends Circle {
 
    private double radius = 6.0;
    private String cityName;
    private double xPos;
    private double yPos;
    private int quad;
    private String color;
    private boolean hasAirport;
    private ArrayList<City> landNeighbors;
    private ArrayList<City> seaNeighbors;
    
 public City(){
     
 }
    
 public City(String name, String col, double centerX, double centerY, int q)
 {
     super(centerX, centerY, 6.0, Paint.valueOf("#FF0000"));
     cityName = name;
     hasAirport = false;
     xPos = centerX;
     yPos = centerY;
     quad = q;
     color = col;
     landNeighbors = new ArrayList<City>();
     seaNeighbors = new ArrayList<City>();
     
     
 }   
 
 public void addAirport()
 {
     hasAirport = true;
 }        
 
 public boolean hasAirport()
 {
     return hasAirport;
 }        
 
 public void addSeaNeighbors(ArrayList<City> sea)
 {
     seaNeighbors = sea;
 }        
 
 public void addLandNeighbors(ArrayList<City> land)
 {
     landNeighbors = land;
 }        
 
 public ArrayList<City> getLandNeighbors()
 {
     return landNeighbors;
 }        
 
 public ArrayList<City> getSeaNeighbors()
 {
     return seaNeighbors;
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
