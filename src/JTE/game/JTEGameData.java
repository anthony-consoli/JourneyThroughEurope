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

    public JTEGameData(int numPlayers) {
        //cities in the game
        fileLoader = new JTEFileLoader(schemaFile);
        cities = fileLoader.loadCities(citiesFile);
        cityHash = new HashMap<String, City>();

        for (int i = 0; i < cities.length; i++) {
            Card tmpCard = new Card(cities[i], cities[i].getColor());
            if (tmpCard.getColor().equals("green")) {
                greenDeck.push(tmpCard);
            } else if (tmpCard.getColor().equals("red")) {
                redDeck.push(tmpCard);
            } else if (tmpCard.getColor().equals("yellow")) {
                yellowDeck.push(tmpCard);
            }
            cityHash.put(cities[i].getName(), cities[i]);
        }

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
                    System.out.println(tmpCity.getName());
                }
                                    
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numPlayers; i++) {
            players.add(new JTEPlayer((i + 1), false));
        }
        currentPlayer = players.peek();

    }

    public JTEPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public City[] getCities() {
        return cities;
    }

    public JTEFileLoader getFileLoader() {
        return fileLoader;
    }

}
