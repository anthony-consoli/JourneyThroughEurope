/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.file;
import JTE.game.City;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import xml_utilities.XMLUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author Anthony
 */
public class JTEFileLoader {
    
        // THIS WILL HELP US PARSE THE XML FILES
    private XMLUtilities xmlUtil;
    
    // THIS IS THE SCHEMA WE'LL USE
    private File levelSchema;
    
    //HASHMAP FOR CITIES
    private City[] cities;
    
        public JTEFileLoader(File initCitySchema)
    {
        // THIS KNOWS HOW TO READ AND ACCESS XML FILES
        xmlUtil = new XMLUtilities();
        
        // WE'LL USE THE SCHEMA FILE TO VALIDATE THE XML FILES
        levelSchema = initCitySchema;
    }
        public City[] loadCities(File cityFile)
        {
            try
            {
                Document doc = xmlUtil.loadXMLDocument( cityFile.getAbsolutePath(), 
                                                    levelSchema.getAbsolutePath());
    
                    
                // FIRST GET THE REGIONS LIST
                Node citiesListNode = doc.getElementsByTagName("cities").item(0);
                cities = new City[179];          
                            
                // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
                ArrayList<Node> intersectionsList = xmlUtil.getChildNodesWithName(citiesListNode, "city");

                for (int i = 0; i < intersectionsList.size(); i++)
                {
                    // GET THEIR DATA FROM THE DOC
                    Node cityNode = intersectionsList.get(i);
                    NamedNodeMap cityAttributes = cityNode.getAttributes();
                    //GET CARD COLOR
                    String cardColor = cityAttributes.getNamedItem("color").getNodeValue();                    
                    String nameText = cityAttributes.getNamedItem("name").getNodeValue();
                    String xText = cityAttributes.getNamedItem("x").getNodeValue();
                    String quadText = cityAttributes.getNamedItem("quad").getNodeValue();
                    double x = Double.parseDouble(xText);
                    x = (x*.33);
                    String yText = cityAttributes.getNamedItem("y").getNodeValue();
                    double y = Double.parseDouble(yText);
                    y = (y*.33);
                    int quad = Integer.parseInt(quadText);
                     // NOW MAKE AND ADD THE CITY
                    City newCity = new City(nameText, cardColor, x, y, quad);
                    cities[i] = newCity;
                }
                
                return cities;
        }
        catch(Exception e)
        {
            // LEVEL DIDN'T LOAD PROPERLY
            System.out.println(e.getMessage());
        }
            return cities;
    }
}
