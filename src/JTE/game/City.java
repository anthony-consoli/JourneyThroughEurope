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
   
    
//DEFAULT CONSTRUCTOR DOES NOTHING
 public City(){
     
 }
    
 /**
  * Constructor takes in important information about the cities,
  * creates a circle on the game board that represents the city and
  * also retains important information about the city such as the name 
  * location, and list of neighbors.
  * @param name
  * @param col
  * @param centerX
  * @param centerY
  * @param q 
  */
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
 
 /**
  * This method is used to indicate if a particular city has an airport.
  */
 public void addAirport()
 {
     hasAirport = true;
 }        
 
 /**
  * This method checks whether or not a city has an airport.
  * @return 
  */
 public boolean hasAirport()
 {
     return hasAirport;
 }        
 
 /**
  * This method is used to add sea neighbors to a particular city.
  * @param sea 
  */
 public void addSeaNeighbors(ArrayList<City> sea)
 {
     seaNeighbors = sea;
 }        
 
  /**
  * This method is used to add land neighbors to a particular city.
  * @param land 
  */
 public void addLandNeighbors(ArrayList<City> land)
 {
     landNeighbors = land;
 }        
 
 /**
  * This method returns an ArrayList of a particlar city's land neighbors
  * @return 
  */
 public ArrayList<City> getLandNeighbors()
 {
     return landNeighbors;
 }        
 
 /**
  * This method returns an ArrayList of a particular city's sea neighbors
  * @return 
  */
 public ArrayList<City> getSeaNeighbors()
 {
     return seaNeighbors;
 }        
 
 //GETS COLOR
 public String getColor()
 {
     return color;
 }        
 
 //GETS NAME
 public String getName()
 {
     return cityName;
 }       
 
 //GETS X POSITION OF CITY
 public double getX()
 {
     return xPos;
 }       
 
 //GETS Y POSITION OF CITY
 public double getY()
 {
     return yPos;
 }       
 
 //GETS QUADRANT THAT CITY IS IN
 public int getQuad()
 {
     return quad;
 }       
 
 /**
  * The toString method returns the city in string format.
  * @return 
  */
@Override   
 public String toString()
 {
     return cityName;
 }        
 
 
        
 
}
