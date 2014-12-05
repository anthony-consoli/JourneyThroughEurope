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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
    private HashMap<String, Vertex> verticies;

    //NUMBER OF PLAYERS
    private int numPlayers;
    private boolean[] cpuOrPlayer;

    //Players 
    ConcurrentLinkedQueue<JTEPlayer> players = new ConcurrentLinkedQueue<JTEPlayer>();

    //CURRENT PLAYER
    JTEPlayer currentPlayer;

    //Card Decks
    HashMap<String, Stack<Card>> cards;
    Stack<Card> greenDeck = new Stack<Card>();
    Stack<Card> redDeck = new Stack<Card>();
    Stack<Card> yellowDeck = new Stack<Card>();

    public JTEGameData(int num, boolean[] cpu) {
        
        numPlayers = num;
        cpuOrPlayer = cpu;
        //cities in the game
        fileLoader = new JTEFileLoader(schemaFile);
        cities = fileLoader.loadCities(citiesFile);
        cityHash = new HashMap<String, City>();
        verticies = new HashMap<String, Vertex>();

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
            verticies.put(cities[i].getName(), new Vertex(cities[i].getName()));
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
                Vertex vertCity = new Vertex("");
                ArrayList<Edge> tmpEdge = new ArrayList<Edge>();
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
                                    vertCity = verticies.get(theNode.getTextContent());
                                    break;
                                case "land":
                                    NodeList landList = theNode.getChildNodes();
                                    for (int k = 0; k < landList.getLength(); k++) {
                                        if (landList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                            landNeighbors.add(cityHash.get(landList.item(k).getTextContent()));
                                            tmpEdge.add(new Edge(verticies.get(landList.item(k).getTextContent()), 1));
                                        }
                                    }
                                    break;
                                case "sea":
                                    NodeList seaList = theNode.getChildNodes();
                                    for (int k = 0; k < seaList.getLength(); k++) {
                                        if (seaList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                            seaNeighbors.add(cityHash.get(seaList.item(k).getTextContent()));
                                            tmpEdge.add(new Edge(verticies.get(seaList.item(k).getTextContent()), 6));
                                        }
                                    }
                                    break;
                            }

                        }
                    }
                    tmpCity.addLandNeighbors(landNeighbors);
                    tmpCity.addSeaNeighbors(seaNeighbors);
                    Iterator<Edge> edgeIT = tmpEdge.iterator();
                    Edge[] edges = new Edge[tmpEdge.size()];
                    int pos = 0;
                    while(edgeIT.hasNext())
                    {
                        edges[pos] = edgeIT.next();
                        pos++;
                    }    
                        
                    vertCity.adjacencies = edges;
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
            players.add(new JTEPlayer((i + 1), playerHand, cpuOrPlayer[i]));
        }
        
        Iterator<JTEPlayer> it = players.iterator();
        while(it.hasNext())
        {

            JTEPlayer tmpPlayer = it.next();
            //for(int i=0;i<3;i++)
              //  System.out.print(tmpPlayer.getCards().get(i).getCityName() + "   ");
            ConcurrentLinkedQueue<City> playerPath = new ConcurrentLinkedQueue<City>();
            if(tmpPlayer.isCpu())
            {
                
                resetVerticies();
                
                computePaths(verticies.get(tmpPlayer.getCards().get(0).getCityName()));
                List<Vertex> path1 = getShortestPathTo(verticies.get(tmpPlayer.getCards().get(1).getCityName()));
                for(int i=1;i< path1.size();i++)
                {
                    playerPath.add(cityHash.get(path1.get(i).toString()));
                }
                
                resetVerticies();
                
                computePaths(verticies.get(tmpPlayer.getCards().get(1).getCityName()));
                List<Vertex> path2 = getShortestPathTo(verticies.get(tmpPlayer.getCards().get(2).getCityName()));
                for(int i=1;i< path2.size();i++)
                {
                    playerPath.add(cityHash.get(path2.get(i).toString()));
                }                 
                
                resetVerticies();
                
                computePaths(verticies.get(tmpPlayer.getCards().get(2).getCityName()));
                List<Vertex> path3 = getShortestPathTo(verticies.get(tmpPlayer.getCards().get(0).getCityName()));
                for(int i=1;i< path3.size();i++)
                {
                    playerPath.add(cityHash.get(path3.get(i).toString()));
                }         
                
                
                
                tmpPlayer.setTrip(playerPath);
            }    
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

    public HashMap<String,City> getCityHash()
    {
        return cityHash;
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
   


    public static void computePaths(Vertex source) {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);
        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();
            // Visit each edge exiting u
            for (Edge e : u.adjacencies) {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);
                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target) {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous) {
            path.add(vertex);
        }
        Collections.reverse(path);
        return path;


    }
    
    public void resetVerticies()
    {
        Iterator itV = verticies.entrySet().iterator();
        while(itV.hasNext())
        {
            Map.Entry pairs = (Map.Entry)itV.next(); 
            Vertex tmp = (Vertex) pairs.getValue();
            tmp.minDistance = Double.POSITIVE_INFINITY;
            tmp.previous = null;
        }    
    }        
            
}



    class Vertex implements Comparable<Vertex> {

    public final String name;
    public Edge[] adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;

    public Vertex(String argName) {
        name = argName;
    }

    public String toString() {
        return name;
    }

    public int compareTo(Vertex other) {
        return Double.compare(minDistance, other.minDistance);
    }
}

class Edge {

    public final Vertex target;
    public final double weight;

    public Edge(Vertex argTarget, double argWeight) {
        target = argTarget;
        weight = argWeight;
    }
}
