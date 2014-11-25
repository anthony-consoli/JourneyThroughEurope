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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JTEGameData {

    //SCHEMA FILE NAME
    File schemaFile = new File("data/JTESchema.xsd");
    //CITY XML FILE
    File citiesFile = new File("data/JTE.xml");

    //FILE LOADER FOR CITIES
    private JTEFileLoader fileLoader;

    //ARRAY OF CITIES
    private City[] cities;

    //HASHMAP OF CITIES FOR ADDING NEIGHBORS
    private HashMap<String, City> cityHash;

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
    Stack<Card> yellowDeck = new Stack<Card>();

    public JTEGameData(int num) {
        
        numPlayers = num;
        //cities in the game
        fileLoader = new JTEFileLoader(schemaFile);
        cities = fileLoader.loadCities(citiesFile);
        cityHash = new HashMap<String, City>();

        //CREATE THREE DECKS OF CARDS
        for (int i = 0; i < cities.length; i++) {
            Card tmpCard = new Card(cities[i], cities[i].getColor());
            if (tmpCard.getColor().equals("green")) {
                greenDeck.push(tmpCard);
            } else if (tmpCard.getColor().equals("red")) {
                redDeck.push(tmpCard);
            } else if (tmpCard.getColor().equals("yellow")) {
                yellowDeck.push(tmpCard);
            }
            //CREATE A HASH MAP OF CITIES USED TO EASILY SET UP NEIGHBORS
            cityHash.put(cities[i].getName(), cities[i]);
        }
        
        //SHUFFLE THE DECKS
         greenDeck = shuffleDeck(greenDeck);
         redDeck = shuffleDeck(redDeck);
         yellowDeck = shuffleDeck(yellowDeck);

        //SETTING UP THE LAND AND SEA NEIGHBORS FOR THE CITIES
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("data/cities.xml"));
            Node root = doc.getElementsByTagName("routes").item(0);
            NodeList cardlist = root.getChildNodes();
            for (int i = 0; i < cardlist.getLength(); i++) {
                ArrayList<City> landNeighbors = new ArrayList<City>();
                ArrayList<City> seaNeighbors = new ArrayList<City>();
                City tmpCity = new City();
                Node cardNode = cardlist.item(i);
                if (cardNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList cardAttrs = cardNode.getChildNodes();
                    // one card
                    for (int j = 0; j < cardAttrs.getLength(); j++) {
                        if (cardAttrs.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Node theNode = cardAttrs.item(j);
                            switch (theNode.getNodeName()) {
                                case "name":
                                    tmpCity = cityHash.get(theNode.getTextContent());
                                    break;
                                case "land":
                                    NodeList landList = theNode.getChildNodes();
                                    for (int k = 0; k < landList.getLength(); k++) {
                                        if (landList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                            landNeighbors.add(cityHash.get(landList.item(k).getTextContent()));
                                        }
                                    }
                                    break;
                                case "sea":
                                    NodeList seaList = theNode.getChildNodes();
                                    for (int k = 0; k < seaList.getLength(); k++) {
                                        if (seaList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                            seaNeighbors.add(cityHash.get(seaList.item(k).getTextContent()));
                                        }
                                    }
                                    break;
                            }

                        }
                    }
                    tmpCity.addLandNeighbors(landNeighbors);
                    tmpCity.addSeaNeighbors(seaNeighbors);
                }
                                    
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numPlayers; i++) {
            ArrayList<Card> playerHand = new ArrayList<Card>();
            switch((i+1))
            {
                case 1:
                    playerHand.add(redDeck.pop());
                    playerHand.add(greenDeck.pop());
                    playerHand.add(yellowDeck.pop());
                    break;
                case 2:
                    playerHand.add(greenDeck.pop());
                    playerHand.add(yellowDeck.pop());
                    playerHand.add(redDeck.pop());
                    break;    
                case 3:
                    playerHand.add(yellowDeck.pop());
                    playerHand.add(redDeck.pop());
                    playerHand.add(greenDeck.pop());
                    break; 
                case 4:
                    playerHand.add(redDeck.pop());
                    playerHand.add(greenDeck.pop());
                    playerHand.add(yellowDeck.pop());
                    break; 
                case 5:
                    playerHand.add(greenDeck.pop());
                    playerHand.add(yellowDeck.pop());
                    playerHand.add(redDeck.pop());
                    break; 
                case 6:
                    playerHand.add(yellowDeck.pop());
                    playerHand.add(redDeck.pop());
                    playerHand.add(greenDeck.pop());
                    break;                     
            }
            players.add(new JTEPlayer((i + 1), playerHand, false));
        }
        currentPlayer = players.peek();

    }
    
    public Stack<Card> shuffleDeck(Stack<Card> deck)
    {
        Card[] deckArray = new Card[deck.size()];
        Stack<Card> shuffledDeck = new Stack<Card>();
        
        for(int i=0;i<deck.size();i++)
        {
            deckArray[i] = deck.pop();
        }    
        
        for(int i=0;i<deck.size();i++)
        {
            int newI;
            Card temp;
            Random randIndex = new Random();

            // pick a random index between 0 and cardsInDeck - 1
            newI = randIndex.nextInt(deck.size());

            // swap cards[i] and cards[newI]
            temp = deckArray[i];
            deckArray[i] = deckArray[newI];
            deckArray[newI] = temp;
        }
        
        for(int i=0;i<deck.size();i++)
        {
            shuffledDeck.push(deckArray[i]);
        }    
        return shuffledDeck;
    }        
            
    public JTEPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(JTEPlayer player)
    {
        currentPlayer = player;
    }        
    
    public City[] getCities() {
        return cities;
    }

    public ConcurrentLinkedQueue<JTEPlayer> getPlayers()
    {
        return players;
    }        
    
    public int getNumPlayers()
    {
        return numPlayers;
    }        
    
    public JTEFileLoader getFileLoader() {
        return fileLoader;
    }
    
    public Stack<Card> getRedDeck()
    {
        return redDeck;
    }        

    public Stack<Card> getGreenDeck()
    {
        return greenDeck;
    }        

    public Stack<Card> getYellowDeck()
    {
        return yellowDeck;
    }        
        
}
