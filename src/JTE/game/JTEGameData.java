/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.game;

/**
 *
 * @author Anthony
 */

import java.util.HashMap;
import JTE.file.JTEFileLoader;
import java.io.File;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;


public class JTEGameData {
    
    //SCHEMA FILE NAME
    File schemaFile = new File("data/JTESchema.xsd");
    //CITY XML FILE
    File citiesFile = new File("data/JTE.xml");

    //FILE LOADER FOR CITIES
    private JTEFileLoader fileLoader;
    
    //ARRAY OF CITIES
    private City[]cities;
    
    //NUMBER OF PLAYERS
    private int numPlayers;
   
    //Players
    ConcurrentLinkedQueue<JTEPlayer> players = new ConcurrentLinkedQueue<JTEPlayer>();
   
    //CURRENT PLAYER
    JTEPlayer currentPlayer;
    
    //Card Decks
    HashMap<String, Stack<Card>> cards;
    Stack<Card> greenDeck = new Stack<Card>();
    Stack<Card> redDeck = new Stack<Card>();
    Stack<Card>yellowDeck = new Stack<Card>();
    
    public JTEGameData(int numPlayers)
    {
        //cities in the game
        fileLoader = new JTEFileLoader(schemaFile);
        cities = fileLoader.loadCities(citiesFile);
        
        for(int i=0; i<cities.length;i++)
        {
            Card tmpCard = new Card(cities[i], cities[i].getColor());
            if(tmpCard.getColor().equals("green"))
                greenDeck.push(tmpCard);
            else if(tmpCard.getColor().equals("red"))
                redDeck.push(tmpCard);
            else if(tmpCard.getColor().equals("yellow"))
                yellowDeck.push(tmpCard);
        }    
        
        for(int i=0;i<numPlayers;i++)
        {
            players.add(new JTEPlayer((i+1),false));
        }    
        currentPlayer = players.peek();
        
    }
    
    public JTEPlayer getCurrentPlayer()
    {
        return currentPlayer;
    }        
    
    public City[] getCities()
    {
        return cities;
    }        

    public JTEFileLoader getFileLoader()
    {
        return fileLoader;
    }        
    
}
